"""Rebuild WP7 static inventories; no upstream checkout or game launch required."""
import argparse
import json
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / 'src/main/resources/assets/gt6ktfruaddon'
JAVA = ROOT / 'src/main/java/com/lombinaxmasson/gt6ktfruaddon'


def arguments(text, start):
    """Split a Java call at top-level commas, respecting strings and nested arrays/calls."""
    parts, begin, stack, quoted, escaped = [], start, [], False, False
    for i in range(start, len(text)):
        c = text[i]
        if quoted:
            if escaped:
                escaped = False
            elif c == '\\':
                escaped = True
            elif c == '"':
                quoted = False
        elif c == '"':
            quoted = True
        elif c in '({[':
            stack.append(c)
        elif c in ')}]':
            if not stack:
                parts.append(text[begin:i].strip())
                return parts
            stack.pop()
        elif c == ',' and not stack:
            parts.append(text[begin:i].strip())
            begin = i + 1
    raise ValueError('Unterminated Java call')


def reports():
    en = json.loads((ASSETS / 'lang/en_us.json').read_text(encoding='utf-8-sig'))
    zh = json.loads((ASSETS / 'lang/zh_cn.json').read_text(encoding='utf-8-sig'))
    same = sorted(k for k in en if zh.get(k) == en[k])
    missing = sorted(set(en) - set(zh))
    translation = ['# 中文待核对清单', '',
        '由 `python tools/documentation_reports.py` 生成。英文与中文相同只表示待人工核对；化学式、缩写等可能无需翻译。', '',
        f'英文键 {len(en)} 条；同文 {len(same)} 条；缺中文键 {len(missing)} 条。', '',
        '| 键 | 英文/当前中文 |', '| --- | --- |']
    for key in same + missing:
        value = en[key].replace('|', '\\|').replace('\n', '<br>')
        translation.append(f'| `{key}` | {value} |')

    inventory = ROOT / 'build/reports/documentation-block-ids.txt'
    if not inventory.exists():
        raise SystemExit('Run gradlew documentationInventory before generating documentation reports')
    blocks = sorted(inventory.read_text(encoding='utf-8-sig').splitlines())
    if not blocks or len(blocks) != len(set(blocks)):
        raise ValueError('Empty or duplicate registry block inventory')
    hits = {b: [] for b in blocks}
    aliases = json.loads((ROOT / 'tools/mappings.json').read_text())['block_aliases']
    source = (JAVA / 'generated/SourceRecipes.java').read_text(encoding='utf-8-sig')
    count = 0
    for match in re.finditer(r'^        add\("', source, re.MULTILINE):
        args = arguments(source, match.end() - 1)
        if len(args) < 7:
            raise ValueError('SourceRecipes.add signature changed')
        count += 1
        # add(source, map, timing, itemsIn, itemsOut, fluidsIn, fluidsOut, ...)
        for block in re.findall(r'\bblock\("([^"]+)"', args[4]):
            block = aliases.get(block, block)
            if block in hits:
                hits[block].append(args[0].strip('"'))
    for path in sorted((ROOT / 'src/main/resources/data').glob('*/recipe/**/*.json')):
        recipe = json.loads(path.read_text(encoding='utf-8-sig'))
        result = recipe.get('result', {})
        item = result.get('id', result.get('item', '')) if isinstance(result, dict) else result
        if item.startswith('gt6ktfruaddon:') and item.split(':', 1)[1] in hits:
            hits[item.split(':', 1)[1]].append(path.relative_to(ROOT).as_posix())
    found = sum(bool(v) for v in hits.values())
    availability = ['# 方块生存配方产物清单', '',
        '先执行 `gradlew documentationInventory`，再由 `python tools/documentation_reports.py` 生成。方块全集由 `gradlew documentationInventory` 从 Features.blockIds() 导出；机器、零件、气罐和管道均包含（包括 GT6CE 动态生成资源的方块）。', '',
        f'共 {len(blocks)} 个方块；{found} 个出现在所扫描配方产物，{len(blocks)-found} 个未发现。扫描 {count} 行生成源配方及 data/*/recipe/**/*.json。', '',
        '这是静态直接产物索引，不证明输入、能源、研究条件或整条生存链可达。未发现也不等于创造专属：不扫描 GT6CE 自动生成配方、数据包覆盖、掉落或玩家操作产物。手写 registry/Recipes.java 当前没有自有方块输出；新增此类输出时需扩展扫描器。', '',
        '| 方块 id（命名空间 gt6ktfruaddon） | 直接配方产物 | 来源 |', '| --- | --- | --- |']
    for block, refs in hits.items():
        availability.append(f'| `{block}` | {"有" if refs else "未发现"} | {"；".join(sorted(set(refs))) or "—"} |')
    return {'untranslated-zh-cn.md': '\n'.join(translation) + '\n',
            'survival-availability.md': '\n'.join(availability) + '\n'}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--check', action='store_true')
    args = parser.parse_args()
    for name, content in reports().items():
        path = ROOT / 'docs' / name
        if args.check:
            if not path.exists() or path.read_bytes() != content.encode('utf-8'):
                raise SystemExit(f'Stale documentation report: {path}')
        else:
            path.write_bytes(content.encode('utf-8'))
        print(f'{"Checked" if args.check else "Wrote"} {path.relative_to(ROOT)}')
