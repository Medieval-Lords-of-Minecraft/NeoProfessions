package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.entity.Player;

public interface RecipeRequirement {
	public boolean passesReq(Player p, int amount);
	public String failMessage(Player p);
	public String getLoreString(Player p);
	
	public default void useReq(Player p, int amount) {
		
	}
}
