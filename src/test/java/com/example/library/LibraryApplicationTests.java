package com.example.library;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The LibraryApplicationTests class is a Spring Boot application runner
 * implementing CommandLineRunner. It scans for tests in the specified package
 * using JUnit Platform, executes them, and prints a summary of the test
 * execution.
 */
@SpringBootApplication
public class LibraryApplicationTests implements CommandLineRunner {

    /**
     * The main method of the application. It launches the Spring Boot
     * application.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(LibraryApplicationTests.class, args);
    }

    /**
     * This method is invoked upon application startup and is responsible for
     * executing the tests, collecting their results, and printing a summary of
     * the test execution.
     *
     * @param args Command-line arguments
     * @throws Exception If an exception occurs during test execution
     */
    @Override
    public void run(String... args) throws Exception {
        // Create a TestPlan that scans for tests in the specified package
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage("com.example.library.service"))
                .build();
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        // Get the test execution summary and print it
        TestExecutionSummary summary = listener.getSummary();
        System.out.println(summary);

        // Print the test execution results
        System.out.println("Test execution summary:");
        System.out.println(" - Tests found: " + summary.getTestsFoundCount());
        System.out.println(" - Tests successful: " + summary.getTestsSucceededCount());
        System.out.println(" - Tests failed: " + summary.getTestsFailedCount());
        System.out.println(" - Tests aborted: " + summary.getTestsAbortedCount());

        // Calculate and print the duration of the test execution
        long startTime = summary.getTimeStarted();
        long finishTime = summary.getTimeFinished();
        long duration = finishTime - startTime;
        System.out.println(" - Time started: " + startTime);
        System.out.println(" - Time finished: " + finishTime);
        System.out.println(" - Duration: " + duration + " ms");
    }
}
