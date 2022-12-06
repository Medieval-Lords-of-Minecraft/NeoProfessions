package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sucy.skill.api.event.PlayerCalculateDamageEvent;

import me.Neoblade298.NeoProfessions.Objects.FlagSettings;

public interface ModDamageDealtAugment {
	
	public default void applyDamageDealtEffects(Player user, LivingEntity target, double damage) {
		// Empty unless overridden
	}
	
	public default double getDamageDealtFlat(LivingEntity user, PlayerCalculateDamageEvent e) {
		return 0;
	}
	
	public default double getDamageDealtMult(LivingEntity user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user, LivingEntity target);
	
	public default FlagSettings setFlagAfter() {
		return null;
	}
}
