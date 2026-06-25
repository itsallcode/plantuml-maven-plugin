# System Requirements

## Introduction

This document specifies the requirements for the PlantUML Maven Plugin.

## Features

### Diagram Rendering
`feat~diagram-rendering~1`

The plugin renders PlantUML diagrams from source files. By default, it includes all established PlantUML suffixes (`.puml`, `.plantuml`, `.iuml`) in the input.

Needs: req

## Requirements

### Configure PlantUML Version 
`req~configure-plantuml-version~1`
The user specifies the version of the PlantUML JAR to be used for rendering.

Covers:
* `feat~diagram-rendering~1`

Needs: scn

### Configure Output Directory
`req~configure-output-directory~1`
The user configures the directory where the generated diagrams are saved.

Default: `${project.build.directory}/generated-diagrams`

Covers:
* `feat~diagram-rendering~1`

Needs: scn

### Preserve Relative Output Paths
`req~preserve-relative-output-paths~1`
The plugin preserves the relative path of each rendered PlantUML source file below the configured source directory when writing output files. It does not create output directories for paths where no PlantUML source file was rendered.

Rationale:

This allows rendering hierarchical structures and avoids name conflicts for identical file names in different directories.

Covers:
* `feat~diagram-rendering~1`

Needs: scn

### Configure Source Files
`req~configure-source-files~1`
The user configures the source directory and include patterns that select the PlantUML files to render.

Covers:
* `feat~diagram-rendering~1`

Needs: scn

### Configure Output Format
`req~configure-output-format~2`
The user configures a single output format for rendered diagrams. The plugin supports `png` and `svg`.

Covers:
* `feat~diagram-rendering~1`

Needs: scn

## Scenarios

### Render Using Configured PlantUML Version
`scn~render-using-configured-plantuml-version~1`

**GIVEN** a source directory that contains PlantUML files
**AND** the user configured the PlantUML version
**WHEN** a user executes `render`
**THEN** the plugin uses the configured PlantUML version.

Covers:
* `req~configure-plantuml-version~1`

Needs: dsn

### Render to Default Output Directory
`scn~render-default-output-directory~1`
**GIVEN** a source directory that contains PlantUML files
**AND** the output directory is not configured
**WHEN** a user executes `render`
**THEN** the plugin renders the files to the default output directory.

Covers:
* `req~configure-output-directory~1`

Needs: dsn

### Configure Output Directory
`scn~configure-output-directory~1`
**GIVEN** a source directory that contains PlantUML files
**AND** the output directory is configured
**WHEN** a user executes `render`
**THEN** the plugin renders the files to the configured output directory.

Covers:
* `req~configure-output-directory~1`

Needs: dsn

### Preserve Relative Output Paths
`scn~preserve-relative-output-paths~1`
**GIVEN** a configured source directory containing PlantUML files in nested directories
**WHEN** a user executes `render`
**THEN** the plugin writes each rendered diagram below the output directory using the same relative path as its source file below the source directory
**AND** the plugin does not create output directories for paths that contain no rendered PlantUML files.

Covers:
* `req~preserve-relative-output-paths~1`

Needs: dsn

### Select Source Files
`scn~select-source-files~1`
**GIVEN** a configured source directory that contains PlantUML files
**AND** include patterns are configured
**WHEN** a user executes `render`
**THEN** the plugin renders only the files matching the include patterns from the configured source directory.

Covers:
* `req~configure-source-files~1`

Needs: dsn

### Render PNG
`scn~render-png~1`
**GIVEN** a source directory that contains PlantUML files
**AND** the output format is set to PNG
**WHEN** a user executes `render`
**THEN** the plugin renders the files to PNG format.

Covers:
* `req~configure-output-format~2`

Needs: dsn

### Render SVG 
`scn~render-svg~1`
**GIVEN** a source directory that contains PlantUML files
**AND** the output format is set to SVG
**WHEN** a user executes `render`
**THEN** the plugin renders the files to SVG format.

Covers:
* `req~configure-output-format~2`

Needs: dsn
