package me.mini.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.mini.entity.ErrorEntity;
import me.mini.utils.AppConstants;
import me.mini.utils.MiniMeException;
import me.mini.utils.Utils;
import me.mini.utils.XMLHelper;

/**
 * Error handler
 * 
 * @author parampreetsethi
 * 
 */
public class ErrorHandler extends HttpServlet {

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

	public void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String msg = req.getParameter("msg");
		int errorCode = AppConstants.GENERIC_ERROR_CODE;
		if (req.getAttribute("code") != null) {
			errorCode = (Integer) req.getAttribute("code");
		}
		if (Utils.isStringNullOrEmpty(msg)) {
			msg = (String) req.getAttribute("msg");
		}
		try {
			ErrorEntity entity = new ErrorEntity(msg, errorCode);
			resp.getOutputStream().print(XMLHelper.convertToXML(entity));
		} catch (MiniMeException mex) {
			mex.printStackTrace();
			resp.getOutputStream().print(AppConstants.GENERIC_ERROR);
		}
	}
}
