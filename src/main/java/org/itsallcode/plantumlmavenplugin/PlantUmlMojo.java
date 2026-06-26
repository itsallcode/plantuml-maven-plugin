package org.itsallcode.plantumlmavenplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Renders PlantUML source files into generated diagram artifacts during the Maven build.
 */
@Mojo(name = "render", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class PlantUmlMojo extends AbstractMojo {
    /**
     * Directory where generated diagrams are written.
     * Defaults to {@code ${project.build.directory}/generated-diagrams}.
     */
    // [impl->dsn~configure-output-directory~1]
    // [impl->dsn~render-default-output-directory~1]
    private static final List<String> DEFAULT_INCLUDE_PATTERNS = List.of("**/*.puml", "**/*.plantuml", "**/*.iuml");

    @Parameter(defaultValue = "${project.build.directory}/generated-diagrams", property = "outputDirectory")
    private File outputDirectory;

    /**
     * Output format to generate.
     * Supported values are {@code png} and {@code svg}.
     */
    @Parameter(property = "format")
    private String format;

    /**
     * Source file selection configuration.
     */
    @Parameter
    private SourceFiles sourceFiles;

    /**
     * Executes the rendering goal.
     *
     * @throws MojoExecutionException if format validation, output directory creation, or rendering fails
     */
    public void execute() throws MojoExecutionException {
        final ImageType imageType = normalizeFormat(format);
        final SourceSelection sourceSelection = normalizeSourceFiles(sourceFiles);
        createOutputDirectory(outputDirectory);
        renderDiagrams(imageType, sourceSelection);
    }

    /**
     * Normalizes the configured output format and applies the default format when none was configured.
     *
     * @param configuredFormat raw format from plugin configuration
     * @return validated image type
     */
    ImageType normalizeFormat(final String configuredFormat) {
        if (configuredFormat == null) {
            return ImageType.PNG;
        }
        if (configuredFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("PlantUML output format must not be blank");
        }
        final String normalizedFormat = configuredFormat.trim().toUpperCase(Locale.ROOT);
        try {
            return ImageType.valueOf(normalizedFormat);
        } catch (final IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unsupported PlantUML output format '" + configuredFormat
                    + "'. Choose one of: png, svg", exception);
        }
    }

    SourceSelection normalizeSourceFiles(final SourceFiles configuredSourceFiles) {
        if (configuredSourceFiles == null || configuredSourceFiles.directory == null) {
            throw new IllegalArgumentException("sourceFiles.directory must not be null");
        }
        return new SourceSelection(configuredSourceFiles.directory, compileMatchers(configuredSourceFiles.includes()));
    }

    List<PathMatcher> compileMatchers(final List<String> includes) {
        final List<String> patterns = (includes == null || includes.isEmpty()) ? DEFAULT_INCLUDE_PATTERNS : includes;
        final List<PathMatcher> matchers = new ArrayList<>(patterns.size());
        for (final String pattern : patterns) {
            if (pattern == null || pattern.trim().isEmpty()) {
                throw new IllegalArgumentException("sourceFiles.includes.include must not contain blank patterns");
            }
            final String trimmedPattern = pattern.trim();
            matchers.add(FileSystems.getDefault().getPathMatcher("glob:" + trimmedPattern));
            if (trimmedPattern.startsWith("**/")) {
                matchers.add(FileSystems.getDefault().getPathMatcher("glob:" + trimmedPattern.substring(3)));
            }
        }
        return matchers;
    }

    private static void createOutputDirectory(final File directory) {
        if (directory == null) {
            throw new IllegalArgumentException("Output directory must not be null");
        }
        if (directory.exists() && !directory.isDirectory()) {
            throw new UncheckedIOException(new IOException("Output path is not a directory: " + directory));
        }
        if (!directory.exists() && !directory.mkdirs()) {
            throw new UncheckedIOException(new IOException("Could not create output directory " + directory));
        }
    }

    // [impl->dsn~dynamic-dependency~1]
    // [impl->dsn~render-using-configured-plant-uml-dependency~1]
    private void renderDiagrams(final ImageType imageType, final SourceSelection sourceSelection) {
        final List<File> files = findPlantUmlFiles(sourceSelection);
        if (files.isEmpty()) {
            getLog().info("No PlantUML files found in " + sourceSelection.directory);
            return;
        }
        final PlantUmlFacade plantUmlFacade = new PlantUmlFacade(this.getLog());
        for (final File file : files) {
            renderFile(imageType, file, sourceSelection.directory, plantUmlFacade);
        }
    }

    // [impl->dsn~render-png~1]
    // [impl->dsn~render-svg~1]
    // [impl->dsn~preserve-relative-output-paths~1]
    private void renderFile(final ImageType imageType, final File file, final File sourceDirectory,
                            final PlantUmlFacade plantUmlFacade) {
        plantUmlFacade.renderFileToDirectory(file, resolveOutputDirectoryFor(file, sourceDirectory), imageType);
    }

    File resolveOutputDirectoryFor(final File inputFile, final File sourceDirectory) {
        final Path sourcePath = sourceDirectory.toPath().toAbsolutePath().normalize();
        final Path filePath = inputFile.toPath().toAbsolutePath().normalize();
        final Path relativePath = sourcePath.relativize(filePath);
        final Path relativeParent = relativePath.getParent();
        if (relativeParent == null) {
            return outputDirectory;
        }
        return outputDirectory.toPath().resolve(relativeParent).toFile();
    }

    static List<File> findPlantUmlFiles(final SourceSelection sourceSelection) {
        return findPlantUmlFiles(sourceSelection.directory,
                sourceSelection.directory.toPath().toAbsolutePath().normalize(),
                sourceSelection.includes);
    }

    @SuppressWarnings("java:S134") // Splitting this if-cascade makes the code less readable
    private static List<File> findPlantUmlFiles(final File dir, final Path sourceRoot,
                                                final List<PathMatcher> includes) {
        final List<File> result = new ArrayList<>();
        if (dir.exists() && dir.isDirectory()) {
            final File[] files = dir.listFiles();
            if (files != null) {
                for (final File file : files) {
                    if (file.isDirectory()) {
                        result.addAll(findPlantUmlFiles(file, sourceRoot, includes));
                    } else if (isIncludedPlantUmlFile(file, sourceRoot, includes)) {
                        result.add(file);
                    }
                }
            }
        }
        return result;
    }

    // [impl->dsn~select-source-files~1]
    static boolean isIncludedPlantUmlFile(final File file, final Path sourceRoot, final List<PathMatcher> includes) {
        if (!isPlantUmlFile(file.getName())) {
            return false;
        }
        final Path relativePath = sourceRoot.relativize(file.toPath().toAbsolutePath().normalize());
        return includes.stream().anyMatch(matcher -> matcher.matches(relativePath));
    }

    private static boolean isPlantUmlFile(final String name) {
        return name.endsWith(".puml") || name.endsWith(".plantuml") || name.endsWith(".iuml");
    }

    static final class SourceSelection {
        private final File directory;
        private final List<PathMatcher> includes;

        private SourceSelection(final File directory, final List<PathMatcher> includes) {
            this.directory = directory;
            this.includes = includes;
        }
    }

    public static final class SourceFiles {
        @Parameter(property = "sourceFiles.directory")
        private File directory;

        @Parameter
        private Includes includes;

        public SourceFiles() {
        }

        SourceFiles(final File directory, final List<String> includes) {
            this.directory = directory;
            this.includes = new Includes(includes);
        }

        private List<String> includes() {
            return (includes == null) ? null : includes.include;
        }
    }

    public static final class Includes {
        @Parameter
        private List<String> include;

        public Includes() {
        }

        Includes(final List<String> include) {
            this.include = List.copyOf(include);
        }
    }
}
