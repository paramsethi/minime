package me.mini.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Stats Entity for the system
 * 
 * @author parampreetsethi
 *
 */

@XmlRootElement(name = "stats")
public class StatsEntity {

	private long totalUrls;
	private long testUrlRan;
	private long timeTakenInMS;
	private long totalMemory;
	private long freeMemory;

	/**
	 * @return the totalUrls
	 */
	@XmlElement(name = "totalNumberOfUrlSupported")
	public long getTotalUrls() {
		return totalUrls;
	}

	/**
	 * @param totalUrls
	 *            the totalUrls to set
	 */
	public void setTotalUrls(long totalUrls) {
		this.totalUrls = totalUrls;
	}

	/**
	 * @return the testUrlRan
	 */
	@XmlElement(name = "testRanforUrlNum")
	public long getTestUrlRan() {
		return testUrlRan;
	}

	/**
	 * @param testUrlRan
	 *            the testUrlRan to set
	 */
	public void setTestUrlRan(long testUrlRan) {
		this.testUrlRan = testUrlRan;
	}

	/**
	 * @return the timeTakenInMS
	 */
	@XmlElement
	public long getTimeTakenInMS() {
		return timeTakenInMS;
	}

	/**
	 * @param timeTakenInMS
	 *            the timeTakenInMS to set
	 */
	public void setTimeTakenInMS(long timeTakenInMS) {
		this.timeTakenInMS = timeTakenInMS;
	}

	/**
	 * @return the totalMemory
	 */
	@XmlElement
	public long getTotalMemory() {
		return totalMemory;
	}

	/**
	 * @param totalMemory
	 *            the totalMemory to set
	 */
	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	/**
	 * @return the freeMemory
	 */
	@XmlElement
	public long getFreeMemory() {
		return freeMemory;
	}

	/**
	 * @param freeMemory
	 *            the freeMemory to set
	 */
	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

}
