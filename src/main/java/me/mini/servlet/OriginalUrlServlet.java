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
import me.mini.utils.AppConstants;
import me.mini.utils.MiniMeException;
import me.mini.utils.Utils;
import me.mini.utils.XMLHelper;

/**
 * Get Original Url from given short url hash
 * 
 * @author parampreetsethi
 * 
 */
public class OriginalUrlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger
			.getLogger(OriginalUrlServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	/**
	 * Process current request
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = req.getParameter("shorturl");
		log.debug("Original Request Short url " + url);

		String errorMsg = AppConstants.GENERIC_ERROR;
		int errorCode = AppConstants.GENERIC_ERROR_CODE;
		boolean isError = false;
		UrlEntity entity = null;
		try {

			if (Utils.isStringNullOrEmpty(url)) {
				errorCode = AppConstants.MISSING_REQUIRED_PARAMETER_CODE;
				throw new MiniMeException(
						AppConstants.MISSING_REQUIRED_PARAMETER);
			}

			CassandraClient client = CassandraClient.getInstance();
			entity = client.getByShortUrlHash(getUrlHash(url));

			if (entity != null) {
				resp.getOutputStream().print(XMLHelper.convertToXML(entity));
			} else {
				isError = true;
				errorMsg = AppConstants.MISSING_URL;
				errorCode = AppConstants.MISSING_URL_CODE;
			}
		} catch (MiniMeException mex) {
			isError = true;
			errorMsg = mex.getMessage();
		} catch (Exception exp) {
			isError = true;
			exp.printStackTrace();
			errorMsg = exp.getMessage();
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

	/**
	 * Get url hash from the given url
	 * 
	 * @param url
	 * @return
	 */
	public String getUrlHash(String url) {
		return url.lastIndexOf("/") > 0
				&& url.lastIndexOf("/") + 1 < url.length() ? url.substring(url
				.lastIndexOf("/") + 1) : url;
	}
}
