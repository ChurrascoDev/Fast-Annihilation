package com.github.imthenico.annihilation.api.equipment;

import org.bukkit.potion.PotionEffect;

import java.util.Objects;

public class Kit {

    private final Equipment equipment;
    private final PotionEffect[] effects;

    public Kit(
            Equipment equipment,
            PotionEffect[] effects
    ) {
        this.equipment = Objects.requireNonNull(equipment);
        this.effects = effects == null ? new PotionEffect[0] : effects;
    }

    public PotionEffect[] getEffects() {
        return effects;
    }

    public Equipment getEquipment() {
        return equipment;
    }
}