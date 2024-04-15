package com.senzing.g2.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.senzing.g2.engine.RepositoryManager.Configuration;

import static com.senzing.io.IOUtilities.*;
import static org.junit.jupiter.api.Assertions.fail;

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
     * The class-wide repo directory.
     */
    private File repoDirectory = null;

    /**
     * Whether or not the repository has been created.
     */
    private boolean repoCreated = false;

    /**
     * Protected default constructor.
     */
    protected AbstractTest() {
        this(null);
    }

    /**
     * Protected constructor allowing the derived class to specify the
     * location for the entity respository.
     *
     * @param repoDirectory The directory in which to include the entity
     *                      repository.
     */
    protected AbstractTest(File repoDirectory) {
        if (repoDirectory == null) {
            repoDirectory = createTestRepoDirectory(this.getClass());
        }
        this.repoDirectory = repoDirectory;
    }

    /**
     * Signals the beginning of the current test suite.
     *
     * @return <tt>true</tt> if replaying previous results and <tt>false</tt>
     *         if using the live API.
     */
    protected void beginTests() {
        // do nothing for now
    }

    /**
     * Signals the end of the current test suite.
     */
    protected void endTests() {
        // do nothing for now
    }

    /**
     * Returns the module name with which to initialize the server.  By default
     * this returns <tt>"Test API Server"</tt>.  Override to use a different
     * module name.
     *
     * param suffix The optional suffix to append to the module name.
     *
     * @return The module name with which to initialize the server.
     */
    protected String getModuleName(String suffix) {
        if (suffix != null && suffix.trim().length() > 0) {
            suffix = " - " + suffix.trim();
        } else {
            suffix = "";
        }
        return this.getClass().getSimpleName() + suffix;
    }

    /**
     * Returns the contents of the JSON init file from the default
     * repository directory.
     * 
     * @reutrn The contents of the JSON init file as a {@link String}
     */
    protected String getRepoSettings() {
        return this.readRepoSettingsFile(this.getRepositoryDirectory());
    }

    /**
     * Returns the contents of the JSON init file as a {@link String}.
     *
     * @param repoDirectory The {@link File} representing the directory
     *                      of the test repository.
     * 
     * @return The contents of the JSON init file as a {@link String}.
     */
    protected String readRepoSettingsFile(File repoDirectory) {
        try {
            File initJsonFile = new File(repoDirectory, "g2-init.json");

            return readTextFileAsString(initJsonFile, "UTF-8").trim();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a temp repository directory for the test class.
     *
     * @return The {@link File} representing the directory.
    */
    protected File createTestRepoDirectory() {
        return createTestRepoDirectory(this.getClass(), null);
    }

    /**
     * Creates a temp repository directory for a specific test.
     *
     * @param testName The name of the test that the directory will be used for.
     *
     * @return The {@link File} representing the directory.
    */
    protected File createTestRepoDirectory(String testName) {
        return createTestRepoDirectory(this.getClass(), testName);
    }

    /**
     * Creates a temp repository directory for a specific test.
     *
     * @param c The {@link Class} for which the directory is being created.
     *
     * @return The {@link File} representing the directory.
    */
    protected static File createTestRepoDirectory(Class<?> c) {
        return createTestRepoDirectory(c, null);
    }

    /**
     * Creates a temp repository directory for a specific test.
     *
     * @param c The {@link Class} for which the directory is being created.
     * 
     * @param testName The name of the test that the directory will be used for.
     *
     * @return The {@link File} representing the directory.
    */
    protected static File createTestRepoDirectory(Class<?> c, String testName) {
        String prefix = "sz-repo-" + c.getSimpleName() + "-" 
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
     * Returns the {@link File} identifying the repository directory used for
     * the test.  This can be specified in the constructor, but if not specified
     * is a newly created temporary directory.
     * 
     * @return The {@link File} identifying the repository directory used for
     *         the test.
     */
    protected File getRepositoryDirectory() {
        return this.repoDirectory;
    }
    
    /**
     * This method can typically be called from a method annotated with
     * "@BeforeAll".  It will create a Senzing entity repository and
     * initialize and start the Senzing API Server.
     */
    protected void initializeTestEnvironment() {
        this.initializeTestEnvironment(false);
    }

    /**
     * This method can typically be called from a method annotated with
     * "@BeforeAll".  It will create a Senzing entity repository and
     * initialize and start the Senzing API Server.
     * 
     * @param excludeConfig <code>true</code> if the default configuration
     *                      should be excluded from the repository, and
     *                      <code>false</code> if it should be included.
     */
    protected void initializeTestEnvironment(boolean excludeConfig) {
        String moduleName = this.getModuleName("RepoMgr (create)");
        RepositoryManager.setThreadModuleName(moduleName);
        boolean concluded = false;
        try {
          Configuration config = RepositoryManager.createRepo(
              this.getRepositoryDirectory(), excludeConfig, true);
          this.repoCreated = true;

          this.prepareRepository();
    
          RepositoryManager.conclude();
          concluded = true;
              
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (Error e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
        e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!concluded) RepositoryManager.conclude();
            RepositoryManager.clearThreadModuleName();
        }
    }

    /**
     * This method can typically be called from a method annotated with
     * "@AfterAll".  It will delete the entity repository that was created
     * for the tests if there are no test failures recorded via 
     * {@link #incrementFailureCount()}.
     */
    protected void teardownTestEnvironment() {
        int failureCount = this.getFailureCount();
        teardownTestEnvironment((failureCount == 0));
    }

    /**
     * This method can typically be called from a method annotated with
     * "@AfterAll".  It will optionally delete the entity repository that
     * was created for the tests.
     *
     * @param deleteRepository <tt>true</tt> if the test repository should be
     *                         deleted, otherwise <tt>false</tt>
     */
    protected void teardownTestEnvironment(boolean deleteRepository) {
        String preserveProp = System.getProperty("senzing.test.preserve.repos");
        if (preserveProp != null) preserveProp = preserveProp.trim().toLowerCase();
        boolean preserve = (preserveProp != null && preserveProp.equals("true"));

        // cleanup the repo directory
        if (this.repoCreated && deleteRepository && !preserve) {
            deleteRepository(this.repoDirectory);
        }
    }

    /**
     * Deletes the repository at the specified repository directory.
     * 
     * @param repoDirectory The repository directory to delete.
     */
    protected static void deleteRepository(File repoDirectory) {
        if (repoDirectory.exists() && repoDirectory.isDirectory()) {
            try {
                // delete the repository
                Files.walk(repoDirectory.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Override this function to prepare the repository by configuring
     * data sources or loading records.  By default this function does nothing.
     * The repository directory can be obtained via {@link
     * #getRepositoryDirectory()}.
     */
    protected void prepareRepository() {
        // do nothing
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

      /**
   * Validates the json data and ensures the expected JSON property keys are
   * present and that no unexpected keys are present.
   *
   * @param jsonData      The json data to validate.
   * @param expectedKeys The zero or more expected property keys.
   */
  public static void validateJsonDataMap(Object jsonData, String... expectedKeys)
  {
    validateJsonDataMap(null,
                            jsonData,
                            true,
                            expectedKeys);
  }

  /**
   * Validates the json data and ensures the expected JSON property keys are
   * present and that no unexpected keys are present.
   *
   * @param testInfo     Additional test information to be logged with failures.
   * @param jsonData      The json data to validate.
   * @param expectedKeys The zero or more expected property keys.
   */
  public static void validateJsonDataMap(String    testInfo,
                                         Object    jsonData,
                                         String... expectedKeys)
  {
    validateJsonDataMap(testInfo, jsonData, true, expectedKeys);
  }

  /**
   * Validates the json data and ensures the expected JSON property keys are
   * present and that, optionally, no unexpected keys are present.
   *
   * @param jsonData      The json data to validate.
   * @param strict       Whether or not property keys other than those specified are
   *                     allowed to be present.
   * @param expectedKeys The zero or more expected property keys -- these are
   *                     either a minimum or exact set depending on the
   *                     <tt>strict</tt> parameter.
   */
  public static void validateJsonDataMap(Object    jsonData,
                                         boolean   strict,
                                         String... expectedKeys)
  {
    validateJsonDataMap(null, jsonData, strict, expectedKeys);
  }

  /**
   * Validates the json data and ensures the expected JSON property keys are
   * present and that, optionally, no unexpected keys are present.
   *
   *
   * @param testInfo     Additional test information to be logged with failures.
   * @param jsonData      The json data to validate.
   * @param strict       Whether or not property keys other than those specified are
   *                     allowed to be present.
   * @param expectedKeys The zero or more expected property keys -- these are
   *                     either a minimum or exact set depending on the
   *                     <tt>strict</tt> parameter.
   */
  public static void validateJsonDataMap(String    testInfo,
                                         Object    jsonData,
                                         boolean   strict,
                                         String... expectedKeys)
  {
    String suffix = (testInfo != null && testInfo.trim().length() > 0)
        ? " ( " + testInfo + " )" : "";

    if (jsonData == null) {
      fail("Expected json data but got null value" + suffix);
    }

    if (!(jsonData instanceof Map)) {
      fail("Raw data is not a JSON object: " + jsonData + suffix);
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> map = (Map<String, Object>) jsonData;
    Set<String> expectedKeySet = new HashSet<>();
    Set<String> actualKeySet = map.keySet();
    for (String key : expectedKeys) {
      expectedKeySet.add(key);
      if (!actualKeySet.contains(key)) {
        fail("JSON property missing from json data: " + key + " / " + map
                 + suffix);
      }
    }
    if (strict && expectedKeySet.size() != actualKeySet.size()) {
      Set<String> extraKeySet = new HashSet<>(actualKeySet);
      extraKeySet.removeAll(expectedKeySet);
      fail("Unexpected JSON properties in json data: " + extraKeySet + suffix);
    }

  }


  /**
   * Validates the json data and ensures it is a collection of objects and the
   * expected JSON property keys are present in the array objects and that no
   * unexpected keys are present.
   *
   * @param jsonData      The json data to validate.
   * @param expectedKeys The zero or more expected property keys.
   */
  public static void validateJsonDataMapArray(Object     jsonData,
                                              String...  expectedKeys)
  {
    validateJsonDataMapArray(null, jsonData, true, expectedKeys);
  }

  /**
   * Validates the json data and ensures it is a collection of objects and the
   * expected JSON property keys are present in the array objects and that no
   * unexpected keys are present.
   *
   * @param testInfo     Additional test information to be logged with failures.
   * @param jsonData      The json data to validate.
   * @param expectedKeys The zero or more expected property keys.
   */
  public static void validateJsonDataMapArray(String     testInfo,
                                              Object     jsonData,
                                              String...  expectedKeys)
  {
    validateJsonDataMapArray(testInfo, jsonData, true, expectedKeys);
  }

  /**
   * Validates the json data and ensures it is a collection of objects and the
   * expected JSON property keys are present in the array objects and that,
   * optionally, no unexpected keys are present.
   *
   * @param jsonData      The json data to validate.
   * @param strict       Whether or not property keys other than those specified are
   *                     allowed to be present.
   * @param expectedKeys The zero or more expected property keys for the array
   *                     objects -- these are either a minimum or exact set
   *                     depending on the <tt>strict</tt> parameter.
   */
  public static void validateJsonDataMapArray(Object     jsonData,
                                             boolean    strict,
                                             String...  expectedKeys)
  {
    validateJsonDataMapArray(null, jsonData, strict, expectedKeys);
  }

  /**
   * Validates the json data and ensures it is a collection of objects and the
   * expected JSON property keys are present in the array objects and that,
   * optionally, no unexpected keys are present.
   *
   * @param testInfo     Additional test information to be logged with failures.
   * @param jsonData      The json data to validate.
   * @param strict       Whether or not property keys other than those specified are
   *                     allowed to be present.
   * @param expectedKeys The zero or more expected property keys for the array
   *                     objects -- these are either a minimum or exact set
   *                     depending on the <tt>strict</tt> parameter.
   */
  public static void validateJsonDataMapArray(String     testInfo,
                                              Object     jsonData,
                                              boolean    strict,
                                              String...  expectedKeys)
  {
    String suffix = (testInfo != null && testInfo.trim().length() > 0)
        ? " ( " + testInfo + " )" : "";

    if (jsonData == null) {
      fail("Expected json data but got null value" + suffix);
    }

    if (!(jsonData instanceof Collection)) {
      fail("Raw data is not a JSON array: " + jsonData + suffix);
    }

    Collection<Object> collection = (Collection<Object>) jsonData;
    Set<String> expectedKeySet = new HashSet<>();
    for (String key : expectedKeys) {
      expectedKeySet.add(key);
    }

    for (Object obj : collection) {
      if (!(obj instanceof Map)) {
        fail("Raw data is not a JSON array of JSON objects: " + jsonData + suffix);
      }

      Map<String, Object> map = (Map<String, Object>) obj;

      Set<String> actualKeySet = map.keySet();
      for (String key : expectedKeySet) {
        if (!actualKeySet.contains(key)) {
          fail("JSON property missing from json data array element: "
                   + key + " / " + map + suffix);
        }
      }
      if (strict && expectedKeySet.size() != actualKeySet.size()) {
        Set<String> extraKeySet = new HashSet<>(actualKeySet);
        extraKeySet.removeAll(expectedKeySet);
        fail("Unexpected JSON properties in json data: " + extraKeySet + suffix);
      }
    }
  }

}
