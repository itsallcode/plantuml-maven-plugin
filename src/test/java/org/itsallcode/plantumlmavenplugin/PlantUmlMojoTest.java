package org.itsallcode.plantumlmavenplugin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlantUmlMojoTest {
    @ParameterizedTest
    @CsvSource({
            "png, PNG",
            "SVG, SVG",
            "' svg ', SVG"
    })
    void givenSupportedFormatWhenNormalizingThenMojoUsesUpperCaseValue(final String configuredFormat, final String expectedFormat) {
        final PlantUmlMojo mojo = new PlantUmlMojo();
        assertEquals(expectedFormat, mojo.normalizeFormat(configuredFormat));
    }

    @ParameterizedTest
    @CsvSource({
            "' '",
            "jpg"
    })
    void givenUnsupportedFormatWhenNormalizingThenMojoRejectsIt(final String configuredFormat) {
        final PlantUmlMojo mojo = new PlantUmlMojo();
        assertThrows(IllegalArgumentException.class, () -> mojo.normalizeFormats(java.util.Collections.singletonList(configuredFormat)));
    }
}
