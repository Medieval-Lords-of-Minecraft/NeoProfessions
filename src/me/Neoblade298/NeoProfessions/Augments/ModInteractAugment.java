package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public interface ModInteractAugment {
	public default void applyInteractEffects(Player user, PlayerInteractEvent e) {
		// Empty unless overridden
	}
	
	public abstract boolean canUse(Player user, PlayerInteractEvent e);
}
