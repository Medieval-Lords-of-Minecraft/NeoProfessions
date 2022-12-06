package me.Neoblade298.NeoProfessions.Objects;

public class FlagSettings {
	private String flag;
	private int duration;
	public FlagSettings(String flag, int duration) {
		super();
		this.flag = flag;
		this.duration = duration;
	}
	public String getFlag() {
		return flag;
	}
	public int getDuration() {
		return duration;
	}
}
