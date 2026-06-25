# PlantUML Maven Plugin

This plugin renders PlantUML diagrams during a Maven build while letting the consuming project choose the PlantUML dependency version.

## Configuration

The plugin supports the configuration shape used by `com.github.jeluard:plantuml-maven-plugin` for source file selection, output format, and output directory.

```xml
<plugin>
  <groupId>org.itsallcode</groupId>
  <artifactId>plantuml-maven-plugin</artifactId>
  <version>0.2.0</version>
  <configuration>
    <sourceFiles>
      <directory>${basedir}/model/diagrams</directory>
      <includes>
        <include>**/*.plantuml</include>
      </includes>
    </sourceFiles>
    <format>svg</format>
    <outputDirectory>${basedir}/doc/images/uml</outputDirectory>
  </configuration>
  <dependencies>
    <dependency>
      <groupId>net.sourceforge.plantuml</groupId>
      <artifactId>plantuml</artifactId>
      <version>1.2026.6</version>
    </dependency>
  </dependencies>
</plugin>
```

Rendered files keep their relative path below `sourceFiles.directory`, so nested diagram trees are reproduced below `outputDirectory`.
