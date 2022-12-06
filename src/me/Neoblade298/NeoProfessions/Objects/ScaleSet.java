package me.Neoblade298.NeoProfessions.Objects;

public class ScaleSet {
	private int base;
	private int scale;
	
	public ScaleSet(int base, int scale) {
		this.base = base;
		this.scale = scale;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public int getResult(int level) {
		return base + (scale * (level - 1));
	}
}
