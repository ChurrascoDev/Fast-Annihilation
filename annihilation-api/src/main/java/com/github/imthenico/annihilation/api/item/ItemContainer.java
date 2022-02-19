package com.github.imthenico.annihilation.api.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ItemContainer implements Iterable<ItemStack> {

    private final Map<Integer, ItemStack> itemStacks;

    public ItemContainer(Map<Integer, ItemStack> itemStacks) {
        this.itemStacks = new HashMap<>(itemStacks.size());

        itemStacks.forEach((k, v) -> this.itemStacks.put(k, v.clone()));
    }

    @NotNull
    public ItemStack getItem(int slot) {
        return itemStacks.get(slot);
    }

    public int size() {
        return itemStacks.size();
    }

    @NotNull
    @Override
    public Iterator<ItemStack> iterator() {
        return itemStacks.values().iterator();
    }

    public Map<Integer, ItemStack> asMap() {
        return Collections.unmodifiableMap(itemStacks);
    }
}