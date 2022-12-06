package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

public interface ModRegenAugment {
	
	public default void applyRegenEffects(Player user, double healing) {
		
	}
	
	public default double getRegenFlat(Player user) {
		return 0;
	}
	
	public default double getRegenMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user);
}
