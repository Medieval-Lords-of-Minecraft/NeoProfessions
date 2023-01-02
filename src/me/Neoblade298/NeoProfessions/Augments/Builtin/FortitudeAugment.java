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

public class FortitudeAugment extends Augment implements ModShieldsAugment {
	private static double shieldDurationMult = AugmentManager.getValue("fortitude.shield-duration-multiplier-base");
	private static double shieldDurationMultLvl = AugmentManager.getValue("fortitude.shield-duration-multiplier-per-lvl");
	
	public FortitudeAugment() {
		super();
		this.name = "Fortitude";
		this.etypes = Arrays.asList(new EventType[] {EventType.SHIELDS});
	}

	public FortitudeAugment(int level) {
		super(level);
		this.name = "Fortitude";
		this.etypes = Arrays.asList(new EventType[] {EventType.SHIELDS});
	}

	@Override
	public double getShieldsDurationMult(Player p) {
		return shieldDurationMult + (shieldDurationMultLvl * ((level / 5) - 1));
	}

	@Override
	public Augment createNew(int level) {
		return new FortitudeAugment(level);
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases duration of shields you");
		lore.add("§7give by §f" + formatPercentage(getShieldsGainMult(user)) + "%§7.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean canUse(Player user, AddShieldsEvent e) {
		return true;
	}
}
