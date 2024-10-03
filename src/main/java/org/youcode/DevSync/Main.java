package org.youcode.DevSync;

import org.youcode.DevSync.dao.impl.TagDAOImpl;
import org.youcode.DevSync.domain.entities.Tag;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        TagDAOImpl tagDAO = new TagDAOImpl();

        // Create a new Tag
        Tag newTag = new Tag();
        newTag.setName("exampleTag");

        // Save the Tag
        Tag savedTag = tagDAO.save(newTag);

        // Print the saved Tag
        System.out.println("Saved Tag: " + savedTag);
    }
}