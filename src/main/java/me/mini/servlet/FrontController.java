package me.mini.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.mini.utils.AppConstants;
import me.mini.utils.Utils;

import org.apache.log4j.Logger;

/**
 * 
 * Front Controller Takes all the requests and forward to serving servlets
 * 
 * @author parampreetsethi
 * 
 */
public class FrontController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FrontController.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	/**
	 * Process incoming request and forward to serving servlets
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");

		if (Utils.isStringNullOrEmpty(action)) {
			action = "error";
		}

		SupportedActions actionType = SupportedActions.valueOf(action);
		String url = "error";

		String format = req.getParameter("format");

		// Check for invalid response format
		if (!Utils.isStringNullOrEmpty(format)
				&& !isValidResponseFormat(format)) {
			actionType = SupportedActions.error;
			req.setAttribute("msg", AppConstants.UNSUPPORTED_RESPONSE_FORMAT);
			req.setAttribute("code",
					AppConstants.UNSUPPORTED_RESPONSE_FORMAT_CODE);

		}

		switch (actionType) {
		case min:
			url = "shorten";
			break;
		case unmin:
			url = "original";
			break;
		case stats:
			url = "debugstats";
			break;
		case error:
		default:
			url = "exception";
			// error case
			break;
		}

		log.debug("This is new url : " + url);
		RequestDispatcher rd = req.getRequestDispatcher(url);
		rd.forward(req, resp);
	}

	public enum SupportedActions {
		min, unmin, error, stats
	}

	public enum SupportedResponseTypes {
		xml
	}

	public boolean isValidResponseFormat(String response) {
		try {
			SupportedResponseTypes.valueOf(response);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
