package me.Neoblade298.NeoProfessions.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Managers.CurrencyManager;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class EssencePayCommand implements CommandExecutor {

	Professions main;

	public EssencePayCommand(Professions main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		Player p = (Player) sender;

		if (args.length != 3) {
			sender.sendMessage("§c/epay [player] [level] [amount]");
		}
		else {
			Player recipient = Bukkit.getPlayer(args[0]);
			if (recipient == null) {
				Util.sendMessage(p, "&cPlayer must be online!");
				return true;
			}
			// Normalize essence level
			int level = Integer.parseInt(args[1]);
			level -= level % 5;
			if (!(level <= 60 && level > 0 && level % 5 == 0)) {
				Util.sendMessage(sender, "&cFailed to pay essence, invalid level " + level + "!");
				return true;
			}
			int amount = Integer.parseInt(args[2]);
			if (amount <= 0 || amount >= 99999) {
				Util.sendMessage(sender, "&cFailed to pay essence, invalid amount " + amount + "!");
				return true;
			}
			else if (!CurrencyManager.hasEnough(p, level, amount)) {
				Util.sendMessage(sender, "&cFailed to pay essence, Insufficient essence!");
				return true;
			}
			
			Util.sendMessage(p,
					"&7You paid &c" + recipient.getName() + " &e" + amount + " &6Lv " + level + " &7Essence");
			Util.sendMessage(recipient,
					"&c" + p.getName() + "&7 has paid you &e" + amount + " &6Lv " + level + " &7Essence");
			CurrencyManager.add(recipient, level, amount);
			CurrencyManager.subtract(p, level, amount);
			return true;
		}
		return true;
	}
}