package me.mini.bean;

import me.mini.utils.PropertyBag;

import javax.xml.bind.annotation.XmlElement;

/**
 * JAXB bean for URL entity and cassandra column family url_mapping
 *
 * @author parampreetsethi
 */
public class UrlMapping {

    @XmlElement(name = "urlHash")
    private String urlHash;

    @XmlElement(name = "shortUrl")
    private String shortUrl;

    @XmlElement(name = "origUrl")
    private String origUrl;

    public String getUrlHash() {
        return urlHash;
    }

    public void setUrlHash(String urlHash) {
        this.urlHash = urlHash;
        this.setShortUrl(urlHash);
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = PropertyBag.getProperty("app_url") + urlHash;
    }

    public String getOrigUrl() {
        return origUrl;
    }

    public void setOrigUrl(String origUrl) {
        this.origUrl = origUrl;
    }

}
