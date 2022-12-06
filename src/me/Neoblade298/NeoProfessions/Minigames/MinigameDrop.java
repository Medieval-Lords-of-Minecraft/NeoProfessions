package me.Neoblade298.NeoProfessions.Minigames;

import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;

public class MinigameDrop {
	private StoredItemInstance item;
	private int exp;
	public MinigameDrop(StoredItemInstance item, int exp) {
		super();
		this.item = item;
		this.exp = exp;
	}
	public StoredItemInstance getItem() {
		return item;
	}
	public int getExp() {
		return exp;
	}
	
	
}
