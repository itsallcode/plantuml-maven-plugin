# GH-5 CI build with GitHub Actions

## Goal

Establish a GitHub Actions based CI build for this repository that runs the Maven build and tests on pushes and pull requests, keeps OpenFastTrace enforcement active, and reports code coverage as far as the Maven Invoker based integration-test setup allows.

## Scope

In scope:

* Add a GitHub Actions build workflow for the existing Maven verification path.
* Publish code coverage from the CI build in a form that works with the current unit-test and integration-test setup.
* Reuse the OpenFastTrace repository's `.github` setup as a template where it fits this plugin repository.
* Document any CI-specific Maven configuration needed for stable coverage reporting.

Out of scope:

* Changing plugin runtime behavior or user-facing rendering features.
* Adding release automation, site publishing, or other GitHub workflows not needed for GH-5 acceptance criteria.
* Perfect integration-test coverage if the Maven Invoker setup cannot attribute all executed code to JaCoCo.

## Design References

* [System Requirements](../spec/system_requirements.md)
* [Quality Requirements](../spec/design/quality_requirements.md)
* [Design](../spec/design.md)

## Strategy

GH-5 is internal delivery tooling rather than a user-visible plugin feature, so the system requirements are expected to stay unchanged. The implementation should start from the OpenFastTrace `.github` template referenced in the issue, then trim it down to the workflows and repository automation that are relevant for this smaller Maven plugin project.

For coverage reporting, prefer the simplest Maven-based approach that works with the existing unit tests, Maven Invoker integration tests, and OpenFastTrace trace build breaker. If integration-test execution cannot be fully captured by JaCoCo, document that limitation and still publish the best available coverage signal from CI.

## Task List

- [x] Create and checkout a new Git branch `refactor/5-ci-build`

### Requirements And Design

- [x] Confirm that `doc/spec/system_requirements.md` needs no change because GH-5 does not alter user-visible plugin behavior.
- [x] Update `doc/spec/design/quality_requirements.md` to define GitHub Actions CI as the required automation path for build, test, trace, and coverage reporting.
- [x] No update required in `doc/spec/design.md`; the CI workflow changes are sufficiently specified in `doc/spec/design/quality_requirements.md`.
- [ ] Stop and ask user for a review of the design updates.

### Implementation

- [x] Create `.github/workflows/build.yml` from the OpenFastTrace template and adapt it to this repository's actual Maven build, including unit tests, Maven Invoker integration tests, and OpenFastTrace tracing.
- [x] Add the minimal supporting `.github` files from the OpenFastTrace template that are useful here, and explicitly skip template files that are unrelated to GH-5.
- [x] Add or adjust Maven coverage configuration so CI produces a coverage artifact or uploadable report without weakening the existing build gates.
- [x] Configure CI coverage publication using the simplest repository-appropriate mechanism, and document any unavoidable gaps caused by the integration-test setup.

### Verification

- [x] Verify locally that `mvn clean verify` still passes with the CI and coverage changes in place.
- [x] Verify that the Maven build produces the coverage files expected by the GitHub Actions workflow.
- [x] Keep the OpenFastTrace trace clean.
- [x] Validate the GitHub Actions workflow YAML and confirm the workflow triggers, Java version, and Maven steps match the intended CI behavior.

### Update user documentation

- [x] Update `README.md` to mention the GitHub Actions CI build and coverage reporting, including any relevant status badge or limitations if appropriate.

### Version and Changelog Update

- [ ] Decide whether GH-5 should ship as part of the next feature release or as a standalone maintenance release, and update versioning and changelog entries only if this repository wants CI-only changes to be released immediately.
