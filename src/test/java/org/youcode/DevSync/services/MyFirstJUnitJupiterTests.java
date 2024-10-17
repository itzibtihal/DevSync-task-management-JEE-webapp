package org.youcode.DevSync.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("Disabled until bug #99 has been fixed")
public class MyFirstJUnitJupiterTests
{


    @Test
    void addition() {
        assertEquals(2,2);
    }

    @Test
    @DisplayName("😱 uuuugh")
    void testWithDisplayNameContainingEmoji() {
    }

    @Disabled("Disabled until bug #42 has been resolved")
    @Test
    void testWillBeSkipped() {
    }



}
