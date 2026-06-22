# System Requirements

## Introduction

This document specifies the requirements for the PlantUML Maven Plugin.

## Features

### Diagram Rendering
`feat~diagram-rendering~1`

The plugin renders PlantUML diagrams from source files. By default, it includes all established PlantUML suffixes (`.puml`, `.plantuml`, `.iuml`) in the input.

Needs: req

## Requirements

### `req~plantuml-version~1`
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

### Configure Output Format
`req~configure-output-format~1`
The plugin supports rendering diagrams in PNG, SVG, or both formats.

Covers:
* `feat~diagram-rendering~1`

Needs: scn

## Scenarios

### Render to Default Output Directory
`scn~render-default-output-directory~1`
**GIVEN** a source directory that contains PlantUML files
**AND** the output directory is not configured
**WHEN** a user executes `render`
**THEN** the plugin renders the files to the default output directory.

### Configure Output Directory
`scn~configure-output-directory~1`
**GIVEN** a source directory that contains PlantUML files
**AND** the output directory is configured
**WHEN** a user executes `render`
**THEN** the plugin renders the files to the configured output directory.

Covers:
* `req~configure-output-directory~1`

Needs: impl, itest

### Render PNG
`scn~render-png~1`
**GIVEN** a source directory that contains PlantUML files
**AND** the output format is set to PNG
**WHEN** a user executes `render`
**THEN** the plugin renders the files to PNG format.

Covers:
* `req~output-format~1`

Needs: impl, itest

### Render SVG 
`scn~render-svg~1`
**GIVEN** a source directory that contains PlantUML files
**AND** the output format is set to SVG
**WHEN** a user executes `render`
**THEN** the plugin renders the files to SVG format.

Covers:
* req~output-format~1

Needs: impl, itest
