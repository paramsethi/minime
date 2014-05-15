package me.mini.servlet;

import me.mini.bean.SystemStatistics;
import me.mini.utils.Constants;
import me.mini.utils.StatisticsGenerator;
import me.mini.utils.XMLUtils;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Debug stats of the system
 *
 * @author parampreetsethi
 */
public class StatisticsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(StatisticsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Processing StatisticsServlet request");
        try {
            SystemStatistics entity = StatisticsGenerator.systemStats();
            resp.getOutputStream().print(XMLUtils.convertToXML(entity));
        } catch (Exception e) {
        	e.printStackTrace();
            sendErrorResponse(req, resp, e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletRequest req, HttpServletResponse resp, String errorMessage) throws ServletException, IOException {
        RequestDispatcher disp = req.getRequestDispatcher(Constants.exception);
        req.setAttribute("message", errorMessage);
        disp.forward(req, resp);
    }
}
