package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sucy.skill.api.event.PlayerCalculateDamageEvent;
import me.Neoblade298.NeoProfessions.Objects.FlagSettings;

public interface ModDamageTakenAugment {
	public default void applyDamageTakenEffects(Player user, LivingEntity target, PlayerCalculateDamageEvent e) {
		// Empty unless overridden
	}
	
	public default double getDamageTakenFlat(LivingEntity user) {
		return 0;
	}
	
	public default double getDamageReturnedFlat(LivingEntity user) {
		return 0;
	}
	
	public default double getDamageTakenMult(LivingEntity user) {
		return 0;
	}
	
	public abstract boolean canUse(Player user, LivingEntity target, PlayerCalculateDamageEvent e);
	
	public default FlagSettings setFlagAfter() {
		return null;
	}
}
