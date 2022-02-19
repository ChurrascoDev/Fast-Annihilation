package com.github.imthenico.annihilation.api.event;

import com.google.common.base.Function;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

public class PlayerDamageEvent extends EntityDamageEvent {

    public PlayerDamageEvent(Entity damagee, DamageCause cause, int damage) {
        super(damagee, cause, damage);
    }

    public PlayerDamageEvent(Entity damagee, DamageCause cause, double damage) {
        super(damagee, cause, damage);
    }

    public PlayerDamageEvent(Entity damagee, DamageCause cause, Map<DamageModifier, Double> modifiers, Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damagee, cause, modifiers, modifierFunctions);
    }

    @Override
    public Player getEntity() {
        return (Player) super.getEntity();
    }
}