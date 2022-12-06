package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashSet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.gmail.nossr50.api.PartyAPI;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;

import io.lumine.mythic.bukkit.events.MythicMobLootDropEvent;
import io.lumine.mythic.core.drops.Drop;
import io.lumine.mythic.core.drops.droppables.SkillAPIDrop;
import me.Neoblade298.NeoProfessions.Professions;

public class PartyListeners implements Listener {
	Professions main;

	public PartyListeners(Professions main) {
		this.main = main;
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onMobLoot(MythicMobLootDropEvent e) {
		if (!(e.getKiller() instanceof Player)) return;
		Player p = (Player) e.getKiller();
		
		// Check for exp drop
		SkillAPIDrop drop = null;
		for (Drop d : e.getDrops().getDrops()) {
			if (d instanceof SkillAPIDrop) {
				drop = (SkillAPIDrop) d;
				break;
			}
		}
		if (drop == null) return;
		e.getDrops().getLootTableIntangible().remove(SkillAPIDrop.class);
		
		// Get party
		HashSet<Player> getsExp = new HashSet<Player>();
		getsExp.add(p);
		if (PartyAPI.inParty(p)) {
			// Figure out how many people we need to split it with
			for (Entity ent : p.getNearbyEntities(60, 60, 60)) {
				if (ent instanceof Player) {
					Player receive = (Player) ent;
					if (PartyAPI.inSameParty(p, receive)) {
						getsExp.add(receive);
					}
				}
			}
		}
		
		// Figure out amount of exp per player
		double amount = drop.getAmount() * (0.9 + (0.1 * (getsExp.size())));
		double amtPer = (int) (amount / getsExp.size());
		for (Player recipient : getsExp) {
			SkillAPI.getPlayerData(recipient).giveExp(amtPer, ExpSource.MOB);
		}
	}
}
