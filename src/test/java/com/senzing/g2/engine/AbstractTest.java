package com.senzing.g2.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.senzing.io.IOUtilities.*;

/**
 * 
 */
public abstract class AbstractTest {
    /**
     * The number of tests that failed for this instance.
     */
    private int failureCount = 0;

    /**
     * The number of tests that succeeded for this instance.
     */
    private int successCount = 0;

    /**
     * The time of the last progress log.
     */
    private long progressLogTimestamp = -1L;

    /**
     * Returns the contents of the JSON init file as a {@link String}.
     *
     * @param repoDirectory The {@link File} representing the directory
     *                      of the test repository.
     * 
     * @return The contents of the JSON init file as a {@link String}.
     */
    protected String readInitJsonFile(File repoDirectory) {
        try {
            File initJsonFile = new File(repoDirectory, "g2-init.json");

            return readTextFileAsString(initJsonFile, "UTF-8").trim();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a temp repository directory.
     *
     * @param testName The name of the test that the directory will be used for.
     *
     * @return The {@link File} representing the directory.
    */
    protected File createTestRepoDirectory(String testName) {
        String prefix = "sz-repo-" + this.getClass().getSimpleName() + "-" 
            + (testName == null ? "" : (testName + "-"));
        return doCreateTestRepoDirectory(prefix);
    }

    /**
     * Creates a temp repository directory.
     *
     * @param prefix The directory name prefix for the temp repo directory.
     *
     * @return The {@link File} representing the directory.
     */
    protected static File doCreateTestRepoDirectory(String prefix) {
        try {
        File    targetDir     = null;
        String  buildDirProp  = System.getProperty("project.build.directory");
        if (buildDirProp != null) {
            targetDir = new File(buildDirProp);
        } else {
            String workingDir = System.getProperty("user.dir");
            File currentDir = new File(workingDir);
            targetDir = new File(currentDir, "target");
        }

        boolean forceTempRepos = false;
        String prop = System.getProperty("senzing.test.tmp.repos");
        if (prop != null && prop.toLowerCase().trim().equals("true")) {
            forceTempRepos = true;
        }

        // check if we have a target directory (i.e.: maven build)
        if (forceTempRepos || !targetDir.exists()) {
            // if no target directory then use the temp directory
            return Files.createTempDirectory(prefix).toFile();
        }

        // if we have a target directory then use it as a parent for our test repo
        File testRepoDir = new File(targetDir, "test-repos");
        if (!testRepoDir.exists()) {
            testRepoDir.mkdirs();
        }

        // create a temp directory inside the test repo directory
        return Files.createTempDirectory(testRepoDir.toPath(), prefix).toFile();

        } catch (RuntimeException e) {
        throw e;
        } catch (Exception e) {
        throw new RuntimeException(e);
        }
    }

    /**
     * Increments the failure count and returns the new failure count.
     * @return The new failure count.
     */
    protected int incrementFailureCount() {
        this.failureCount++;
        this.conditionallyLogCounts(false);
        return this.failureCount;
    }

    /**
     * Increments the success count and returns the new success count.
     * @return The new success count.
     */
    protected int incrementSuccessCount() {
        this.successCount++;
        this.conditionallyLogCounts(false);
        return this.successCount;
    }

    /**
     * Conditionally logs the progress of the tests.
     *
     * @param complete <tt>true</tt> if tests are complete for this class,
     *                 otherwise <tt>false</tt>.
     */
    protected void conditionallyLogCounts(boolean complete) {
        int successCount = this.getSuccessCount();
        int failureCount = this.getFailureCount();

        long now = System.currentTimeMillis();
        long lapse = (this.progressLogTimestamp > 0L)
            ? (now - this.progressLogTimestamp) : 0L;

        if (complete || (lapse > 30000L)) {
        System.out.println(this.getClass().getSimpleName()
                            + (complete ? " Complete: " : " Progress: ")
                            + successCount + " (succeeded) / " + failureCount
                            + " (failed)");
        this.progressLogTimestamp = now;
        }
        if (complete) {
        System.out.println();
        }
        if (this.progressLogTimestamp < 0L) {
        this.progressLogTimestamp = now;
        }
    }

    /**
     * Returns the current failure count.  The failure count is incremented via
     * {@link #incrementFailureCount()}.
     *
     * @return The current success count.
     */
    protected int getFailureCount() {
        return this.failureCount;
    }

    /**
     * Returns the current success count.  The success count is incremented via
     * {@link #incrementSuccessCount()}.
     *
     * @return The current success count.
     */
    protected int getSuccessCount() {
        return this.successCount;
    }

    /**
     * Wrapper function for performing a test that will first check if
     * the native API is available via {@link #assumeNativeApiAvailable()} and
     * then record a success or failure.
     *
     * @param testFunction The {@link Runnable} to execute.
     */
    protected void performTest(Runnable testFunction) {
        boolean success = false;
        try {
        testFunction.run();
        success = true;

        } catch (Error|RuntimeException e) {
        e.printStackTrace();
        System.err.flush();
        if ("true".equals(System.getProperty("com.senzing.api.test.fastFail"))) {
            try {
            Thread.sleep(5000L);
            } catch (InterruptedException ignore) {
            // do nothing
            }
            System.exit(1);
        }
        throw e;
        } finally {
        if (!success) this.incrementFailureCount();
        else this.incrementSuccessCount();
        }
    }
}
