package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

import com.sucy.skill.api.event.PlayerCriticalDamageEvent;

public interface ModCritDamageAugment {
	
	public default void applyCritDamageEffects(Player user, double damage) {
		// Empty unless overridden
	}
	
	public default double getCritDamageFlat(Player user) {
		return 0;
	}
	
	public default double getCritDamageMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user, PlayerCriticalDamageEvent e);
}
