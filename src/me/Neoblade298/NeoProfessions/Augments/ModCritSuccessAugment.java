package me.Neoblade298.NeoProfessions.Augments;

import com.sucy.skill.api.event.PlayerCriticalSuccessEvent;
import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Objects.FlagSettings;

public interface ModCritSuccessAugment {
	
	public default void applyCritSuccessEffects(PlayerData user, double chance) {
		// Empty unless overridden
	}
	
	public abstract boolean canUse(PlayerData user, PlayerCriticalSuccessEvent e);
	
	public default FlagSettings setFlag() {
		return null;
	}
}
