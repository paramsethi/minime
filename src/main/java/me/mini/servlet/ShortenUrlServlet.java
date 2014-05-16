package me.mini.servlet;

import me.mini.algo.hashgenerator.ShortUrlGenerator;
import me.mini.algo.strategy.BaseUrlShorteningStrategy;
import me.mini.algo.strategy.RandomUrlShorteningStrategy;
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
 * Shorten the given long url.
 *
 * @author parampreetsethi
 */
public class ShortenUrlServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(ShortenUrlServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    /**
     * Process incoming request and shorten the given url
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter(Constants.ORIGURL);
        log.info(String.format("Processing ShortenUrlServlet request for origurl: %s", url));
        try {
            validateQueryUrl(url);
            CassandraUrlQueryUtil cassandraQueryClient = CassandraUrlQueryUtil.getInstance();
            UrlMapping urlMapping = cassandraQueryClient.queryByOrigUrl(url);
			if (urlMapping == null) {
				urlMapping = new UrlMapping();
				urlMapping.setOrigUrl(url);
				urlMapping.setUrlHash(generateUrlHash());
				cassandraQueryClient.writeQuery(urlMapping);
			}
            resp.getOutputStream().print(XMLUtils.convertToXML(urlMapping));
		} catch (MinimeException mex) {
			sendErrorResponse(req, resp, mex.getMessage());
		} catch (Exception ex) {
        	ex.printStackTrace();
            sendErrorResponse(req, resp, ex.getMessage());
        }
        log.info(String.format("Done processing ShortenUrlServlet request for origurl: %s", url));
    }

    /**
     * Generates a unique (and collision free) has for the short url - based on the {@link me.mini.algo.strategy.BaseUrlShorteningStrategy} implementation
     *
     * @return
     * @throws MinimeException
     */
    private String generateUrlHash() throws MinimeException {
        BaseUrlShorteningStrategy strategy = new RandomUrlShorteningStrategy();
        ShortUrlGenerator urlGenerator = new ShortUrlGenerator(strategy);
        String shortUrlHash = urlGenerator.generateShortUrlHash();
        return shortUrlHash;
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
        if (!GlobalUtils.isValidUrl(url)) {
            throw new MinimeException(ErrorDictionary.INVALID_URL_ERROR);
        }
    }

}
