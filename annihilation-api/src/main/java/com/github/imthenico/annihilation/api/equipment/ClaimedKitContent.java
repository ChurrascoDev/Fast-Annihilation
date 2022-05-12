package com.github.imthenico.annihilation.api.equipment;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.item.ItemContainer;
import java.util.Objects;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class ClaimedKitContent {

    private final UUID ownerId;
    private final Kit kit;

    public ClaimedKitContent(Player owner, Kit kit) {
        this.ownerId = owner.getUniqueId();
        this.kit = kit;
    }

    public ClaimedItem[] getInventory() {
        Equipment equipment = kit.getEquipment();

        return asClaimedItems(equipment.getInventoryItems().asMap().values(), owner());
    }

    public ClaimedItem[] getArmor() {
        Equipment equipment = kit.getEquipment();

        return asClaimedItems(equipment.getArmorPieces().values(), owner());
    }

    public ClaimedItem[] getAllEquipment() {
        Equipment equipment = kit.getEquipment();
        Map<Armor, ItemStack> armorPieces = equipment.getArmorPieces();
        ItemContainer inventoryItems = equipment.getInventoryItems();

        List<ItemStack> itemStacks = new ArrayList<>(
                armorPieces.size() + inventoryItems.size()
        );

        itemStacks.addAll(armorPieces.values());
        itemStacks.addAll(inventoryItems.asMap().values());

        return asClaimedItems(itemStacks, owner());
    }

    public ClaimedEffect[] getEffects() {
        PotionEffect[] effects = kit.getEffects();

        ClaimedEffect[] claimedEffects = new ClaimedEffect[effects.length];

        Player player = owner();

        for (int i = 0; i < effects.length; i++) {
            claimedEffects[i] = new ClaimedEffect(effects[i], player);
        }

        return claimedEffects;
    }

    public Kit getKit() {
        return kit;
    }

    private Player owner() {
        return Bukkit.getPlayer(ownerId);
    }

    public static ClaimedKitContent of(MatchPlayer matchPlayer) {
        Kit kit = matchPlayer.getKit();

        if (kit == null)
            kit = empty();

        return new ClaimedKitContent(matchPlayer.getHandle().getPlayer(), kit);
    }

    private static Kit empty() {
        Equipment equipment = new Equipment(
                Collections.emptyMap(),
                new ItemContainer(Collections.emptyMap())
        );

        return new Kit(equipment, null);
    }

    private static ClaimedItem[] asClaimedItems(Collection<ItemStack> itemStacks, Player player) {
        ClaimedItem[] claimedItems = new ClaimedItem[itemStacks.size()];

        int i = 0;

        for (ItemStack itemStack : itemStacks) {
            claimedItems[i] = new ClaimedItem(itemStack, player);

            i++;
        }

        return claimedItems;
    }

    public static class ClaimedItem {
        private final ItemStack itemStack;
        private final Player owner;
        private final NBTItem nbtItem;

        public ClaimedItem(ItemStack itemStack, Player owner) {
            this.itemStack = itemStack;
            this.owner = owner;
            this.nbtItem = new NBTItem(itemStack);
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public boolean isScrapped() {
            return owner.getInventory().contains(itemStack);
        }

        public boolean isSpecial() {
            return nbtItem.hasKey("special-item");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClaimedItem that = (ClaimedItem) o;
            return itemStack.equals(that.itemStack) && owner.getUniqueId().equals(that.owner.getUniqueId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(itemStack, owner.getUniqueId());
        }
    }

    public static class ClaimedEffect {
        private final PotionEffect potionEffect;
        private final Player owner;

        public ClaimedEffect(PotionEffect potionEffect, Player owner) {
            this.potionEffect = potionEffect;
            this.owner = owner;
        }

        public PotionEffect getPotionEffect() {
            return potionEffect;
        }

        public boolean isActive() {
            return owner.getActivePotionEffects().contains(potionEffect);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClaimedEffect that = (ClaimedEffect) o;
            return potionEffect.equals(that.potionEffect) && owner.getUniqueId().equals(that.owner.getUniqueId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(potionEffect, owner.getUniqueId());
        }
    }
}