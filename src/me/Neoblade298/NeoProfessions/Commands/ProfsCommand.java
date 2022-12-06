package me.Neoblade298.NeoProfessions.Commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Managers.ProfessionManager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.Profession;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;


public class ProfsCommand implements CommandExecutor {
	Professions main;
	
	public ProfsCommand(Professions main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0 && sender instanceof Player) {
			sendProfs((Player) sender, sender);
			return true;
		}
		else if (args.length == 1) {
			sendProfs(Bukkit.getPlayer(args[0]), sender);
			return true;
		}
		return false;
	}
	
	private void sendProfs(Player viewed, CommandSender viewer) {
		HashMap<ProfessionType, Profession> account = ProfessionManager.getAccount(viewed.getUniqueId());
		for (ProfessionType prof : ProfessionType.values()) {
			String line = "&7-- " + getProfessionBlock(account.get(prof)) + "&7 --";
			viewer.sendMessage(line.replaceAll("&", "§"));
		}
	}
	
	private String getProfessionBlock(Profession prof) {
		return "&6[Lv " + prof.getLevel() + " " + prof.getType().getDisplay() + " &7(" +
				prof.getExp() + " / " + Profession.getNextLv().get(prof.getLevel()) + " XP)&6]";
	}
}