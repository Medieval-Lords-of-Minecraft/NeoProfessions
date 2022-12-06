package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.api.event.PlayerCriticalCheckEvent;
import com.sucy.skill.api.event.PlayerCriticalSuccessEvent;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.player.PlayerSkill;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModCritCheckAugment;
import me.Neoblade298.NeoProfessions.Augments.ModCritSuccessAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class FerociousAugment extends Augment implements ModCritCheckAugment, ModCritSuccessAugment {
	private static double critMult = AugmentManager.getValue("ferocious.crit-chance-multiplier-base");
	private static double critMultLvl = AugmentManager.getValue("ferocious.crit-chance-multiplier-per-lvl");
	private static double cdr = AugmentManager.getValue("ferocious.cooldown-reduction-base");
	private static double cdrLvl = AugmentManager.getValue("ferocious.cooldown-reduction-per-lvl");
	private double cdrFinal = cdr + (cdrLvl * ((level / 5) - 1));
	
	public FerociousAugment() {
		super();
		this.name = "Ferocious";
		this.etypes = Arrays.asList(new EventType[] {EventType.CRIT_CHECK, EventType.CRIT_SUCCESS});
	}

	public FerociousAugment(int level) {
		super(level);
		this.name = "Ferocious";
		this.etypes = Arrays.asList(new EventType[] {EventType.CRIT_CHECK, EventType.CRIT_SUCCESS});
	}
	
	@Override
	public void applyCritSuccessEffects(PlayerData user, double chance) {
        for (PlayerSkill data : user.getSkills()) {
            data.subtractCooldown(this.cdrFinal);
        }
	}

	@Override
	public double getCritChanceMult(Player user) {
		return critMult + (critMultLvl * ((level / 5) - 1));
	}

	@Override
	public Augment createNew(int level) {
		return new FerociousAugment(level);
	}

	@Override
	public boolean canUse(PlayerData user, PlayerCriticalCheckEvent e) {
		return true;
	}

	@Override
	public boolean canUse(PlayerData user, PlayerCriticalSuccessEvent e) {
		return true;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Decreases critical hit chance by §f" + formatPercentage(getCritChanceMult(user)) + "%,");
		lore.add("§7but lower active skill cooldowns by");
		lore.add("§f" + formatDouble(cdrFinal) + " §7on critical hit.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
