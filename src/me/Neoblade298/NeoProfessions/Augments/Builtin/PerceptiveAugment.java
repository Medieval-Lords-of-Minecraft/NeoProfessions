package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.enums.ManaSource;
import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModManaGainAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class PerceptiveAugment extends Augment implements ModManaGainAugment {
	private double manaMult = AugmentManager.getValue("perceptive.mana-regen-multiplier-base");
	private double manaMultLvl = AugmentManager.getValue("perceptive.mana-regen-multiplier-per-lvl");
	private double minMana = AugmentManager.getValue("perceptive.mana-percent-min");
	
	public PerceptiveAugment() {
		super();
		this.name = "Perceptive";
		this.etypes = Arrays.asList(new EventType[] {EventType.MANA_GAIN});
	}

	public PerceptiveAugment(int level) {
		super(level);
		this.name = "Perceptive";
		this.etypes = Arrays.asList(new EventType[] {EventType.MANA_GAIN});
	}

	@Override
	public double getManaGainMult(Player user) {
		return manaMult + (manaMultLvl * ((level / 5) - 1));
	}

	@Override
	public Augment createNew(int level) {
		return new PerceptiveAugment(level);
	}

	@Override
	public boolean canUse(PlayerData user, ManaSource src) {
		return ((user.getMana() / user.getMaxMana()) > minMana) && 
				!user.getClass("class").getData().getManaName().endsWith("MP");
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases resource regen by §f" + formatPercentage(getManaGainMult(user)) + "% §7when");
		lore.add("§7above " + formatPercentage(minMana) + "% resource.");
		lore.add("§cDoesn't work with mana.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
