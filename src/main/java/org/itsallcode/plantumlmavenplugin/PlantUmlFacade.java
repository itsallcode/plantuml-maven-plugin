package org.itsallcode.plantumlmavenplugin;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class PlantUmlFacade {
    private final ClassLoader loader;
    private final Log log;
    private final Class<?> sourceFileReaderClass;
    private final Class<?> fileFormatOptionClass;
    private final Class<?> fileFormatClass;
    private final Object pngFormat;
    private final Object svgFormat;

    /**
     * Create a facade for the PlantUML invocation.
     * <p>
     * This class hides all details of the loading and invoking the PlantUML classes. That allows defining the
     * dependency in the using project's POM file, instead of creating a hard compile time dependency. As long as the
     * PlantUML API stays compatible, this facade abstracts the implementation details of calling PlantUML.
     * </p>
     * <p>
     * The facade caches loaded classes and objects that can be reused to safe resources and speed-up batch rendering.
     * </p>
     *
     * @param log maven Mojo log
     */
    public PlantUmlFacade(final Log log) {
        this.loader = getClassLoader();
        this.log = log;
        try {
            this.sourceFileReaderClass = loadPlantUmlClass("net.sourceforge.plantuml.SourceFileReader");
            this.fileFormatOptionClass = loadPlantUmlClass("net.sourceforge.plantuml.FileFormatOption");
            this.fileFormatClass = loadPlantUmlClass("net.sourceforge.plantuml.FileFormat");
            this.pngFormat = getPlantUmlFileFormat(ImageType.PNG);
            this.svgFormat = getPlantUmlFileFormat(ImageType.SVG);
        } catch (final ClassNotFoundException exception) {
            throw new RuntimeException("Unable to load PlantUML classes", exception);
        }
    }

    /**
     * Resolves the class loader used to access the user-configured PlantUML dependency.
     *
     * @return the thread context class loader when available, otherwise this plugin's class loader
     */
    ClassLoader getClassLoader() {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return (contextClassLoader != null) ? contextClassLoader : getClass().getClassLoader();
    }

    /**
     * Loads a PlantUML class from the plugin execution class loader.
     *
     * @param className fully qualified PlantUML class name
     * @return resolved class
     * @throws ClassNotFoundException if the configured PlantUML dependency does not provide the class
     */
    Class<?> loadPlantUmlClass(final String className) throws ClassNotFoundException {
        this.log.debug(("Loading PlantUML class '" + className) + "'");
        return Class.forName(className, true, this.loader);
    }

    private Object getPlantUmlFileFormat(final ImageType imageType) {
        final Object fileFormat = resolveEnumConstant(this.fileFormatClass, imageType.name());
        final Constructor<?> optionConstructor;
        try {
            optionConstructor = fileFormatOptionClass.getConstructor(fileFormatClass);
            return optionConstructor.newInstance(fileFormat);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException exception) {
            throw new RuntimeException("Unable to create PlantUML file format " + imageType, exception);
        }
    }

    static Object resolveEnumConstant(final Class<?> enumClass, final String constantName) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("Class '" + enumClass.getName() + "' is not an enum");
        }
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(constant -> ((Enum<?>) constant).name().equals(constantName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Enum constant '" + constantName + "' not found in '"
                        + enumClass.getName() + "'"));

    }
    // [impl->dsn~render-png~1]
    // [impl->dsn~render-svg~1]
    public void renderFileToDirectory(final File inputFile, final File outputDirectory, final ImageType imageType) {
        this.log.info("Rendering '" + inputFile + "' to '" + imageType + "' image.");
            final Object options = switch(imageType) {
                case PNG -> this.pngFormat;
                case SVG -> this.svgFormat;
            };
            generateImages(inputFile, outputDirectory, options);
    }

    private void generateImages(final File inputFile, final File outputDirectory, final Object options) {
        try {
            final Constructor<?> readerConstructor = this.sourceFileReaderClass.getConstructor(boolean.class, File.class
                    , File.class, this.fileFormatOptionClass);
            final Object reader = readerConstructor.newInstance(false, inputFile, outputDirectory, options);
            final Method getGeneratedImagesMethod = this.sourceFileReaderClass.getMethod("getGeneratedImages");
            getGeneratedImagesMethod.invoke(reader);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException
                     | IllegalAccessException exception) {
                throw new RuntimeException("Unable to generate images", exception);
            }
    }
}
