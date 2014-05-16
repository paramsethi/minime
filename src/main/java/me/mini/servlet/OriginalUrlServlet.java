package me.mini.servlet;

import me.mini.bean.ErrorDictionary;
import me.mini.bean.UrlMapping;
import me.mini.cassandra.query.CassandraUrlQueryUtil;
import me.mini.utils.Constants;
import me.mini.utils.GlobalUtils;
import me.mini.utils.MinimeException;
import me.mini.utils.XMLUtils;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Get Original Url from given short url hash
 *
 * @author parampreetsethi
 */
public class OriginalUrlServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(OriginalUrlServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    /**
     * Process incoming request and return the short url for the given url
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter(Constants.SHORTURL);
        log.info(String.format("Processing OriginalUrlServlet request for shorturl: %s", url));
        try {
            validateQueryUrl(url);
            String hashToken = GlobalUtils.extractHashFromUrl(url);
            UrlMapping urlMapping = CassandraUrlQueryUtil.getInstance().queryByUrlHash(hashToken);
            if (urlMapping == null) {
                throw new MinimeException(ErrorDictionary.MISSING_URL_ERROR);
            }
            resp.getOutputStream().print(XMLUtils.convertToXML(urlMapping));
		} catch (MinimeException mex) {
			sendErrorResponse(req, resp, mex.getMessage());
		} catch (Exception e) {
        	e.printStackTrace();
            sendErrorResponse(req, resp, e.getMessage());
        }
        log.info(String.format("Done processing OriginalUrlServlet request for shorturl: %s", url));

    }

    private void sendErrorResponse(HttpServletRequest req, HttpServletResponse resp, String errorMessage) throws ServletException, IOException {
        RequestDispatcher disp = req.getRequestDispatcher(Constants.EXCEPTION);
        req.setAttribute("message", errorMessage);
        disp.forward(req, resp);
    }

    private void validateQueryUrl(String url) throws MinimeException {
        if (GlobalUtils.isStringNullOrEmpty(url)) {
            throw new MinimeException(ErrorDictionary.MISSING_REQUIRED_PARAMETER_ERROR);
        }
    }

}
