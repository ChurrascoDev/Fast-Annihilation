package com.github.imthenico.annihilation.api.event;

import com.google.common.base.Function;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class PlayerDamageByEntityEvent extends EntityDamageByEntityEvent {
    public PlayerDamageByEntityEvent(Entity damager, Entity damagee, DamageCause cause, int damage) {
        super(damager, damagee, cause, damage);
    }

    public PlayerDamageByEntityEvent(Entity damager, Entity damagee, DamageCause cause, double damage) {
        super(damager, damagee, cause, damage);
    }

    public PlayerDamageByEntityEvent(Entity damager, Entity damagee, DamageCause cause, Map<DamageModifier, Double> modifiers, Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damager, damagee, cause, modifiers, modifierFunctions);
    }

    @Override
    public Player getEntity() {
        return (Player) super.getEntity();
    }

    public boolean playerDamager() {
        return getDamager() instanceof Player;
    }

    public Player getDamagerAsPlayer() {
        return (Player) getDamager();
    }
}