package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ServiceLoader;

import org.junit.jupiter.api.Test;

import com.gregtech.gregtech.api.addon.AddonMachineContributor;
import com.gregtech.gregtech.api.addon.AddonMaterialContributor;
import com.gregtech.gregtech.api.addon.platform.BehaviorMachineContributor;
import com.lombinaxmasson.gt6ktfruaddon.content.Materials;
import com.lombinaxmasson.gt6ktfruaddon.registry.Machines;

/**
 * GT6CE finds the addon through {@code META-INF/services}. A service file that names a class which
 * moved or was renamed fails here as a {@link java.util.ServiceConfigurationError}, not at game start.
 */
class ServiceRegistrationTest {
    @Test
    void machineContributorIsRegistered() {
        assertTrue(ServiceLoader.load(AddonMachineContributor.class).stream()
                .anyMatch(provider -> provider.type() == Machines.class));
    }

    @Test
    void behaviorContributorIsRegistered() {
        assertTrue(ServiceLoader.load(BehaviorMachineContributor.class).stream()
                .anyMatch(provider -> provider.type() == Machines.class));
    }

    @Test
    void materialContributorIsRegistered() {
        assertTrue(ServiceLoader.load(AddonMaterialContributor.class).stream()
                .anyMatch(provider -> provider.type() == Materials.class));
    }
}
