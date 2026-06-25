package org.itsallcode.plantumlmavenplugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PlantUmlFacadeTest {
    private enum SampleFormat {
        PNG,
        SVG
    }

    @Test
    void givenEnumClassWhenResolvingConstantThenMatchingEnumConstantIsReturned() {
        assertThat(PlantUmlFacade.resolveEnumConstant(SampleFormat.class, "SVG"), is(SampleFormat.SVG));
    }

    @Test
    void givenNonEnumClassWhenResolvingConstantThenFacadeRejectsIt() {
        assertThrows(IllegalArgumentException.class, () -> PlantUmlFacade.resolveEnumConstant(String.class, "SVG"));
    }
}
