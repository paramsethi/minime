package me.mini.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import me.mini.utils.PropertyBag;

/**
 * JAXB bean for URL entity
 * 
 * @author parampreetsethi
 * 
 */
@XmlRootElement(name="url")
public class UrlEntity {

	@XmlElement(name = "status")
	private String status = "success";

	private String urlHash;

	private String origUrl;

	private String shortUrl;

	/**
	 * @return the shortUrl
	 */
	@XmlElement(name = "shortUrl")
	public String getShortUrl() {
		return shortUrl;
	}

	/**
	 * @param shortUrl
	 *            the shortUrl to set
	 */
	public void setShortUrl(String urlHash) {
		this.shortUrl = PropertyBag.getProperty("app_url") + urlHash;
	}

	/**
	 * @return the urlHash
	 */
	@XmlElement(name = "urlHash")
	public String getUrlHash() {
		return urlHash;
	}

	/**
	 * @param urlHash
	 *            the urlHash to set
	 */
	public void setUrlHash(String urlHash) {
		this.urlHash = urlHash;
		this.setShortUrl(urlHash);
	}

	/**
	 * @return the origUrl
	 */
	@XmlElement(name = "origUrl")
	public String getOrigUrl() {
		return origUrl;
	}

	/**
	 * @param origUrl
	 *            the origUrl to set
	 */
	public void setOrigUrl(String origUrl) {
		this.origUrl = origUrl;
	}

}
