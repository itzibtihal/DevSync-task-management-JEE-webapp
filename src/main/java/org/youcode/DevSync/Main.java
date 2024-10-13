package org.youcode.DevSync;

import org.youcode.DevSync.dao.impl.RequestDAOImpl;
import org.youcode.DevSync.dao.impl.TaskDAOImpl;
import org.youcode.DevSync.dao.impl.UserDAOImpl;
import org.youcode.DevSync.dao.impl.TagDAOImpl;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.entities.Tag;
import org.youcode.DevSync.domain.enums.TokenType;
import org.youcode.DevSync.services.RequestService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
//        // Initialize DAOs
//        TaskDAOImpl taskDAO = new TaskDAOImpl();
//        UserDAOImpl userDAO = new UserDAOImpl(); // Replace with your actual user DAO
//        TagDAOImpl tagDAO = new TagDAOImpl(); // Replace with your actual tag DAO
//
//        // Define user and tag UUIDs (replace with actual IDs from your database)
//        UUID userId = UUID.fromString("c2fa970c-bbab-4113-ad63-85ee1995e3e2"); // Asmin
//        UUID userId1 = UUID.fromString("7c20372f-25c2-4ff9-97ce-a475cbcd3f50"); // USER
//        UUID tagId = UUID.fromString("a9fe501e-28b2-45e9-8424-0d13044e53db"); // Some tag ID
//
//        // Fetch user and tag from the database
//        User user = userDAO.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        User user2 = userDAO.findById(userId1).orElseThrow(() -> new RuntimeException("User not found"));
//        Tag tag = tagDAO.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found"));
//
//        // Create a new task
//        Task task = new Task("Finish DevSync implementation", "Work on the task DAO implementation and test the save method.",LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5),user,user2);
//
//        // Assign the tag to the task
//        task.getTags().add(tag);
//
//        // Save the task to the database
//        taskDAO.save(task);
//
//        System.out.println("Task added successfully to user with ID: " + userId);





//        RequestDAOImpl requestDAO = new RequestDAOImpl();
//        UserDAOImpl userDAO = new UserDAOImpl(); // DAO for User
//        TaskDAOImpl taskDAO = new TaskDAOImpl(); // DAO for Task
//
//        // Fetch existing User and Task by ID
//        Optional<User> userOpt = userDAO.findById(UUID.fromString("c23ab367-733e-4744-a769-f53cb2d1989f"));
//        Optional<Task> taskOpt = taskDAO.findById(UUID.fromString("d8d6ae95-495f-4ef8-b517-54e3d27ec60e"));
//
//        if (userOpt.isPresent() && taskOpt.isPresent()) {
//            User user = userOpt.get();
//            Task task = taskOpt.get();
//
//            // Create a new Request instance
//            Request request = new Request(user, task, TokenType.DAILY);
//
//            // Insert the new Request into the database
//            requestDAO.save(request);
//
//            // Confirm the insert
//            System.out.println("Request inserted successfully.");
//        } else {
//            System.out.println("User or Task not found.");
//        }


        RequestDAOImpl requestDAO = new RequestDAOImpl();
        UserDAOImpl userDAO = new UserDAOImpl(); // DAO for User
        TaskDAOImpl taskDAO = new TaskDAOImpl(); // DAO for Task

        // Initialize the RequestService with the RequestDAO
        RequestService requestService = new RequestService(requestDAO);

        // Fetch existing User and Task by ID
        Optional<User> userOpt = userDAO.findById(UUID.fromString("c23ab367-733e-4744-a769-f53cb2d1989f"));
        Optional<Task> taskOpt = taskDAO.findById(UUID.fromString("d8d6ae95-495f-4ef8-b517-54e3d27ec60e"));

        if (userOpt.isPresent() && taskOpt.isPresent()) {
            User user = userOpt.get();
            Task task = taskOpt.get();

            try {
                // Create a new Request instance using the service
                Request request = requestService.createRequest(user, task, TokenType.DAILY);

                // Confirm the insert
                System.out.println("Request inserted successfully with ID: " + request.getId());

                // Example: Count requests by user and date
               // int count = requestService.countUsedTokensToday(user, TokenType.DAILY); // Assuming this is a public method in RequestService
               // System.out.println("Number of requests today: " + count);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error while creating request: " + e.getMessage());
            }
        } else {
            System.out.println("User or Task not found.");
        }

    }
}
