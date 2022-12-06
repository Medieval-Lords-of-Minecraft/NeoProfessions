package me.Neoblade298.NeoProfessions.Augments;

import org.bukkit.entity.Player;

import com.sucy.skill.api.event.PlayerSkillCastSuccessEvent;

public interface ModSkillCastAugment {
	public default void applySkillCastEffects(Player user, PlayerSkillCastSuccessEvent e) {
		// Empty unless overridden
	}
	
	public abstract boolean canUse(Player user, PlayerSkillCastSuccessEvent e);
}
