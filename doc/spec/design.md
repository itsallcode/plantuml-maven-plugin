# Design

## Introduction

This document describes the architecture and design of the PlantUML Maven Plugin. It follows the arc42 template.

## Architecture

The plugin is implemented as a Maven Mojo. It interacts with the PlantUML library to render diagrams.

## Design Decisions

### Dynamic PlantUML Dependency
`dsn~dynamic-dependency~1`

To allow the user to select the PlantUML version, the plugin does not include PlantUML as a compile-time dependency. Instead, it loads the specified version of PlantUML at runtime using Maven's dependency resolution mechanism.

Rationale:

This avoids a fat jar and allows the plugin to be independent of specific PlantUML versions, giving users full control.

Covers:
* `req~configure-plantuml-dependency~1`

Needs: impl

### Mojo Configuration
`dsn~mojo-config~1`

The Mojo exposes parameters for `plantUmlVersion`, `outputDirectory`, and `formats`.

Rationale:

Maven's standard configuration mechanism is used to provide a familiar experience for developers.

Covers:
* `req~configure-output-directory~1`
* `req~configure-output-format~1`

Needs: impl

## Runtime Scenarios

### Render to Default Output Directory
`dsn~render-default-output-directory~1`

**GIVEN** the `outputDirectory` Mojo parameter is null or empty
**WHEN** the `execute()` method is called
**THEN** the plugin resolves `${project.build.directory}/generated-diagrams` as the target path
**AND** invokes the PlantUML library to write diagrams to that path.

Covers:
* `scn~render-default-output-directory~1`

Needs: impl

### Configure Output Directory
`dsn~configure-output-directory~1`

**GIVEN** the `outputDirectory` Mojo parameter is set to a specific path
**WHEN** the `execute()` method is called
**THEN** the plugin uses the configured path as the target for diagram generation.

Covers:
* `scn~configure-output-directory~1`

Needs: impl

### Render PNG
`dsn~render-png~1`

**GIVEN** the `formats` Mojo parameter includes `PNG`
**WHEN** the `execute()` method is called
**THEN** the plugin configures the PlantUML `FileFormatOption` to `PNG` for each input file.

Covers:
* `scn~render-png~1`

Needs: impl

### Render SVG
`dsn~render-svg~1`

**GIVEN** the `formats` Mojo parameter includes `SVG`
**WHEN** the `execute()` method is called
**THEN** the plugin configures the PlantUML `FileFormatOption` to `SVG` for each input file.

Covers:
* `scn~render-svg~1`

Needs: impl
