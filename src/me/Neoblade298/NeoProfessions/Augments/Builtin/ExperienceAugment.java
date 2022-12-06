package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.event.PlayerExperienceGainEvent;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModExpAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class ExperienceAugment extends Augment implements ModExpAugment {
	private static double expMult = AugmentManager.getValue("experience.experience-multiplier-base");
	private static double expMultLvl = AugmentManager.getValue("experience.experience-multiplier-per-lvl");
	
	public ExperienceAugment() {
		super();
		this.name = "Experience";
		this.etypes = Arrays.asList(new EventType[] {EventType.SKILLAPI_EXP});
	}

	public ExperienceAugment(int level) {
		super(level);
		this.name = "Experience";
		this.etypes = Arrays.asList(new EventType[] {EventType.SKILLAPI_EXP});
	}

	@Override
	public Augment createNew(int level) {
		return new ExperienceAugment(level);
	}
	
	@Override
	public double getExpMult(Player user) {
		return expMult + (expMultLvl * ((level / 5) - 1));
	}

	@Override
	public boolean canUse(Player user, PlayerExperienceGainEvent e) {
		return e.getSource().equals(ExpSource.MOB);
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases exp gained from");
		lore.add("§7mobs and minibosses by §f" + formatPercentage(getExpMult(user)) + "%§7.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
