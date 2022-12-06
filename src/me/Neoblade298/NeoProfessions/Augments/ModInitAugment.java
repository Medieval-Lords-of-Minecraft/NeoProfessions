package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

public interface ModInitAugment {
	public default void applyInitEffects(Player user) {
		// Empty unless overridden
	}
}
