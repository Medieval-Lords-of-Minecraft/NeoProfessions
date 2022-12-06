package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.event.PlayerCriticalCheckEvent;
import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModCritCheckAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class PrecisionAugment extends Augment implements ModCritCheckAugment {
	private static double critMult = AugmentManager.getValue("precision.crit-chance-multiplier-base");
	private static double critMultLvl = AugmentManager.getValue("precision.crit-chance-multiplier-per-lvl");
	
	public PrecisionAugment() {
		super();
		this.name = "Precision";
		this.etypes = Arrays.asList(new EventType[] {EventType.CRIT_CHECK});
	}

	public PrecisionAugment(int level) {
		super(level);
		this.name = "Precision";
		this.etypes = Arrays.asList(new EventType[] {EventType.CRIT_CHECK});
	}

	@Override
	public double getCritChanceMult(Player user) {
		return critMult + (critMultLvl * ((level / 5) - 1));
	}

	@Override
	public Augment createNew(int level) {
		return new PrecisionAugment(level);
	}

	@Override
	public boolean canUse(PlayerData user, PlayerCriticalCheckEvent e) {
		return true;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases critical hit chance by §f" + formatPercentage(getCritChanceMult(user)) + "%§7.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
