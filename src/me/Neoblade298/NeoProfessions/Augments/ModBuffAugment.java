package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sucy.skill.api.event.SkillBuffEvent;

public interface ModBuffAugment {
	
	public default void applyBuffEffects(Player user, LivingEntity target) {
		
	}
	
	public default double getBuffTimeMult(LivingEntity user) {
		return 0;
	}
	
	public default double getBuffFlat(LivingEntity user) {
		return 0;
	}
	
	public default double getBuffMult(LivingEntity user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user, LivingEntity target, SkillBuffEvent e);
}
