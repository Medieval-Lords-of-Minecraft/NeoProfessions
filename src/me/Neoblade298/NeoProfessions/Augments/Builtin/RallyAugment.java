package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.player.PlayerData;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModHealAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class RallyAugment extends Augment implements ModHealAugment {
	private static double healMult = AugmentManager.getValue("rally.heal-multiplier-base");
	private static double healMultLvl = AugmentManager.getValue("rally.heal-multiplier-per-lvl");
	private static double minHealth = AugmentManager.getValue("rally.health-percent-min");
	
	public RallyAugment() {
		super();
		this.name = "Rally";
		this.etypes = Arrays.asList(new EventType[] {EventType.HEAL});
	}

	public RallyAugment(int level) {
		super(level);
		this.name = "Rally";
		this.etypes = Arrays.asList(new EventType[] {EventType.HEAL});
	}

	@Override
	public double getHealMult(Player user) {
		return healMult + (healMultLvl * ((level / 5) - 1));
	}

	@Override
	public Augment createNew(int level) {
		return new RallyAugment(level);
	}

	@Override
	public boolean canUse(PlayerData user, LivingEntity target) {
		Player p = user.getPlayer();
		double percentage = p.getHealth() / p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		return percentage > minHealth;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Increases healing by §f" + formatPercentage(getHealMult(user)) + "% §7while");
		lore.add("§7above " + formatPercentage(minHealth) + "% health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
