package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

public interface ModTauntAugment {
	
	public default void applyTauntEffects(Player user, double amount) {
		// Empty unless overridden
	}
	
	public default double getTauntGainFlat(Player user) {
		return 0;
	}
	
	public default double getTauntGainMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user);
}
