# 1-basic-plantuml-plugin

## Goal

Implement a basic PlantUML Maven Plugin that allows users to render diagrams with a selectable PlantUML version and configurable output directory.

## Scope

In scope:

*   Selectable PlantUML JAR version in the plugin configuration.
*   Configurable output directory with default `target/generated-diagrams`.
*   Support for PNG and SVG output formats.
*   Minimal runtime dependencies (Maven and PlantUML).

Out of scope:

*   Advanced PlantUML features (e.g., specific skinparams, includes) beyond basic rendering.
*   Complex file filtering (e.g., regex includes/excludes) - initially will target all `.puml` and `plantuml` files.

## Design References

*   [System Requirements](../spec/system_requirements.md)
*   [Quality Requirements](../spec/design/quality_requirements.md)
*   [Design](../spec/design.md)

## Strategy

The plugin will be implemented as a Maven Mojo. Since the PlantUML version must be selectable, we cannot have a static dependency on `org.plantuml:plantuml`. Instead, we will load the specified version of PlantUML at runtime using Maven's dependency resolution mechanism.

To avoid a fat jar and keep dependencies minimal, we will use the Maven Plugin API.

## Task List

- [x] Create and checkout a new Git branch `feat/1-basic-plantuml-plugin`

### Requirements And Design

- [x] Initial system requirements in `doc/spec/system_requirements.md` (reworked to `type~id~rev` style)
- [x] Initial design in `doc/spec/design.md` (reworked to arc42 and `type~id~rev` style)
- [x] Technical design scenarios for each user scenario in `doc/spec/design.md`
- [x] Adapt quality requirements in `doc/spec/design/quality_requirements.md`
- [x] Add OpenFastTrace Maven Plugin to the build
- [x] Stop and ask user for a review of the system requirements and design

### Implementation

- [x] Update `pom.xml` with necessary Maven dependencies for Mojo development and dynamic dependency resolution.
- [x] Implement `PlantUmlMojo` with configuration parameters for `plantUmlVersion`, `outputDirectory`, and `formats`.
- [x] Implement logic to invoke PlantUML via reflection after loading the JAR at runtime.

### Verification

- [x] Create an integration test (IT) that uses the plugin to generate PNG and SVG diagrams.
- [x] Verify that the output files exist in the expected directory.
- [x] Keep the OpenFastTrace trace clean.
- [x] Keep required build and plugin verification tasks green.

### Update user documentation

- [ ] Update `README.md` with usage instructions and configuration options.

## Version and Changelog Update

- [ ] Raise the version to 0.1.0
- [ ] Write the changelog entry for 0.1.0
