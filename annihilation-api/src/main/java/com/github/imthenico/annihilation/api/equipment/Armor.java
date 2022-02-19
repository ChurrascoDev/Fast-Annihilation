package com.github.imthenico.annihilation.api.equipment;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public enum Armor {

    HELMET,
    CHEST_PLATE,
    LEGGINGS,
    BOOTS;

    public void set(PlayerInventory inventory, ItemStack itemStack) {
        switch (this) {
            case HELMET:
                inventory.setHelmet(itemStack);
                break;
            case CHEST_PLATE:
                inventory.setChestplate(itemStack);
                break;
            case LEGGINGS:
                inventory.setLeggings(itemStack);
                break;
            case BOOTS:
                inventory.setBoots(itemStack);
                break;
        }
    }
}