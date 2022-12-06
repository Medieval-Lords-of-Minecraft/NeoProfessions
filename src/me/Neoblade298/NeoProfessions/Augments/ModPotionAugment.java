package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;

public interface ModPotionAugment {
	public default void applyPotionEffects(Player user, EntityPotionEffectEvent e) {
		// Empty unless overridden
	}
	
	public abstract boolean canUse(Player user, EntityPotionEffectEvent e);
}
