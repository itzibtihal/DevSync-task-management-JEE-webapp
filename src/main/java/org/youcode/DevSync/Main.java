package org.youcode.DevSync;

import org.youcode.DevSync.dao.impl.UserDAOImpl;
import org.youcode.DevSync.modals.User;

import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        UserDAOImpl userDAO = new UserDAOImpl();

        // Replace with a valid UUID of a user you want to delete
        UUID userId = UUID.fromString("9b58d2ce-fde7-4eff-bc4c-3ac0b05ef2b4");

        boolean result = userDAO.delete(userId);

        if (result) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("Failed to delete user.");
        }
    }
}