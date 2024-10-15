package org.youcode.DevSync.services;

import org.youcode.DevSync.dao.interfaces.TagDAO;
import org.youcode.DevSync.domain.entities.Tag;
import org.youcode.DevSync.domain.exceptions.InvalidTagException;
import org.youcode.DevSync.validators.TagValidator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TagService {

    private final TagDAO tagDAO;
    private final TagValidator tagValidator;

    public TagService(TagDAO tagDAO, TagValidator tagValidator) {
        this.tagDAO = tagDAO;
        this.tagValidator = tagValidator;
    }


    public Tag saveTag(Tag tag) {
        tagValidator.validateTag(tag);
        return tagDAO.save(tag);
    }


    public Optional<Tag> findTagById(UUID id) {
        if (id == null) {
            throw new InvalidTagException("Tag ID cannot be null.");
        }
        return tagDAO.findById(id);
    }


    public List<Tag> findAllTags() {
        return tagDAO.findAll();
    }


    public Tag updateTag(Tag tag) {

        tagValidator.validateTagForUpdate(tag);

        if (tagDAO.findById(tag.getId()).isEmpty()) {
            throw new InvalidTagException("Cannot update. Tag with ID " + tag.getId() + " not found.");
        }
        return tagDAO.save(tag);
    }


    public boolean deleteTag(UUID id) {
        if (id == null) {
            throw new InvalidTagException("Tag ID cannot be null.");
        }
        return tagDAO.delete(id);
    }



}
