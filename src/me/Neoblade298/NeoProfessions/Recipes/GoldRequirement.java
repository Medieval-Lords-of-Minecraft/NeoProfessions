package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Professions;

public class GoldRequirement implements RecipeRequirement {
	int amount;
	
	public GoldRequirement(String[] lineArgs) {
		this.amount = 500;
		
		for (String lineArg : lineArgs) {
			String[] args = lineArg.split(":");
			if (args[0].equalsIgnoreCase("amount")) {
				this.amount = Integer.parseInt(args[1]);
			}
		}
	}
	
	public boolean passesReq(Player p, int amount) {
		return Professions.econ.has(p, this.amount * amount);
	}

	public String failMessage(Player p) {
		return "§4[§c§lMLMC§4] §cYou don't have enough gold!";
	}
	
	public void useReq(Player p, int amount) {
		Professions.econ.withdrawPlayer(p, this.amount * amount);
	}
	
	public String getLoreString(Player p) {
		String msg = passesReq(p, 1) ? "§a" : "§c";
		msg += "- " + amount + "g";
		return msg;
	}
}
