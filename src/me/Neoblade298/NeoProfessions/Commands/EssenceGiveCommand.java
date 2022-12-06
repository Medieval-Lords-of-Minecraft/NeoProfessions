package me.Neoblade298.NeoProfessions.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Managers.CurrencyManager;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class EssenceGiveCommand implements CommandExecutor {

	Professions main;

	public EssenceGiveCommand(Professions main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if (!sender.hasPermission("neoprofessions.admin")) {
			return true;
		}

		if (args.length != 3) {
			sender.sendMessage("§4/egive [player] [level] [amount]");
		}
		else {
			Player recipient = Bukkit.getPlayer(args[0]);
			if (recipient == null) {
				Util.sendMessage(sender, "&cPlayer must be online!");
				return true;
			}
			// Normalize essence level
			int level = Integer.parseInt(args[1]);
			level -= level % 5;
			if (!(level <= 60 && level > 0 && level % 5 == 0)) {
				Util.sendMessage(sender, "&cFailed to give essence, invalid level " + level + "!");
				return true;
			}
			int amount = Integer.parseInt(args[2]);
			if (amount <= 0 || amount >= 99999) {
				Util.sendMessage(sender, "&cFailed to give essence, invalid amount " + amount + "!");
				return true;
			}
			
			Util.sendMessage(sender,
					"&7You gave &c" + recipient.getName() + " &e" + amount + " &6Lv " + level + " &7Essence");
			CurrencyManager.add(recipient, level, amount);
			return true;
		}
		return true;
	}
}