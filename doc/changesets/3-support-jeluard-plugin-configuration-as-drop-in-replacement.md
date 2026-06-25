# GH-3 Support jeluard plugin configuration as drop-in replacement

## Goal

Allow existing Maven builds that use `com.github.jeluard:plantuml-maven-plugin` to switch to this plugin without rewriting the POM configuration for source file selection, output format, or output directory.

## Scope

In scope:

* Support nested `sourceFiles` configuration with `directory` and `includes.include`.
* Support a singular `format` parameter for `png` or `svg`.
* Keep `outputDirectory` behavior compatible with the legacy plugin configuration model.
* Verify the compatibility path with integration tests using a legacy-style POM snippet.
* Update user-facing documentation to show the supported replacement configuration.

Out of scope:

* Exclude patterns or other legacy parameters not requested in GH-3.
* Multiple output formats from the legacy-compatible `format` parameter.
* Changing how users provide the PlantUML dependency.

## Design References

* [System Requirements](../spec/system_requirements.md)
* [Quality Requirements](../spec/design/quality_requirements.md)
* [Design](../spec/design.md)

## Strategy

Rework the Mojo's public configuration model around the compatibility shape from GH-3 and map it to a small internal rendering request. Keep the existing rendering facade unchanged unless compatibility work proves otherwise. For source file selection, prefer Java's built-in glob matching against files discovered under the configured source directory so the implementation stays simple and dependency-free.

The current implementation exposes `formats` and `sourceDirectory`, while GH-3 asks for `format` and nested `sourceFiles`. Replace the current public configuration model instead of keeping it as a compatibility layer so the plugin becomes a clear drop-in replacement for the jeluard plugin.

## Task List

- [ ] Create and checkout a new Git branch `feat/3-support-jeluard-plugin-configuration-as-drop-in-replacement`

### Requirements And Design

- [x] Update `doc/spec/system_requirements.md` to cover legacy-compatible source file selection, singular output format configuration, and the expected meaning of `outputDirectory` for replacement scenarios.
- [x] Stop and ask user for a review of the system requirements
- [x] Update `doc/spec/design.md` with runtime design for nested `sourceFiles` binding, include-glob filtering, and singular `format` handling; forward unchanged output-directory behavior where no extra design detail is needed.
- [x] Stop and ask user for a review of the design

### Implementation

- [x] Refactor `PlantUmlMojo` parameter binding to accept `sourceFiles.directory`, `sourceFiles.includes.include`, `format`, and `outputDirectory`, and remove the current `formats`-based public configuration model.
- [x] Implement source file discovery that scans the configured source directory recursively, keeps established PlantUML suffixes, and filters matches with the configured include globs.
- [x] Keep rendering behavior aligned with the selected single output format and configured output directory without changing the dynamic PlantUML dependency approach.
- [x] Add or update user-facing documentation for the replacement configuration model; create the documentation file if the repository still has no existing user guide entry point.

### Verification

- [x] Extend unit tests for singular format normalization, invalid format rejection, and source-file selection validation.
- [x] Add an integration test project that uses a legacy-style POM snippet and verifies selected source files, generated format, and configured output location.
- [x] Keep the OpenFastTrace trace clean.
- [x] Keep `mvn verify` and the Maven Invoker integration tests green.

### Version and Changelog Update

- [x] Raise the version to 0.2.0 (feature release)
- [x] Create or update the changelog entry for 0.2.0
