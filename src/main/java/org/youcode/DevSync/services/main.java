package org.youcode.DevSync.services;

import org.youcode.DevSync.dao.impl.RequestDAOImpl;
import org.youcode.DevSync.dao.impl.TokenManagerDAOImpl; // Assume this implementation exists
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.TokenType;
import org.youcode.DevSync.validators.RequestValidator;

public class main {

    public static void main(String[] args) {
//
//
//        RequestDAO requestDAO = new RequestDAOImpl(); // Assume a TokenManagerDAO implementation
//        RequestValidator validator = new RequestValidator();
//        TokenManagerDAO tokenManagerDAO = new TokenManagerDAOImpl();
//
//
//        RequestService requestService = new RequestService(requestDAO, tokenManagerDAO, validator);
//
//
//        User user = new User();
//        Task task = new Task();
//
//
//        TokenType tokenType = TokenType.DAILY;
//        task.setTokenUsed(true);
//
//        try {
//
//            Request request = requestService.createRequest(user, task, tokenType);
//            System.out.println("Request created: " + request);
//        } catch (IllegalStateException e) {
//            // Catch the exception and print the message
//            System.out.println("Exception caught: " + e.getMessage());
//        } catch (IllegalArgumentException e) {
//            // Catch any illegal argument exceptions and print the message
//            System.out.println("Exception caught: " + e.getMessage());
//        } catch (tokenUsedException e) {
//            System.out.println("Exception caught: " + e.getMessage());
//        }
    }
}
