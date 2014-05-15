package me.mini.bean;

import me.mini.utils.PropertyBag;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAXB bean for URL entity and cassandra column family url_mapping
 *
 * @author parampreetsethi
 */
@XmlRootElement(name = "url")
public class UrlMapping {
    
    private String urlHash;

    private String shortUrl;

    private String origUrl;

    @XmlElement(name = "urlHash")
    public String getUrlHash() {
        return urlHash;
    }

    public void setUrlHash(String urlHash) {
        this.urlHash = urlHash;
        this.setShortUrl(urlHash);
    }

    @XmlElement(name = "shortUrl")
    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = PropertyBag.getProperty("app_url") + urlHash;
    }

    @XmlElement(name = "origUrl")
    public String getOrigUrl() {
        return origUrl;
    }

    public void setOrigUrl(String origUrl) {
        this.origUrl = origUrl;
    }

}
