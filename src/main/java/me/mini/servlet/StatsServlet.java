package me.mini.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.mini.entity.StatsEntity;
import me.mini.strategy.RandomUrlShorteningStrategy;
import me.mini.utils.AppConstants;
import me.mini.utils.MiniMeException;
import me.mini.utils.XMLHelper;

/**
 * Debug stats of the system
 * 
 * @author parampreetsethi
 * 
 */
public class StatsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

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
	 * Process incoming request
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		RandomUrlShorteningStrategy strategy = new RandomUrlShorteningStrategy();
		boolean isError = false;
		String errorMsg = null;
		int errorCode = -1;
		try {
			StatsEntity entity = strategy.systemStats();

			resp.getOutputStream().print(XMLHelper.convertToXML(entity));
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
}
