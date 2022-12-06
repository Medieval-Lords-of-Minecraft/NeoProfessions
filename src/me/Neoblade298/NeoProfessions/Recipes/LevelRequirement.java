package me.Neoblade298.NeoProfessions.Recipes;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Managers.ProfessionManager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.Profession;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;

public class LevelRequirement implements RecipeRequirement {
	ProfessionType type;
	int min;
	
	public LevelRequirement(ProfessionType type, int min) {
		this.type = type;
		this.min = min;
	}
	
	public LevelRequirement(String[] lineArgs) {
		this.min = 1;
		this.type = ProfessionType.CRAFTER;
		
		for (String lineArg : lineArgs) {
			String[] args = lineArg.split(":");
			if (args[0].equalsIgnoreCase("type")) {
				this.type = ProfessionType.valueOf(args[1].toUpperCase());
			}
			else if (args[0].equalsIgnoreCase("min")) {
				this.min = Integer.parseInt(args[1]);
			}
		}
	}
	
	public boolean passesReq(Player p, int amount) {
		HashMap<ProfessionType, Profession> profs = ProfessionManager.getAccount(p.getUniqueId());
		return profs != null && profs.get(type).getLevel() >= min;
	}

	public String failMessage(Player p) {
		return "§4[§c§lMLMC§4] §cYou aren't a high enough level §e" + type.getDisplay() + "§c!";
	}
	
	public String getLoreString(Player p) {
		String msg = passesReq(p, 1) ? "§a" : "§c";
		msg += "- Lv " + min + " " + type.getDisplay();
		return msg;
	}
}
