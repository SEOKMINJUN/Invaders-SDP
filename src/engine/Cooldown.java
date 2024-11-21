package engine;

/**
 * Imposes a cooldown period between two actions.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Cooldown {

	/** Cooldown duration. */
	private int milliseconds;
	/** Maximum difference between durations. */
	private int variance;
	/** Duration of this run, varies between runs if variance > 0. */
	private int duration;
	/** Beginning time. */
	private long time;

	/**
	 * Constructor, established the time until the action can be performed
	 * again.
	 * 
	 * @param milliseconds
	 *            Time until cooldown period is finished.
	 */
	public Cooldown(final int milliseconds) {
		this.milliseconds = milliseconds;
		this.variance = 0;
		this.duration = milliseconds;
		this.time = 0;
	}

	/**
	 * Constructor, established the time until the action can be performed
	 * again, with a variation of +/- variance.
	 * 
	 * @param milliseconds
	 *            Time until cooldown period is finished.
	 * @param variance
	 *            Variance in the cooldown period.
	 */
	protected Cooldown(final int milliseconds, final int variance) {
		this.milliseconds = milliseconds;
		this.variance = variance;
		this.time = 0;
	}

	public int getRemainingTime() {
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - this.time;

		if (elapsedTime >= this.duration) { return 0; }
		return (int) (this.duration - elapsedTime);
	}

	public int getTotalDuration() { return this.duration; }

	/**
	 * Checks if the cooldown is finished.
	 * 
	 * @return Cooldown state.
	 */
	public final boolean checkFinished() {
		if ((this.time == 0)
				|| this.time + this.duration < System.currentTimeMillis())
			return true;
		return false;
	}

	/**
	 * Restarts the cooldown.
	 */
	public final void reset() {
		this.time = System.currentTimeMillis();
		if (this.variance != 0)
			this.duration = (this.milliseconds - this.variance)
					+ (int) (Math.random()
							* (this.milliseconds + this.variance));
	}
}
