package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.event.SkillBuffEvent;
import com.sucy.skill.api.util.BuffType;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModBuffAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class GuardianAugment extends Augment implements ModBuffAugment {
	private static double defenseMult = AugmentManager.getValue("guardian.defense-multiplier-base");
	private static double defenseMultLvl = AugmentManager.getValue("guardian.defense-multiplier-per-lvl");
	
	public GuardianAugment() {
		super();
		this.name = "Guardian";
		this.etypes = Arrays.asList(new EventType[] {EventType.BUFF});
	}

	public GuardianAugment(int level) {
		super(level);
		this.name = "Guardian";
		this.etypes = Arrays.asList(new EventType[] {EventType.BUFF});
	}

	@Override
	public double getBuffMult(LivingEntity user) {
		return defenseMult + (defenseMultLvl * ((level / 5) - 1));
	}

	@Override
	public Augment createNew(int level) {
		return new GuardianAugment(level);
	}

	@Override
	public boolean canUse(Player user, LivingEntity target, SkillBuffEvent e) {
		return e.getType().equals(BuffType.SKILL_DEFENSE) || e.getType().equals(BuffType.DEFENSE);
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases effectiveness of defense");
		lore.add("§7buffs by §f" + formatPercentage(getBuffMult(user)) + "%§7.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
