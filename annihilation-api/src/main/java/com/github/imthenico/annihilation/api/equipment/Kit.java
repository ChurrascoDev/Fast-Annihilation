package com.github.imthenico.annihilation.api.equipment;

import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.potion.PotionEffect;

public class Kit {

    private final Equipment equipment;
    private final PotionEffect[] effects;

    public Kit(
            Equipment equipment,
            PotionEffect[] effects
    ) {
        this.equipment = Validate.notNull(equipment);
        this.effects = Validate.defIfNull(effects, new PotionEffect[0]);
    }

    public PotionEffect[] getEffects() {
        return effects;
    }

    public Equipment getEquipment() {
        return equipment;
    }
}