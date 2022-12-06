package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

import com.sucy.skill.api.event.FlagApplyEvent;

public interface ModFlagAugment {
	
	public default void applyFlagEffects(FlagApplyEvent e) {
		// Empty unless overridden
	}
	
	public default double getFlagTimeFlat(Player user) {
		return 0;
	}
	
	public default double getFlagTimeMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(FlagApplyEvent e);
}
