package org.youcode.DevSync.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.youcode.DevSync.dao.interfaces.TagDAO;
import org.youcode.DevSync.domain.entities.Tag;
import org.youcode.DevSync.domain.exceptions.InvalidTagException;
import org.youcode.DevSync.validators.TagValidator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagServiceTest {

    @Mock
    private TagDAO tagDAO;

    @Mock
    private TagValidator tagValidator;

    @InjectMocks
    private TagService tagService;

    private Tag tag;
    private UUID tagId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tagId = UUID.randomUUID();
        tag = new Tag(tagId, "Sample Tag");
    }

    @Test
    void saveTag_validTag() {
        when(tagDAO.save(any(Tag.class))).thenReturn(tag);
        tagService.saveTag(tag);
        verify(tagValidator).validateTag(tag);
        verify(tagDAO).save(tag);
    }

    @Test
    void saveTag_invalidTag() {
        doThrow(new InvalidTagException("Invalid tag")).when(tagValidator).validateTag(any(Tag.class));
        assertThatThrownBy(() -> tagService.saveTag(tag))
                .isInstanceOf(InvalidTagException.class)
                .hasMessage("Invalid tag");
    }

    @Test
    void findTagById_validId() {
        when(tagDAO.findById(tagId)).thenReturn(Optional.of(tag));
        Optional<Tag> foundTag = tagService.findTagById(tagId);
        assertThat(foundTag).isPresent().contains(tag);
    }

    @Test
    void findTagById_invalidId() {
        assertThatThrownBy(() -> tagService.findTagById(null))
                .isInstanceOf(InvalidTagException.class)
                .hasMessage("Tag ID cannot be null.");
    }

    @Test
    void findAllTags() {
        when(tagDAO.findAll()).thenReturn(List.of(tag));
        List<Tag> tags = tagService.findAllTags();
        assertThat(tags).contains(tag);
    }

    @Test
    void updateTag_validTag() {
        when(tagDAO.findById(tagId)).thenReturn(Optional.of(tag));
        when(tagDAO.save(any(Tag.class))).thenReturn(tag);

        tagService.updateTag(tag);

        verify(tagValidator).validateTagForUpdate(tag);
        verify(tagDAO).save(tag);
    }

    @Test
    void updateTag_tagNotFound() {
        when(tagDAO.findById(tagId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> tagService.updateTag(tag))
                .isInstanceOf(InvalidTagException.class)
                .hasMessage("Cannot update. Tag with ID " + tagId + " not found.");
    }

    @Test
    void deleteTag_validId() {
        when(tagDAO.delete(tagId)).thenReturn(true);
        boolean result = tagService.deleteTag(tagId);
        assertThat(result).isTrue();
    }

    @Test
    void deleteTag_nullId() {
        assertThatThrownBy(() -> tagService.deleteTag(null))
                .isInstanceOf(InvalidTagException.class)
                .hasMessage("Tag ID cannot be null.");
    }

    @Test
    void updateTag_invalidTagForUpdate() {
        doThrow(new InvalidTagException("Invalid tag for update")).when(tagValidator).validateTagForUpdate(any(Tag.class));

        assertThatThrownBy(() -> tagService.updateTag(tag))
                .isInstanceOf(InvalidTagException.class)
                .hasMessage("Invalid tag for update");
    }

}