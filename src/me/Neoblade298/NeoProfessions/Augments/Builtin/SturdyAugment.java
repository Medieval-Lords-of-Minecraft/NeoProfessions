package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModShieldsAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;
import me.neoblade298.neosapiaddons.AddShieldsEvent;

public class SturdyAugment extends Augment implements ModShieldsAugment {
	private static double shieldGainMult = AugmentManager.getValue("sturdy.shield-multiplier-base");
	private static double shieldGainMultLvl = AugmentManager.getValue("sturdy.shield-multiplier-per-lvl");
	
	public SturdyAugment() {
		super();
		this.name = "Sturdy";
		this.etypes = Arrays.asList(new EventType[] {EventType.SHIELDS});
	}

	public SturdyAugment(int level) {
		super(level);
		this.name = "Sturdy";
		this.etypes = Arrays.asList(new EventType[] {EventType.SHIELDS});
	}

	@Override
	public double getShieldsGainMult(Player p) {
		return shieldGainMult + (shieldGainMultLvl * ((level / 5) - 1));
	}

	@Override
	public Augment createNew(int level) {
		return new SturdyAugment(level);
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases amount of shields you");
		lore.add("§7receive by §f" + formatPercentage(getShieldsGainMult(user)) + "%§7.");
		lore.add("§7Only works on shields you receive from others.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean canUse(Player user, AddShieldsEvent e) {
		return user != e.getCaster();
	}
}
