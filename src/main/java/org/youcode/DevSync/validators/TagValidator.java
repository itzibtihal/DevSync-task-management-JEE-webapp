package org.youcode.DevSync.validators;


import org.youcode.DevSync.domain.entities.Tag;
import org.youcode.DevSync.domain.exceptions.InvalidTagException;


public class TagValidator {

    public void validateTag(Tag tag) {
        if (tag == null) {
            throw new InvalidTagException("Tag cannot be null.");
        }


        if (tag.getName() == null || tag.getName().trim().isEmpty()) {
            throw new InvalidTagException("Tag name cannot be null or empty.");
        }

        if (tag.getName().length() > 50) {
            throw new InvalidTagException("Tag name cannot exceed 50 characters.");
        }

//        if (!tag.getName().matches("^[a-zA-Z0-9 ]+$")) {
//            throw new InvalidTagException("Tag name can only contain alphanumeric characters and spaces.");
//        }


    }

    public void validateTagForUpdate(Tag tag) {
        validateTag(tag);
        if (tag.getId() == null) {
            throw new InvalidTagException("Tag ID is required for update.");
        }
    }
}
