package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.AddTag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddTagTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String name = "Tag";
        String imageURL = "image.jpg";

        // Act
        AddTag addTag = new AddTag(name, imageURL);

        // Assert
        assertEquals(name, addTag.getName());
        assertEquals(imageURL, addTag.getImageURL());
    }

    @Test
    public void testSetters() {
        // Arrange
        AddTag addTag = new AddTag();

        // Act
        String name = "Tag";
        addTag.setName(name);

        String imageURL = "image.jpg";
        addTag.setImageURL(imageURL);

        // Assert
        assertEquals(name, addTag.getName());
        assertEquals(imageURL, addTag.getImageURL());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        AddTag addTag1 = new AddTag("Tag", "image.jpg");
        AddTag addTag2 = new AddTag("Tag", "image.jpg");
        AddTag addTag3 = new AddTag("AnotherTag", "image.jpg");

        // Assert
        assertEquals(addTag1, addTag2);
        assertEquals(addTag1.hashCode(), addTag2.hashCode());
        assertEquals(addTag1.equals(addTag2), addTag2.equals(addTag1)); // Symmetric property
        assertEquals(false, addTag1.equals(null)); // Non-nullity property
        assertEquals(false, addTag1.equals(addTag3)); // Equality check for different objects
    }

    @Test
    public void testToString() {
        // Arrange
        AddTag addTag = new AddTag("Tag", "image.jpg");

        // Act & Assert
        assertEquals("AddTag(name=Tag, imageURL=image.jpg)", addTag.toString());
    }
}

