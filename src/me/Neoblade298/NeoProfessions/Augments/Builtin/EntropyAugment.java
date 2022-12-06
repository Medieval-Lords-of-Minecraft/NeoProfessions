package me.Neoblade298.NeoProfessions.Augments.Builtin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.sucy.skill.api.event.PlayerCalculateDamageEvent;

import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.EventType;
import me.Neoblade298.NeoProfessions.Augments.ModDamageTakenAugment;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;

public class EntropyAugment extends Augment implements ModDamageTakenAugment {
	private HashMap<UUID, BukkitTask> regenTasks = new HashMap<UUID, BukkitTask>();
	private HashSet<UUID> toRefresh = new HashSet<UUID>();
	private static double healthRegen = AugmentManager.getValue("entropy.health-percent");
	
	public EntropyAugment() {
		super();
		this.name = "Entropy";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_TAKEN});
	}

	public EntropyAugment(int level) {
		super(level);
		this.name = "Entropy";
		this.etypes = Arrays.asList(new EventType[] {EventType.DAMAGE_TAKEN});
	}

	@Override
	public void applyDamageTakenEffects(Player user, LivingEntity target, PlayerCalculateDamageEvent e) {
		UUID uuid = user.getUniqueId();
		if (regenTasks.containsKey(uuid)) {
			toRefresh.add(uuid);
		}
		else {
			regenTasks.put(uuid, new RefreshableRunnable(user).runTaskTimer(AugmentManager.getMain(), 0, 20));
		}
	}

	@Override
	public Augment createNew(int level) {
		return new EntropyAugment(level);
	}
	
	@Override
	public boolean isPermanent() {
		return true;
	}

	@Override
	public String getLine() {
		return "§7[§a§o" + name + " Lv " + level + "§7]";
	}

	@Override
	public boolean canUse(Player user, LivingEntity target, PlayerCalculateDamageEvent e) {
		return true;
	}

	public ItemStack getItem(Player user) {
		ItemStack item = super.getItem(user);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add("§7Taking damage heals you for");
		lore.add("§f" + formatPercentage(healthRegen * 3) + "% §7max health over 3s");
		lore.add("§7This does not stack, but it");
		lore.add("§7does refresh on repeated hits.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	private class RefreshableRunnable extends BukkitRunnable {
		Player user;
		public int count;
		
		public RefreshableRunnable(Player user) {
			this.user = user;
			this.count = 3;
		}
		
		public void run() {
			if (user == null) {
				this.cancel();
				regenTasks.remove(user.getUniqueId());
				toRefresh.remove(user.getUniqueId());
				return;
			}
			
			if (toRefresh.contains(user.getUniqueId())) {
				count = 3;
				toRefresh.remove(user.getUniqueId());
			}
			
			if (count-- > 0 && user != null && user.isValid()) {
				double max = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
				user.setHealth(Math.min(max, user.getHealth() + (max * healthRegen)));
			}
			else {
				this.cancel();
				regenTasks.remove(user.getUniqueId());
			}
		}
	}

}
