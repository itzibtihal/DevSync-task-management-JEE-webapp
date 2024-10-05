package org.youcode.DevSync;

import org.youcode.DevSync.dao.impl.TaskDAOImpl;
import org.youcode.DevSync.dao.impl.UserDAOImpl;
import org.youcode.DevSync.dao.impl.TagDAOImpl;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.entities.Tag;

import java.time.LocalDateTime;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // Initialize DAOs
        TaskDAOImpl taskDAO = new TaskDAOImpl();
        UserDAOImpl userDAO = new UserDAOImpl(); // Replace with your actual user DAO
        TagDAOImpl tagDAO = new TagDAOImpl(); // Replace with your actual tag DAO

        // Define user and tag UUIDs (replace with actual IDs from your database)
        UUID userId = UUID.fromString("c2fa970c-bbab-4113-ad63-85ee1995e3e2"); // Asmin
        UUID userId1 = UUID.fromString("7c20372f-25c2-4ff9-97ce-a475cbcd3f50"); // USER
        UUID tagId = UUID.fromString("a9fe501e-28b2-45e9-8424-0d13044e53db"); // Some tag ID

        // Fetch user and tag from the database
        User user = userDAO.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userDAO.findById(userId1).orElseThrow(() -> new RuntimeException("User not found"));
        Tag tag = tagDAO.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found"));

        // Create a new task
        Task task = new Task("Finish DevSync implementation", "Work on the task DAO implementation and test the save method.", LocalDateTime.now().plusDays(5), user,user2);

        // Assign the tag to the task
        task.getTags().add(tag);

        // Save the task to the database
        taskDAO.save(task);

        System.out.println("Task added successfully to user with ID: " + userId);
    }
}
