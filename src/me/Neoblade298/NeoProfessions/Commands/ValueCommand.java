package me.Neoblade298.NeoProfessions.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.neoblade298.neocore.bukkit.util.Util;


public class ValueCommand implements CommandExecutor {
	Professions main;

	public ValueCommand(Professions main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0 && sender instanceof Player) {
			Player p = (Player) sender;
			ItemStack item = p.getInventory().getItemInMainHand();
			String display = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ?
					item.getItemMeta().getDisplayName() : item.getType().name();

			if (item == null || item.getType().isAir()) {
				Util.msg(sender, "&cYou're not holding anything!");
				return true;
			}

			NBTItem nbti = new NBTItem(item);
			double value = 0;

			if (!nbti.getString("value").isBlank()) {
				value = Double.parseDouble(nbti.getString("value"));
			}
			else {
				value = nbti.getDouble("value");
			}
			Util.msg(sender, "&7Value of &c" + display + "&7: &e" + value + "g");
			return true;
		}
		return false;
	}
}