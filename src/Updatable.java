public abstract class Updatable extends Thread {
	private long updateTime;

	public Updatable(long updates) {
		this.updateTime = updates;
	}

	public long getUpdateTime() {
		return this.updateTime;
	}
}
