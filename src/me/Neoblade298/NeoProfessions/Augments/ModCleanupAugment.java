package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

public interface ModCleanupAugment {
	public default void applyCleanupEffects(Player user) {
		// Empty unless overridden
	}
}
