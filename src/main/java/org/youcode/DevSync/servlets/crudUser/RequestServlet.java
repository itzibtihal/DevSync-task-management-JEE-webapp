package org.youcode.DevSync.servlets.crudUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.youcode.DevSync.dao.impl.RequestDAOImpl;
import org.youcode.DevSync.dao.impl.TokenManagerDAOImpl;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.RequestStatus;
import org.youcode.DevSync.services.RequestService;
import org.youcode.DevSync.validators.RequestValidator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RequestServlet extends HttpServlet {

    private RequestService requestService;
    private RequestValidator validator;


    @Override
    public void init() throws ServletException {
        RequestDAO requestDAO = new RequestDAOImpl();
        TokenManagerDAO tokenManagerDAO = new TokenManagerDAOImpl(); // Create TokenManagerDAO instance
        validator = new RequestValidator();
        // Pass the TokenManagerDAO to the RequestService
        requestService = new RequestService(requestDAO, tokenManagerDAO, validator);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            System.out.println("Session attributes: " + Collections.list(request.getSession().getAttributeNames()));
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated.");
            return;
        }

        if ("listAll".equals(action)) {
            listAllRequests(user, request, response);
        } else if ("listRejected".equals(action)) {
            listRejectedRequests(user, request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

    private void listAllRequests(User user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Request> requests = requestService.getAllRequestsByUser(user);
        request.setAttribute("requests", requests);
        request.getRequestDispatcher("/user/Requests/requestUser.jsp").forward(request, response);
    }

    private void listRejectedRequests(User user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Request> requests = requestService.getRequestsByUserAndStatus(user, RequestStatus.REFUSED);
        request.setAttribute("rejectedRequests", requests);
        request.getRequestDispatcher("/user/Requests/RejectedRequest.jsp").forward(request, response);
    }
}
