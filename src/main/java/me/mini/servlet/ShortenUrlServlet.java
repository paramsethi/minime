package me.mini.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import me.mini.client.CassandraClient;
import me.mini.entity.UrlEntity;
import me.mini.strategy.RandomUrlShorteningStrategy;
import me.mini.strategy.UrlShorteningStrategy;
import me.mini.utils.AppConstants;
import me.mini.utils.MiniMeException;
import me.mini.utils.Utils;
import me.mini.utils.XMLHelper;

/**
 * Shorten the given long url.
 * 
 * @author parampreetsethi
 * 
 */
public class ShortenUrlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger log = Logger.getLogger(ShortenUrlServlet.class);

	private static int collisionCount = 0;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	};

	/**
	 * Process incoming request and shorten the given url
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String url = req.getParameter("origurl");
		boolean isError = false;
		String errorMsg = AppConstants.GENERIC_ERROR;
		int errorCode = AppConstants.GENERIC_ERROR_CODE;

		try {
			if (Utils.isStringNullOrEmpty(url)) {
				errorCode = AppConstants.MISSING_REQUIRED_PARAMETER_CODE;
				throw new MiniMeException(
						AppConstants.MISSING_REQUIRED_PARAMETER);
			}

			if (!Utils.isValidUrl(url)) {
				errorCode = AppConstants.INVALID_URL_CODE;
				throw new MiniMeException(AppConstants.INVALID_URL);
			}

			String shortUrlHash = req.getParameter("customalias");

			log.debug("Original url: " + url);

			CassandraClient client = CassandraClient.getInstance();

			UrlEntity entity = client.getByOrigUrl(url);

			if (!Utils.isStringNullOrEmpty(shortUrlHash)) {
				if (entity != null && !entity.getUrlHash().equals(shortUrlHash)) {
					errorCode = AppConstants.URL_ALREADY_EXISTS_CODE;
					throw new MiniMeException(AppConstants.URL_ALREADY_EXISTS);
				} else {
					if (client.getByShortUrlHash(shortUrlHash) != null) {
						errorCode = AppConstants.EXISTING_CUSTOM_ALIAS_CODE;

						throw new MiniMeException(
								AppConstants.EXISTING_CUSTOM_ALIAS);
					}
				}
			}

			if (entity == null) {

				entity = new UrlEntity();
				entity.setOrigUrl(url);
				if (Utils.isStringNullOrEmpty(shortUrlHash)) {

					// Generate Unique Hash
					UrlShorteningStrategy strategy = new RandomUrlShorteningStrategy();
					shortUrlHash = strategy.getUrlHash();

					// Check if hash collision, and it already exists in the
					// database
					while (client.getByShortUrlHash(shortUrlHash) != null) {
						collisionCount++;
						shortUrlHash = strategy.getUrlHash();
					}
				}
				entity.setUrlHash(shortUrlHash);
				client.writeQueryExecute(entity);
			}

			log.debug("Total Collision count " + collisionCount);

			resp.getOutputStream().print(XMLHelper.convertToXML(entity));

		} catch (MiniMeException mex) {
			isError = true;
			errorMsg = mex.getMessage();
		} catch (Exception ex) {
			isError = true;
			errorMsg = ex.getMessage();
			ex.printStackTrace();
		}

		if (isError) {
			errorMsg = errorMsg == null ? AppConstants.GENERIC_ERROR : errorMsg;
			RequestDispatcher disp = req
					.getRequestDispatcher(AppConstants.ERROR_URL);
			req.setAttribute("msg", errorMsg);
			req.setAttribute("code", errorCode);
			disp.forward(req, resp);
		}

	}
}
