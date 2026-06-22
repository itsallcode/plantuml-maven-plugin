# Quality Requirements

This chapter documents architecture-relevant quality requirements and technical quality goals.

User-facing acceptance scenarios are defined in [System Requirements](../system_requirements.md).

## Requirement Quality

We have the following requirement hierarchy in this project:

| Document | Level | Artifact types | Meaning | Covers |
|---------------------|-------|----------------|-------------------------------------------------------------------------|------------------------|
| System Requirements | 1 | `feat` | Top level feature | |
| | 2 | `req` | High level requirement | `feat` |
| | 3 | `scn` | Given-when-then scenario | `req` |
| Design | 4 | `dsn` | Design requirement | `req`, `scn` |
| Production Code | 5 | `impl` | Implementation | `dsn` |
| Test Code | 5 | `utest` | Unit test | `dsn` |
| | 5 | `itest` | Integration test | `dsn` |

Runtime design requirements `dsn` can only cover one scenario at a time `scn`.

## Code Quality

### Production Code Rules

#### Clean Architecture
1. Names and structure must reveal intent.
2. Code must be testable through public or package-visible seams.

#### KISS
1. Choose the simplest implementation that satisfies the requirement.
2. Avoid speculative abstractions and premature generalization.

#### DRY
1. Do not copy-paste logic.
2. Extract shared behavior when duplication would cause multiple sources of truth.

#### Explicit Code
1. Use descriptive names.
2. Prefer code that explains itself through naming and structure.
3. Comments are only allowed when they capture intent or context that the code cannot express clearly on its own.

#### OWASP
1. Validate untrusted input at boundaries.
2. Reject invalid, malformed, or unexpected input explicitly.
3. Avoid unsafe file and path handling patterns.

### Test Rules

#### DAMP
1. Optimize tests for readability over deduplication.
2. Use parameterized tests when the same behavior must be verified against multiple input/output combinations.

#### BDD
1. Tests must describe behavior in Given-When-Then form.
2. Each test must make the precondition, action, and expected outcome easy to identify.

#### Test Quality
1. Each test should verify one logical expectation.
2. Prefer known-good constants and explicit expected values.

## Dependency Policy

Minimal dependencies: only Maven and PlantUML at runtime. Any new third-party dependency requires explicit approval.

## OpenFastTrace Tracing

OpenFastTrace tracing runs as a build breaker. The trace stays clean for all artifact types.
