package org.itsallcode;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Goal that renders PlantUML diagrams.
 *
 */
@Mojo(name = "render", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class PlantUmlMojo extends AbstractMojo {

    /**
     * Directory where generated diagrams will be saved.
     */
    // [impl->dsn~mojo-config~1,dsn~configure-output-directory~1]
    // [impl->dsn~render-default-output-directory~1]
    @Parameter(defaultValue = "${project.build.directory}/generated-diagrams", property = "outputDirectory")
    private File outputDirectory;

    /**
     * Output formats (e.g., PNG, SVG).
     */
    // [impl->dsn~mojo-config~1]
    @Parameter(property = "formats")
    private List<String> formats;

    /**
     * Source directory containing PlantUML files.
     */
    @Parameter(defaultValue = "${basedir}/src/main/resources", property = "sourceDirectory")
    private File sourceDirectory;

    public void execute() throws MojoExecutionException {
        if (formats == null || formats.isEmpty()) {
            formats = new ArrayList<String>();
            formats.add("PNG");
        }

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        try {
            renderDiagrams();
        } catch (Exception e) {
            throw new MojoExecutionException("Error rendering PlantUML diagrams", e);
        }
    }

    private void renderDiagrams() throws Exception {
        // [impl->dsn~dynamic-dependency~1]
        Class<?> sourceFileReaderClass = Class.forName("net.sourceforge.plantuml.SourceFileReader");
        Class<?> fileFormatOptionClass = Class.forName("net.sourceforge.plantuml.FileFormatOption");
        Class<?> fileFormatClass = Class.forName("net.sourceforge.plantuml.FileFormat");

        List<File> files = findPlantUmlFiles(sourceDirectory);
        if (files.isEmpty()) {
            getLog().info("No PlantUML files found in " + sourceDirectory);
            return;
        }

        for (String format : formats) {
            Object fileFormat = Enum.valueOf((Class<Enum>) fileFormatClass, format.toUpperCase());
            Constructor<?> optionConstructor = fileFormatOptionClass.getConstructor(fileFormatClass);
            Object options = optionConstructor.newInstance(fileFormat);

            for (File file : files) {
                renderFile(format, file, sourceFileReaderClass, fileFormatOptionClass, options);
            }
        }
    }

    // [impl->dsn~render-png~1]
    // [impl->dsn~render-svg~1]
    private void renderFile(final String format, final File file, final Class<?> sourceFileReaderClass, final Class<?> fileFormatOptionClass, final Object options) throws Exception {
        getLog().info("Rendering " + file + " to " + format);
        
        Constructor<?> readerConstructor = sourceFileReaderClass.getConstructor(boolean.class, File.class, File.class, fileFormatOptionClass);
        Object reader = readerConstructor.newInstance(false, file, outputDirectory, options);
        Method getGeneratedImagesMethod = sourceFileReaderClass.getMethod("getGeneratedImages");
        getGeneratedImagesMethod.invoke(reader);
    }

    private List<File> findPlantUmlFiles(File dir) {
        List<File> result = new ArrayList<File>();
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        result.addAll(findPlantUmlFiles(f));
                    } else if (isPlantUmlFile(f.getName())) {
                        result.add(f);
                    }
                }
            }
        }
        return result;
    }

    private boolean isPlantUmlFile(String name) {
        return name.endsWith(".puml") || name.endsWith(".plantuml") || name.endsWith(".iuml");
    }
}
