package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

import me.neoblade298.neomythicextension.events.ChestDropEvent;

public interface ModChestDropAugment {
	
	public default void applyExpEffects(Player user) {
		
	}
	
	public default double getChestChanceFlat(Player user) {
		return 0;
	}
	
	public default double getChestChanceMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user, ChestDropEvent e);
}
