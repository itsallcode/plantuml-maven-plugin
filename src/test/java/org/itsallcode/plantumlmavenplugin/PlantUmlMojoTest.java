package org.itsallcode.plantumlmavenplugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PlantUmlMojoTest {
    @ParameterizedTest
    @CsvSource({
            "png, PNG",
            "SVG, SVG",
            "' svg ', SVG"
    })
    void givenSupportedFormatWhenNormalizingThenMojoUsesUpperCaseValue(final String configuredFormat,
            final ImageType expectedFormat) {
        final PlantUmlMojo mojo = new PlantUmlMojo();
        assertThat(mojo.normalizeFormat(configuredFormat), is(expectedFormat));
    }

    @Test
    void givenMissingFormatWhenNormalizingThenMojoUsesPngByDefault() {
        final PlantUmlMojo mojo = new PlantUmlMojo();
        assertThat(mojo.normalizeFormat(null), is(ImageType.PNG));
    }

    @ParameterizedTest
    @CsvSource({
            "' '",
            "jpg"
    })
    void givenUnsupportedFormatWhenNormalizingThenMojoRejectsIt(final String configuredFormat) {
        final PlantUmlMojo mojo = new PlantUmlMojo();
        assertThrows(IllegalArgumentException.class, () -> mojo.normalizeFormat(configuredFormat));
    }

    @Test
    // [utest->dsn~select-source-files~1]
    void givenMissingSourceDirectoryWhenNormalizingThenMojoRejectsIt() {
        final PlantUmlMojo mojo = new PlantUmlMojo();
        final PlantUmlMojo.SourceFiles sourceFiles = new PlantUmlMojo.SourceFiles();
        assertThrows(IllegalArgumentException.class, () -> mojo.normalizeSourceFiles(sourceFiles));
    }

    @Test
    // [utest->dsn~select-source-files~1]
    void givenBlankIncludeWhenCompilingMatchersThenMojoRejectsIt() {
        final PlantUmlMojo mojo = new PlantUmlMojo();
        final List<String> includes = List.of(" ");
        assertThrows(IllegalArgumentException.class, () -> mojo.compileMatchers(includes));
    }

    @CsvSource({
            "diagrams/*.puml, diagrams/overview.puml, true",
            "**/*.puml, diagrams/overview.puml, true",
            "**/*.puml, diagrams/overview.plantuml, false",
            "docs/*.puml, diagrams/overview.puml, false"

    })
    @ParameterizedTest
    // [utest->dsn~select-source-files~1]
    void givenInputFilesWhenCheckingIncludeThenMojoAcceptsIt(final String glob, final String relativePath,
                                                             final boolean expectedAcceptance) {
        final PlantUmlMojo mojo = new PlantUmlMojo();
        final Path sourceRoot = Path.of("/tmp", "src");
        final File file = sourceRoot.resolve(relativePath).toFile();
        assertThat(mojo.isIncludedPlantUmlFile(file, sourceRoot, mojo.compileMatchers(List.of(glob))),
                is(expectedAcceptance));
    }

    @Test
    // [utest->dsn~preserve-relative-output-paths~1]
    void givenNestedSourceFileWhenResolvingOutputDirectoryThenMojoPreservesRelativePath() throws Exception {
        final PlantUmlMojo mojo = new PlantUmlMojo();
        final File outputDirectory = new File("/tmp/out");
        setField(mojo, "outputDirectory", outputDirectory);
        final File sourceDirectory = new File("/tmp/src");
        final File inputFile = new File(sourceDirectory, "architecture/context/overview.puml");
        final File resolvedOutputDirectory = mojo.resolveOutputDirectoryFor(inputFile, sourceDirectory);
        assertThat(resolvedOutputDirectory, is(new File(outputDirectory, "architecture/context")));
    }

    @Test
    // [utest->dsn~preserve-relative-output-paths~1]
    void givenTopLevelSourceFileWhenResolvingOutputDirectoryThenMojoUsesConfiguredOutputDirectory() throws Exception {
        final PlantUmlMojo mojo = new PlantUmlMojo();
        final File outputDirectory = new File("/tmp/out");
        setField(mojo, "outputDirectory", outputDirectory);
        final File sourceDirectory = new File("/tmp/src");
        final File inputFile = new File(sourceDirectory, "overview.puml");

        final File resolvedOutputDirectory = mojo.resolveOutputDirectoryFor(inputFile, sourceDirectory);

        assertThat(resolvedOutputDirectory, is(outputDirectory));
    }

    private static void setField(final Object target, final String fieldName, final Object value) throws Exception {
        final Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
