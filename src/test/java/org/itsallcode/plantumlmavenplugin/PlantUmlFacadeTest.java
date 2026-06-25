package org.itsallcode.plantumlmavenplugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlantUmlFacadeTest {
    private enum SampleFormat {
        PNG,
        SVG
    }

    @Test
    void givenEnumClassWhenResolvingConstantThenMatchingEnumConstantIsReturned() {
        assertEquals(SampleFormat.SVG, PlantUmlFacade.resolveEnumConstant(SampleFormat.class, "SVG"));
    }

    @Test
    void givenNonEnumClassWhenResolvingConstantThenFacadeRejectsIt() {
        assertThrows(IllegalArgumentException.class, () -> PlantUmlFacade.resolveEnumConstant(String.class, "SVG"));
    }
}
