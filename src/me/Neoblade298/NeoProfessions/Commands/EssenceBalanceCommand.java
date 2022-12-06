package me.Neoblade298.NeoProfessions.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Managers.CurrencyManager;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class EssenceBalanceCommand implements CommandExecutor {

	Professions main;

	public EssenceBalanceCommand(Professions main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		Player toView;
		if (args.length == 0) {
			toView = (Player) sender;
		}
		else {
			toView = Bukkit.getPlayer(args[0]);
		}
		
		if (toView == null) {
			Util.sendMessage(sender, "&cPlayer must be online!");
			return true;
		}
		
		String msg = "&65: &f" + CurrencyManager.get(toView, 5);
		for (int i = 10; i <= 60; i += 5) {
			msg += "&7, &6" + i + ": &f" + CurrencyManager.get(toView, i);
		}
		Util.sendMessage(sender, msg);
		return true;
	}
}