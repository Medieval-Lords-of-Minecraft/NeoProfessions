package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModDamageDealtAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class UnderdogAugment extends Augment implements ModDamageDealtAugment {
	private static double damageMult = AugmentManager.getValue("underdog.damage-multiplier-base");
	private static double damageMultLvl = AugmentManager.getValue("underdog.damage-multiplier-per-lvl");
	private static double maxHealth = AugmentManager.getValue("underdog.health-percent-max");
	
	public UnderdogAugment() {
		super();
		this.name = "Underdog";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
	}

	public UnderdogAugment(int level) {
		super(level);
		this.name = "Underdog";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
	}

	@Override
	public double getDamageDealtMult(LivingEntity user) {
		return damageMult + (damageMultLvl * ((level / 5) - 1));
	}

	@Override
	public Augment createNew(int level) {
		return new UnderdogAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		double percentage = user.getHealth() / user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		return percentage < maxHealth;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases damage by §f" + formatPercentage(getDamageDealtMult(user)) + "% §7when");
		lore.add("§7below " + formatPercentage(maxHealth) + "% health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
