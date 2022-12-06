package me.Neoblade298.NeoProfessions.Objects;

public class StoredItemSource {
	private String source;
	private boolean isMob;
	public StoredItemSource(String source, boolean isMob) {
		this.source = source;
		this.isMob = isMob;
	}
	public boolean isMob() {
		return isMob;
	}
	public String getSource() {
		return source;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof StoredItemSource) {
			StoredItemSource comp = (StoredItemSource) o;
			return comp.source.equals(this.source) && this.isMob == comp.isMob();
		}
		return false;
	}
}
