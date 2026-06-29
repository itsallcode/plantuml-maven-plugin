# PlantUML Maven Plugin

This plugin renders PlantUML diagrams during a Maven build while letting the consuming project choose the PlantUML dependency version.

Rendered files keep their relative path below `sourceFiles.directory`, so nested diagram trees are reproduced below `outputDirectory`.

## Project Information

[![Build](https://github.com/itsallcode/openfasttrace/actions/workflows/build.yml/badge.svg)](https://github.com/itsallcode/openfasttrace/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.itsallcode/openfasttrace.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.itsallcode%22%20a%3A%22openfasttrace%22)

SonarCloud status:

[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=bugs)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)
[![Code smells](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=code_smells)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)
[![Duplicated Lines](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=ncloc)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=security_rating)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)
[![Technical Dept](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=sqale_index)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aplantuml-maven-plugin&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aplantuml-maven-plugin)

* [Changelog](doc/changes/changelog.md)
* [Code of conduct](CODE_OF_CONDUCT.md)
* [Contributing](CONTRIBUTING.md)

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

## CI

GitHub Actions runs `mvn clean verify` for pushes to `main` and pull requests. When `SONAR_TOKEN` is configured in GitHub Actions secrets, the workflow also publishes coverage and static-analysis results to SonarCloud.

SonarCloud consumes the JaCoCo XML report produced by the Maven build. The Maven Invoker based integration tests run the plugin in separate Maven processes, so the build injects a dedicated JaCoCo agent into those forked JVMs and merges their execution data with the unit-test coverage before generating the report.
