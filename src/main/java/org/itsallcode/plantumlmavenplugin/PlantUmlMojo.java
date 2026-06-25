package org.itsallcode.plantumlmavenplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
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
    @Parameter(defaultValue = "${project.build.directory}/generated-diagrams", property = "outputDirectory")
    private File outputDirectory;

    /**
     * Output formats to generate.
     * Supported values are {@code PNG} and {@code SVG}.
     */
    @Parameter(property = "formats")
    private List<String> formats;

    /**
     * Source directory that is scanned recursively for PlantUML files.
     * Supported suffixes are {@code .puml}, {@code .plantuml}, and {@code .iuml}.
     */
    @Parameter(defaultValue = "${basedir}/src/main/resources", property = "sourceDirectory")
    private File sourceDirectory;

    /**
     * Executes the rendering goal.
     *
     * @throws MojoExecutionException if format validation, output directory creation, or rendering fails
     */
    public void execute() throws MojoExecutionException {
        try {
            formats = normalizeFormats(formats);
            createOutputDirectory(outputDirectory);
            renderDiagrams();
        } catch (Exception e) {
            throw new MojoExecutionException("Error rendering PlantUML diagrams", e);
        }
    }

    /**
     * Normalizes the configured output formats and applies the default format when none was configured.
     *
     * @param configuredFormats raw formats from plugin configuration
     * @return validated uppercase format names
     */
    List<String> normalizeFormats(final List<String> configuredFormats) {
        if (configuredFormats == null || configuredFormats.isEmpty()) {
            return List.of("PNG");
        }
        final List<String> normalizedFormats = new ArrayList<>(configuredFormats.size());
        for (final String configuredFormat : configuredFormats) {
            final String normalizedFormat = normalizeFormat(configuredFormat);
            if (!"PNG".equals(normalizedFormat) && !"SVG".equals(normalizedFormat)) {
                throw new IllegalArgumentException("Unsupported PlantUML output format '" + configuredFormat
                        + "'. Chose one of: PNG, SVG");
            }
            normalizedFormats.add(normalizedFormat);
        }
        return normalizedFormats;
    }

    /**
     * Trims and uppercases a single configured output format.
     *
     * @param configuredFormat raw format value from plugin configuration
     * @return normalized uppercase format name
     */
    String normalizeFormat(final String configuredFormat) {
        if (configuredFormat == null || configuredFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("PlantUML output formats must not be blank");
        }
        return configuredFormat.trim().toUpperCase(Locale.ROOT);
    }

    private void createOutputDirectory(final File directory) throws IOException {
        if (directory == null) {
            throw new IllegalArgumentException("Output directory must not be null");
        }
        if (directory.exists() && !directory.isDirectory()) {
            throw new IOException("Output path is not a directory: " + directory);
        }
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Could not create output directory " + directory);
        }
    }

    // [impl->dsn~dynamic-dependency~1]
    // [impl->dsn~render-using-configured-plant-uml-dependency~1]
    private void renderDiagrams() {
        if (sourceDirectory == null) {
            throw new IllegalArgumentException("Source directory must not be null");
        }
        final List<File> files = findPlantUmlFiles(sourceDirectory);
        if (files.isEmpty()) {
            getLog().info("No PlantUML files found in " + sourceDirectory);
            return;
        }

        for (final String format : formats) {
            for (final File file : files) {
                renderFile(format, file);
            }
        }
    }

    // [impl->dsn~render-png~1]
    // [impl->dsn~render-svg~1]
    private void renderFile(final String format, final File file) {
        // We load the facade as late as possible to allow unit testing the other Mojo functions
        final PlantUmlFacade plantUmlFacade = new PlantUmlFacade(this.getLog());
        plantUmlFacade.renderFileToDirectory(file, this.outputDirectory, ImageType.valueOf(format));
    }

    private List<File> findPlantUmlFiles(final File dir) {
        final List<File> result = new ArrayList<>();
        if (dir.exists() && dir.isDirectory()) {
            final File[] files = dir.listFiles();
            if (files != null) {
                for (final File file : files) {
                    if (file.isDirectory()) {
                        result.addAll(findPlantUmlFiles(file));
                    } else if (isPlantUmlFile(file.getName())) {
                        result.add(file);
                    }
                }
            }
        }
        return result;
    }

    private boolean isPlantUmlFile(final String name) {
        return name.endsWith(".puml") || name.endsWith(".plantuml") || name.endsWith(".iuml");
    }
}
