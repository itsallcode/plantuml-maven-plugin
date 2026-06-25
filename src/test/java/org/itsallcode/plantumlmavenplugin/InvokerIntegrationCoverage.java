package org.itsallcode.plantumlmavenplugin;

/**
 * Trace markers for invoker-based integration tests under {@code src/it}, which OpenFastTrace does not scan directly.
 */
final class InvokerIntegrationCoverage {
    private InvokerIntegrationCoverage() {
    }

    // [itest->dsn~select-source-files~1]
    // [itest->dsn~preserve-relative-output-paths~1]
    @SuppressWarnings("unused")
    private static final String INVOKER_TESTS = "legacy-source-selection-it";
}
