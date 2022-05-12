package com.github.imthenico.annihilation.api.equipment;

import com.github.imthenico.annihilation.api.item.ItemContainer;
import java.util.Objects;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Equipment {

    private final Map<Armor, ItemStack> armorPieces;
    private final ItemContainer inventory;

    public Equipment(
            Map<Armor, ItemStack> armorPieces,
            ItemContainer inventory
    ) {
        this.armorPieces = new HashMap<>(armorPieces.size());
        this.inventory = Objects.requireNonNull(inventory);

        armorPieces.forEach((k, v) -> this.armorPieces.put(k, v.clone()));
    }

    public Map<Armor, ItemStack> getArmorPieces() {
        return armorPieces;
    }

    public ItemContainer getInventoryItems() {
        return inventory;
    }
}