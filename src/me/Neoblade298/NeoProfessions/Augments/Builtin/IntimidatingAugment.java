package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModDamageDealtAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class IntimidatingAugment extends Augment implements ModDamageDealtAugment {
	private static double threatMult = AugmentManager.getValue("intimidating.threat-multiplier-base");
	private static double threatMultLvl = AugmentManager.getValue("intimidating.threat-multiplier-per-lvl");
	
	public IntimidatingAugment() {
		super();
		this.name = "Intimidating";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
	}

	public IntimidatingAugment(int level) {
		super(level);
		this.name = "Intimidating";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_DEALT});
	}
	
	private double getThreatMultiplier() {
		return threatMult * (threatMultLvl * ((level / 5) - 1));
	}

	@Override
	public void applyDamageDealtEffects(Player user, LivingEntity target, double damage) {
		MythicBukkit.inst().getAPIHelper().addThreat(target, user, threatMult);
	}

	@Override
	public Augment createNew(int level) {
		return new IntimidatingAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target) {
		return MythicBukkit.inst().getAPIHelper().isMythicMob(target);
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases threat from damaging an");
		lore.add("§7enemy by §f" + formatPercentage(getThreatMultiplier()) + "%§7.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
