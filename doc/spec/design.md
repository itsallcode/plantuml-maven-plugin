# Design

## Introduction

This document describes the architecture of the PlantUML Maven Plugin. It follows the [arc42](https://docs.arc42.org) template.

## Solution Strategy

The plugin is implemented as a Maven Mojo. It interacts with the PlantUML library to render diagrams. The PlantUML dependency is declared by the user, not by building a fat jar.

A facade abstracts the reflective way of accessing the PlantUML classes.

## Building Block View

The Maven Plugin consists of a MavenMojo and a PlantUML facade.

```plantuml
@startuml
set separator none
hide empty members

package org.itsallcode.plantumlmavenplugin {
    class PlantUmlMojo
    class PlantUmlFacade
    
    PlantUmlMojo -> PlantUmlFacade
}

package net.sourceforge.plantuml {
    class SourceFileReader
    class FileFormatOption
    class FileFormat
}

PlantUmlFacade -d-> SourceFileReader
PlantUmlFacade -d-> FileFormatOption
PlantUmlFacade -d-> FileFormat

@enduml
```

### Used PlantUML Classes

The [`SourceFileReader`](https://github.com/plantuml/plantuml/blob/master/src/main/java/net/sourceforge/plantuml/SourceFileReader.java) — despite its name — does not only read files, but also decides where the rendered result they should go in the output directory.

[`FileFormat`](https://github.com/plantuml/plantuml/blob/master/src/main/java/net/sourceforge/plantuml/FileFormat.java) is an enum listing file formats PlantUML can handle with file extensions and Mime types. The facade abstracts and restricts this with `ImageFormat`.

[`FileFormatOption`](https://github.com/plantuml/plantuml/blob/master/src/main/java/net/sourceforge/plantuml/FileFormatOption.java) is a parameter object that be used to control a number of settings for the generated output files. 

## Runtime View

### Render Using Configured PlantUML Dependency
`dsn~render-using-configured-plant-uml-dependency~1`

**GIVEN** the PlantUML dependency is configured in the POM file
**WHEN** he `execute()` method is called
**THEN** the plugin uses the configured PlantUML library version to render diagrams.

Covers:

* `scn~render-using-configured-plantuml-version~1`

Needs: impl, utest

### Render to Default Output Directory
`dsn~render-default-output-directory~1`

**GIVEN** the `outputDirectory` Mojo parameter is null or empty
**WHEN** the `execute()` method is called
**THEN** the plugin resolves `${project.build.directory}/generated-diagrams` as the target path
**AND** invokes the PlantUML library to write diagrams to that path.

Covers:
* `scn~render-default-output-directory~1`

Needs: impl

### Preserve Relative Output Paths
`dsn~preserve-relative-output-paths~1`

**GIVEN** a rendered PlantUML source file is located below the configured source directory
**WHEN** the `execute()` method renders the file
**THEN** the plugin resolves the file's relative path below the source directory
**AND** writes the rendered output below the configured output directory using that same relative path
**AND** creates only output directories that are required for rendered PlantUML files.

Covers:
* `scn~preserve-relative-output-paths~1`

Needs: impl, itest

### Configure Output Directory
`dsn~configure-output-directory~1`

**GIVEN** the `outputDirectory` Mojo parameter is set to a specific path
**WHEN** the `execute()` method is called
**THEN** the plugin uses the configured path as the target for diagram generation.

Covers:
* `scn~configure-output-directory~1`

Needs: impl

### Select Source Files
`dsn~select-source-files~1`

**GIVEN** the Mojo parameter `sourceFiles.directory` is set
**AND** the Mojo parameter `sourceFiles.includes.include` contains glob patterns
**WHEN** the `execute()` method discovers PlantUML source files
**THEN** the plugin scans recursively below the configured source directory
**AND** keeps only files with established PlantUML suffixes whose relative path matches one of the configured include patterns.

Covers:
* `scn~select-source-files~1`

Needs: impl, utest, itest

### Render PNG
`dsn~render-png~1`

**GIVEN** the `format` Mojo parameter is `png` (case-insensitive)
**WHEN** the `execute()` method is called
**THEN** the plugin configures the PlantUML `FileFormatOption` to `PNG` for each selected input file.

Covers:
* `scn~render-png~1`

Needs: impl

### Render SVG
`dsn~render-svg~1`

**GIVEN** the `format` Mojo parameter is `svg` (case-insensitive)
**WHEN** the `execute()` method is called
**THEN** the plugin configures the PlantUML `FileFormatOption` to `SVG` for each selected input file.

Covers:
* `scn~render-svg~1`

Needs: impl

## Architecture Decisions

### How Does the Plugin use the PlantUML Library?

We did not want to bundle the PlantUML library with the jar, to allow users to update independently.

#### Dynamic PlantUML Dependency
`dsn~dynamic-dependency~1`

To allow the user to select the PlantUML version, the plugin does not include PlantUML as a compile-time dependency. Instead, it loads the specified version of PlantUML at runtime using Maven's dependency resolution mechanism.

Rationale:

This avoids a fat jar and allows the plugin to be independent of specific PlantUML versions, giving users full control. Additionally, this allows using forks instead of the original PlantUML dependency (if that should ever be necessary).

Covers:
* `scn~render-using-configured-plantuml-version~1`

Needs: impl

#### Alternatives Considered

We also thought about having the plugin load the PlantUML library at runtime. The UX benefit would be that the users only need to provide the version number and the configuration would be more compact.

We decided against this option because it would make the code more complicated and cost flexibility in picking the dependency. Also, it would be code duplication with Maven's dependency resolution mechanism.

## Quality

See ["Quality Requirements"](design/quality_requirements.md)
