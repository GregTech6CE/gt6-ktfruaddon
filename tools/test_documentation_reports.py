"""Guard the Java output-argument scanner against input/output confusion."""
import unittest
from documentation_reports import arguments


class JavaArgumentsTest(unittest.TestCase):
    def test_nested_inputs_do_not_shift_output(self):
        text = '"Source:1", MAP, ticks(2).euPerTick(3), new ItemStack[] {block("input", 2), own("x", 1)}, new ItemStack[] {block("output", 1)}, NO_FLUIDS, NO_FLUIDS);'
        args = arguments(text, 0)
        self.assertEqual(7, len(args))
        self.assertIn('"output"', args[4])
        self.assertNotIn('"input"', args[4])

    def test_string_delimiters_are_not_syntax(self):
        self.assertEqual(['"a,b)"', 'call("x", new int[] {1, 2})'],
                         arguments('"a,b)", call("x", new int[] {1, 2}))', 0))

    def test_truncated_call_fails(self):
        with self.assertRaises(ValueError):
            arguments('"source", call(1)', 0)


if __name__ == '__main__':
    unittest.main()
