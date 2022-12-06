package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

import com.sucy.skill.api.event.PlayerExperienceGainEvent;

public interface ModExpAugment {
	
	public default void applyExpEffects(Player user) {
		
	}
	
	public default double getExpFlat(Player user) {
		return 0;
	}
	
	public default double getExpMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user, PlayerExperienceGainEvent e);
}
