package me.mini.servlet;

import me.mini.bean.ErrorDictionary;
import me.mini.utils.Constants;
import me.mini.utils.MinimeException;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Front Controller Takes all the requests and forward to serving servlets
 *
 * @author parampreetsethi
 */
public class FrontController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter(Constants.action);
            SupportedOperations actionType = SupportedOperations.valueOf(action);
            if (actionType == null) {
                throw new MinimeException(ErrorDictionary.UNSUPPORTED_OPERATION_ERROR);
            }

            String format = req.getParameter("format");
            SupportedResponseFormat responseFormat = SupportedResponseFormat.valueOf(format);
            if (responseFormat == null) {
                throw new MinimeException(ErrorDictionary.UNSUPPORTED_RESPONSE_FORMAT_ERROR);
            }

            String url;
            switch (actionType) {
                case min:
                    url = Constants.shorten;
                    break;
                case unmin:
                    url = Constants.original;
                    break;
                case stats:
                    url = Constants.debugstats;
                    break;
                case error:
                default:
                    url = Constants.exception;
                    // error case
                    break;
            }
            RequestDispatcher rd = req.getRequestDispatcher(url);
            rd.forward(req, resp);
        } catch (Exception e) {
            sendErrorResponse(req, resp, e.getMessage());
        }
    }


    private void sendErrorResponse(HttpServletRequest req, HttpServletResponse resp, String errorMessage) throws ServletException, IOException {
        RequestDispatcher disp = req.getRequestDispatcher(Constants.exception);
        req.setAttribute("message", errorMessage);
        disp.forward(req, resp);
    }


    public boolean isValidResponseFormat(String response) {
        try {
            SupportedResponseFormat.valueOf(response);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public enum SupportedOperations {
        min, unmin, error, stats
    }

    public enum SupportedResponseFormat {
        xml
    }
}
