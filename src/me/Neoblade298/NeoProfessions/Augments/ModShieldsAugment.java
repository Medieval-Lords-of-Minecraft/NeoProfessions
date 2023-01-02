package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

import me.neoblade298.neosapiaddons.AddShieldsEvent;

public interface ModShieldsAugment {
	
	public default void applyShieldEffects(Player user, AddShieldsEvent e) {
		// Empty unless overridden
	}
	
	public default double getShieldsGainMult(Player user) {
		return 0;
	}
	
	public default double getShieldsDurationMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user, AddShieldsEvent e);
}
