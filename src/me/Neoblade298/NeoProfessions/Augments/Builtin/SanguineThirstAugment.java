package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.util.FlagManager;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModDamageDealtAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class SanguineThirstAugment extends Augment implements ModDamageDealtAugment {
	private static int cooldownSeconds = (int) AugmentManager.getValue("sanguinethirst.cooldown-seconds");
	private static double healing = AugmentManager.getValue("sanguinethirst.heal-percent");
	
	public SanguineThirstAugment() {
		super();
		this.name = "Sanguine Thirst";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
	}

	public SanguineThirstAugment(int level) {
		super(level);
		this.name = "Sanguine Thirst";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
	}
	
	@Override
	public boolean isPermanent() {
		return true;
	}

	@Override
	public String getLine() {
		return "§7[§4§o" + name + " Lv " + level + "§7]";
	}

	@Override
	public Augment createNew(int level) {
		return new SanguineThirstAugment(level);
	}
	
	public void applyDamageDealtEffects(Player user, LivingEntity target, double damage) {
		Player p = user.getPlayer();
		double max = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		p.setHealth(Math.min(max, p.getHealth() + (max * healing)));
		FlagManager.addFlag(user, user, "aug_sanguineThirst", cooldownSeconds * 20);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		return !FlagManager.hasFlag(user, "aug_sanguineThirst");
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Dealing damage heals for §f" + formatPercentage(healing) + "% §7your");
		lore.add("§7max health. " + cooldownSeconds + "s cooldown.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
