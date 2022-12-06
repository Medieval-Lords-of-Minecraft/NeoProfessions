package me.Neoblade298.NeoProfessions.Utilities;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Util {
	public final static Random gen = new Random();
	public static void sendMessage(CommandSender p, String m) {
		String message = "&4[&c&lMLMC&4] " + m;
		p.sendMessage(message.replaceAll("&", "§"));
	}
	public static void sendMessageNoPrefix(CommandSender p, String m) {
		p.sendMessage(m.replaceAll("&", "§"));
	}

	public ItemStack setAmount(ItemStack item, int amount) {
		item.setAmount(amount);
		return item;
	}

	public int getEssenceLevel(ItemStack item) {
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return -1;
		}
		String lore = item.getItemMeta().getLore().get(0);
		if (lore.contains("Essence")) {
			return item.getEnchantmentLevel(Enchantment.DURABILITY);
		}
		return -1;
	}

	public int getItemLevel(ItemStack item) {
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return -1;
		}
		ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
		String lv = null;

		// Find the tier string
		for (String line : lore) {
			if (line.contains("Level Req:")) {
				lv = line;
				break;
			}
		}

		if (lv != null) {
			return Integer.parseInt(lv.split(" ")[2]);
		}
		return -1;
	}

	public static boolean isWeapon(ItemStack item) {
		String type = item.getType().name();
		return type.endsWith("SWORD") || type.endsWith("AXE") || type.endsWith("HOE") || type.endsWith("SHIELD") ||
				type.endsWith("BOW") || type.endsWith("TRIDENT");
	}

	public static boolean isArmor(ItemStack item) {
		String type = item.getType().name();
		return type.endsWith("HELMET") || type.endsWith("CHESTPLATE") || type.endsWith("LEGGINGS") || type.endsWith("BOOTS");
	}

	public int roundToLevel(int toRound, int levelInterval) {
		return toRound - (toRound % levelInterval);
	}
}
