package me.Neoblade298.NeoProfessions.Augments;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Objects.Rarity;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;

public interface ModProfessionHarvestAugment {
	
	public default void applyHarvestEffects(Player user) {
		// Empty unless overridden
	}
	
	public default double getAmountMult(Player user) {
		return 1;
	}
	
	public default HashMap<Rarity, Double> getRarityMults(Player user) {
		return null;
	}
	
	public default double getChance() {
		return 0;
	}
	
	public abstract boolean canUse(Player user, ProfessionType type);
}
