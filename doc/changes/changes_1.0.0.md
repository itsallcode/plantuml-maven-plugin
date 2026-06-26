# PlantUML Maven Plugin 1.0.0, released 2026-06-26

Code name: Drop-in Rendering

## Summary

This is the first stable release of the PlantUML Maven Plugin.

The plugin renders PlantUML diagrams during a Maven build without bundling a fixed PlantUML version. Instead, the consuming project declares the PlantUML dependency version in the plugin configuration, so teams can keep diagram rendering aligned with the PlantUML version they already use.

The plugin also supports the configuration shape of `com.github.jeluard:plantuml-maven-plugin` for source file selection, output format, and output directory. Existing Maven builds can therefore switch to this plugin without restructuring the relevant POM section.

Rendered diagrams keep the relative path of their source files below `sourceFiles.directory`. This makes it practical to generate output for nested diagram trees into a dedicated target directory or documentation folder while preserving an existing directory layout.

## Features

* Render PlantUML source files with the PlantUML library version configured by the consuming Maven project.
* Configure the output directory, with `${project.build.directory}/generated-diagrams` as the default.
* Preserve relative source paths when writing generated diagrams to the output directory.
* Select source files through `sourceFiles.directory` and `sourceFiles.includes.include`.
* Generate a single configured output format per execution: `png` or `svg`.
* Support the `com.github.jeluard:plantuml-maven-plugin` configuration shape as a drop-in replacement.
