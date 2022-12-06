package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

import com.sucy.skill.api.event.PlayerCriticalCheckEvent;
import com.sucy.skill.api.player.PlayerData;

public interface ModCritCheckAugment {
	
	public default void applyCritEffects(PlayerData user, double chance) {
		// Empty unless overridden
	}
	
	public default double getCritChanceFlat(Player user) {
		return 0;
	}
	
	public default double getCritChanceMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(PlayerData user, PlayerCriticalCheckEvent e);
}
