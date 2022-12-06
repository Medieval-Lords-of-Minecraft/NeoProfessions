package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Managers.RecipeManager;

public class KnowledgeRequirement implements RecipeRequirement {
	String key;
	
	public KnowledgeRequirement(String[] lineArgs) {
		this.key = "default";
		
		for (String lineArg : lineArgs) {
			String[] args = lineArg.split(":");
			if (args[0].equalsIgnoreCase("key")) {
				this.key = args[1];
			}
		}
	}
	
	public boolean passesReq(Player p, int amount) {
		return RecipeManager.hasKnowledge(p, key);
	}

	public String failMessage(Player p) {
		return "§4[§c§lMLMC§4] §cYou lack the recipe!";
	}
	
	public String getLoreString(Player p) {
		String msg = passesReq(p, 1) ? "§a" : "§c";
		msg += "- Recipe knowledge";
		return msg;
	}
}
