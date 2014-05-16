package me.mini.servlet;

import me.mini.bean.ErrorDictionary;
import me.mini.utils.MinimeException;
import me.mini.utils.XMLUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Error handler
 *
 * @author parampreetsethi
 */
public class ErrorHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // error message
        String message = (String) req.getAttribute("message");
        message = message == null ? ErrorDictionary.GENERIC_ERROR.getErrorMessage() : message;
        // error code
        Integer errorCode = (Integer) req.getAttribute("code");
        errorCode = errorCode == null ? ErrorDictionary.GENERIC_ERROR.getErrorCode() : errorCode;
        try {
            ErrorDictionary entity = new ErrorDictionary(errorCode, message);
            resp.getOutputStream().print(XMLUtils.convertToXML(entity));
        } catch (MinimeException mex) {
            mex.printStackTrace();
            resp.getOutputStream().print(mex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			resp.getOutputStream().print(ex.getMessage());
		}
    }
}
