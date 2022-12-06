package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sucy.skill.api.player.PlayerData;

public interface ModHealAugment {
	
	public default void applyHealEffects(PlayerData user, LivingEntity target, double healing) {
		
	}
	
	public default double getHealFlat(PlayerData user) {
		return 0;
	}
	
	public default double getHealMult(Player user) {
		return 0;
	}
	
	public abstract boolean canUse(PlayerData user, LivingEntity target);
}
