package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Managers.GardenManager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;

public class GardenSizeRequirement implements RecipeRequirement {
	ProfessionType type;
	int size;
	
	public GardenSizeRequirement(String[] lineArgs) {
		this.type = ProfessionType.HARVESTER;
		this.size = 1;
		
		for (String lineArg : lineArgs) {
			String[] args = lineArg.split(":");
			if (args[0].equalsIgnoreCase("type")) {
				type = ProfessionType.valueOf(args[1].toUpperCase());
			}
			else if (args[0].equalsIgnoreCase("size")) {
				size = Integer.parseInt(args[1]);
			}
		}
	}
	
	public boolean passesReq(Player p, int amount) {
		return GardenManager.getGarden(p, type).getSize() == size;
	}

	public String failMessage(Player p) {
		return "§4[§c§lMLMC§4] §cYour " + type.getDisplay() + " Garden is not large enough!";
	}
	
	public String getLoreString(Player p) {
		String msg = passesReq(p, 1) ? "§a" : "§c";
		msg += "- " + type.getDisplay() + " Garden size of " + size;
		return msg;
	}
}
