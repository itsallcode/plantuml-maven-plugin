# PlantUML Maven Plugin

This plugin renders PlantUML diagrams during a Maven build while letting the consuming project choose the PlantUML dependency version.

[![Build](https://github.com/itsallcode/plantuml-maven-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/itsallcode/plantuml-maven-plugin/actions/workflows/build.yml)

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

## CI

GitHub Actions runs `mvn clean verify` for pushes to `main` and pull requests. When `SONAR_TOKEN` is configured in GitHub Actions secrets, the workflow also publishes coverage and static-analysis results to SonarCloud.

SonarCloud consumes the JaCoCo XML report produced by the Maven build. The Maven Invoker based integration tests still run in CI, but their execution is not fully attributed in JaCoCo because they execute the plugin in separate Maven processes.
