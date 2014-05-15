package me.mini.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Stats Entity for the system
 *
 * @author parampreetsethi
 */

@XmlRootElement(name = "stats")
public class SystemStatistics {
    
    private long totalUrls;

    private long timeTakenInMillis;

    private long totalMemory;

    private long freeMemory;

    @XmlElement
    public long getTotalUrls() {
        return totalUrls;
    }

    public void setTotalUrls(long totalUrls) {
        this.totalUrls = totalUrls;
    }

    @XmlElement
    public long getTimeTakenInMillis() {
        return timeTakenInMillis;
    }

    public void setTimeTakenInMillis(long timeTakenInMillis) {
        this.timeTakenInMillis = timeTakenInMillis;
    }

    @XmlElement
    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    @XmlElement
    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }
}
