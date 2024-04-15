package com.senzing.g2.engine;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import static com.senzing.g2.engine.SzCoreEnvironment.*;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzCoreEnvironmentTest extends AbstractTest {
 
    @BeforeAll public void initializeEnvironment() {
        this.beginTests();
        this.initializeTestEnvironment();
    }

    @AfterAll public void teardownEnvironment() {
        try {
            this.teardownTestEnvironment();
            this.conditionallyLogCounts(true);
        } finally {
            this.endTests();
        }
    }

    @Test
    void testNewDefaultBuilder() {
        this.performTest(() -> {
            String envSettings = System.getenv(
                SzCoreEnvironment.SETTINGS_ENVIRONMENT_VARIABLE);
    
            if (envSettings != null && envSettings.trim().length() == 0) {
                envSettings = null;
            } else if (envSettings != null) {
                envSettings = envSettings.trim();
            }
    
            String defaultSettings = (envSettings == null) 
                ? SzCoreEnvironment.BOOTSTRAP_SETTINGS : envSettings;
    
            SzCoreEnvironment env  = null;
            
            try {
                env  = SzCoreEnvironment.newBuilder().build();
    
                assertEquals(env.getInstanceName(), SzCoreEnvironment.DEFAULT_INSTANCE_NAME,
                    "Environment instance name is not default instance name");
                assertEquals(env.getSettings(), defaultSettings,
                    "Environment settings are not bootstrap settings");
                assertFalse(env.isVerboseLogging(),
                    "Environment verbose logging did not default to false");
                assertNull(env.getConfigId(), "Environment config ID is not null");
    
            } finally {
                if (env != null) env.destroy();
            }    
        });
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false})
    void testNewCustomBuilder(boolean verboseLogging) {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreEnvironment env  = null;
            
            try {
                env  = SzCoreEnvironment.newBuilder()
                                        .instanceName("Custom Instance")
                                        .settings(settings)
                                        .verboseLogging(verboseLogging)
                                        .build();

                assertEquals(env.getInstanceName(), "Custom Instance",
                        "Environment instance name is not as expected");
                assertEquals(env.getSettings(), settings,
                        "Environment settings are not as expected");
                assertEquals(env.isVerboseLogging(), verboseLogging,
                        "Environment verbose logging did not default to false");
                assertNull(env.getConfigId(), "Environment config ID is not null");
    
            } finally {
                if (env != null) env.destroy();
            }    
        });
    }

    @Test
    void testSingletonViolation() {
        this.performTest(() -> {
            SzCoreEnvironment env1 = null;
            SzCoreEnvironment env2 = null;
            try {
                env1 = SzCoreEnvironment.newBuilder().build();
    
                try {
                    env2 = SzCoreEnvironment.newBuilder().settings(BOOTSTRAP_SETTINGS).build();
        
                    // if we get here then we failed
                    fail("Was able to construct a second factory when first was not yet destroyed");
        
                } catch (IllegalStateException expected) {
                    // this exception was expected
                } finally {
                    if (env2 != null) {
                        env2.destroy();
                    }
                }
            } finally {
                if (env1 != null) {
                    env1.destroy();
                }
            }    
        });
    }

    @Test
    void testSingletonAdherence() {
        this.performTest(() -> {
            SzCoreEnvironment env1 = null;
            SzCoreEnvironment env2 = null;
            try {
                env1 = SzCoreEnvironment.newBuilder().instanceName("Instance 1").build();
    
                env1.destroy();
                env1 = null;
    
                env2 = SzCoreEnvironment.newBuilder().instanceName("Instance 2").settings(BOOTSTRAP_SETTINGS).build();
    
                env2.destroy();
                env2 = null;
    
            } finally {
                if (env1 != null) {
                    env1.destroy();
                }
                if (env2 != null) {
                    env2.destroy();
                }
            }    
        });
    }

    @Test
    void testDestroy() {
        this.performTest(() -> {
            SzCoreEnvironment env1 = null;
            SzCoreEnvironment env2 = null;
            try {
                // get the first environment
                env1 = SzCoreEnvironment.newBuilder().instanceName("Instance 1").build();
    
                // ensure it is active
                try {
                    env1.ensureActive();
                } catch (Exception e) {
                    fail("First Environment instance is not active.", e);
                }
    
                // destroy the first environment
                env1.destroy();
    
                // check it is now inactive
                try {
                    env1.ensureActive();
                    fail("First Environment instance is still active.");
    
                } catch (Exception expected) {
                    // do nothing
                } finally {
                    // clear the env1 reference
                    env1 = null;
                }
    
                // create a second environment instance
                env2 = SzCoreEnvironment.newBuilder().instanceName("Instance 2").settings(BOOTSTRAP_SETTINGS).build();
    
                // ensure it is active
                try {
                    env2.ensureActive();
                } catch (Exception e) {
                    fail("Second Environment instance is not active.", e);
                }
    
                // destroy the second environment
                env2.destroy();
    
                // check it is now inactive
                try {
                    env2.ensureActive();
                    fail("Second Environment instance is still active.");
    
                } catch (Exception expected) {
                    // do nothing
                } finally {
                    // clear the env2 reference
                    env2 = null;
                }
    
                env2 = null;
    
            } finally {
                if (env1 != null) {
                    env1.destroy();
                }
                if (env2 != null) {
                    env2.destroy();
                }
            }    
        });
    }


    @ParameterizedTest
    @CsvSource({"1, Foo", "2, Bar", "3, Phoo", "4, Phoox"})
    void testExecute(int threadCount, String expected) {
        SzCoreEnvironment env  = null;

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            threadCount, threadCount, 10L, SECONDS, new LinkedBlockingQueue<>());

        List<Future<String>> futures = new ArrayList<>(threadCount);
        try {
            env  = SzCoreEnvironment.newBuilder().build();

            final SzCoreEnvironment prov = env;

            // loop through the threads
            for (int index = 0; index < threadCount; index++) {
                Future<String> future = threadPool.submit(() -> {
                    return prov.execute(() -> {
                        return expected;
                    });
                });
                futures.add(future);
            }

            // loop through the futures
            for (Future<String> future : futures) {
                try {
                    String actual = future.get();
                    assertEquals(expected, actual, "Unexpected result from execute()");
                } catch (Exception e) {
                    fail("Failed execute with exception", e);
                }
            }


        } finally {
            if (env != null) {
                env.destroy();
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Foo", "Bar", "Phoo", "Phoox"})
    void testExecuteFail(String expected) {
        SzCoreEnvironment env  = null;
        try {
            env  = SzCoreEnvironment.newBuilder().build();

            try {
               env.execute(() -> {
                    throw new SzException(expected);
               });

               fail("Expected SzException was not thrown");

            } catch (SzException e) {
                assertEquals(expected, e.getMessage(), "Unexpected exception messasge");

            } catch (Exception e) {
                fail("Failed execute with exception", e);
            }

        } finally {
            if (env != null) {
                env.destroy();
            }
        }
    }

    @ParameterizedTest
    @CsvSource({"1, Foo", "2, Bar", "3, Phoo", "4, Phoox"})
    void testGetExecutingCount(int threadCount, String expected) {
        int executeCount = threadCount * 3;

        final Object[] monitors = new Object[executeCount];
        for (int index = 0; index < executeCount; index++) {
            monitors[index] = new Object();
        }
        SzCoreEnvironment env  = null;
        try {
            env  = SzCoreEnvironment.newBuilder().instanceName(expected).build();

            final Thread[]      threads     = new Thread[executeCount];
            final String[]      results     = new String[executeCount];
            final Exception[]   failures    = new Exception[executeCount];

            for (int index = 0; index < executeCount; index++) {
                final SzCoreEnvironment prov = env;
                final int threadIndex = index;
                threads[index] = new Thread() { 
                    public void run() {
                        try {
                            String actual = prov.execute(() -> {
                                Object monitor = monitors[threadIndex];
                                synchronized (monitor) {
                                    monitor.notifyAll();
                                    monitor.wait();
                                }
                                return expected + "-" + threadIndex;
                            });
                            results[threadIndex]    = actual;
                            failures[threadIndex]   = null;
             
                         } catch (Exception e) {
                            results[threadIndex]    = null;
                            failures[threadIndex]   = e;
                         }
                    }
                };
            }
            int prevExecutingCount = 0;
            for (int index = 0; index < executeCount; index++) {
                Object monitor = monitors[index];

                synchronized (monitor) {

                    threads[index].start();
                    try {
                        monitor.wait();
                    } catch (InterruptedException ignore) {
                        // do nothing
                    }
                }
                int executingCount = env.getExecutingCount();
                assertTrue(executingCount > 0, "Executing count is zero");
                assertTrue(executingCount > prevExecutingCount, 
                        "Executing count (" + executingCount + ") decremented from previous ("
                        + prevExecutingCount + ")");
                prevExecutingCount = executingCount;
            }

            for (int index = 0; index < executeCount; index++) {
                try {
                    Object monitor = monitors[index];
                    synchronized (monitor) {
                        monitor.notifyAll();
                    }
                    threads[index].join();
                } catch (InterruptedException e) {
                    fail("Interrupted while joining threads");
                }
                int executingCount = env.getExecutingCount();
                assertTrue(executingCount >= 0, "Executing count is negative");
                assertTrue(executingCount < prevExecutingCount, 
                        "Executing count (" + executingCount + ") incremented from previous ("
                        + prevExecutingCount + ")");
                prevExecutingCount = executingCount;
            }
            
            // check the basics
            for (int index = 0; index < executeCount; index++) {
                assertEquals(expected + "-" + index, results[index],
                            "At least one thread returned an unexpected result");
                assertNull(failures[index], 
                                    "At least one thread threw an exception");
            }
            
        } finally {
            for (int index = 0; index < executeCount; index++) {
                Object monitor = monitors[index];
                synchronized (monitor) {
                    monitor.notifyAll();
                }
            }
            if (env != null) {
                env.destroy();
            }
        }
    }

    @Test
    void testGetActiveInstance() {
        this.performTest(() -> {
            SzCoreEnvironment env1 = null;
            SzCoreEnvironment env2 = null;
            try {
                // get the first environment
                env1 = SzCoreEnvironment.newBuilder().instanceName("Instance 1").build();
    
                SzCoreEnvironment active = SzCoreEnvironment.getActiveInstance();

                assertNotNull(active, "No active instance found when it should have been: " 
                              + env1.getInstanceName());
                assertSame(env1, active,
                            "Active instance was not as expected: " 
                            + ((active == null) ? null : active.getInstanceName()));
    
                // destroy the first environment
                env1.destroy();
    
                active = SzCoreEnvironment.getActiveInstance();
                assertNull(active,
                           "Active instance found when there should be none: " 
                           + ((active == null) ? "" : active.getInstanceName()));
                            
                // create a second Environment instance
                env2 = SzCoreEnvironment.newBuilder()
                    .instanceName("Instance 2").settings(BOOTSTRAP_SETTINGS).build();
    
                active = SzCoreEnvironment.getActiveInstance();
                assertNotNull(active, "No active instance found when it should have been: " 
                              + env2.getInstanceName());
                assertSame(env2, active,
                           "Active instance was not as expected: " 
                           + ((active == null) ? null : active.getInstanceName()));
                    
                // destroy the second environment
                env2.destroy();
    
                active = SzCoreEnvironment.getActiveInstance();
                assertNull(active,
                    "Active instance found when there should be none: " 
                    + ((active == null) ? null : active.getInstanceName()));
                
                env2 = null;
    
            } finally {
                if (env1 != null) {
                    env1.destroy();
                }
                if (env2 != null) {
                    env2.destroy();
                }
            }    
        });
 
    }

    @Test
    void testGetConfig() {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreEnvironment env  = null;
            
            try {
                env  = SzCoreEnvironment.newBuilder()
                                         .instanceName("GetConfig Instance")
                                         .settings(settings)
                                         .verboseLogging(false)
                                         .build();

                SzConfig config1 = env.getConfig();
                SzConfig config2 = env.getConfig();

                assertNotNull(config1, "SzConfig was null");
                assertSame(config1, config2, "SzConfig not returning the same object");
                assertInstanceOf(SzCoreConfig.class, config1,
                                "SzConfig instance is not an instance of SzCoreConfig: "
                                + config1.getClass().getName());
                assertFalse(((SzCoreConfig) config1).isDestroyed(),
                            "SzConfig instance reporting that it is destroyed");

                env.destroy();
                env  = null;

                assertTrue(((SzCoreConfig) config1).isDestroyed(),
                            "SzConfig instance reporting that it is NOT destroyed");

            } catch (SzException e) {
                fail("Got SzException during test", e);

            } finally {
                if (env != null) env.destroy();
            }    
        });

    }

    @Test
    void testGetConfigManager() {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreEnvironment env  = null;
            
            try {
                env  = SzCoreEnvironment.newBuilder()
                                         .instanceName("GetConfigManager Instance")
                                         .settings(settings)
                                         .verboseLogging(false)
                                         .build();

                SzConfigManager configMgr1 = env.getConfigManager();
                SzConfigManager configMgr2 = env.getConfigManager();

                assertNotNull(configMgr1, "SzConfigManager was null");
                assertSame(configMgr1, configMgr2, "SzConfigManager not returning the same object");
                assertInstanceOf(SzCoreConfigManager.class, configMgr1,
                                "SzConfigManager instance is not an instance of SzCoreConfigManager: "
                                + configMgr1.getClass().getName());
                assertFalse(((SzCoreConfigManager) configMgr1).isDestroyed(),
                            "SzConfigManager instance reporting that it is destroyed");

                env.destroy();
                env  = null;

                assertTrue(((SzCoreConfigManager) configMgr1).isDestroyed(),
                            "SzConfigManager instance reporting that it is NOT destroyed");

            } catch (SzException e) {
                fail("Got SzException during test", e);

            } finally {
                if (env != null) env.destroy();
            }    
        });
    }

    @Test
    void testGetDiagnostic() {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreEnvironment env  = null;
            
            try {
                env  = SzCoreEnvironment.newBuilder()
                                         .instanceName("GetDiagnostic Instance")
                                         .settings(settings)
                                         .verboseLogging(false)
                                         .build();

                SzDiagnostic diagnostic1 = env.getDiagnostic();
                SzDiagnostic diagnostic2 = env.getDiagnostic();

                assertNotNull(diagnostic1, "SzDiagnostic was null");
                assertSame(diagnostic1, diagnostic2, "SzDiagnostic not returning the same object");
                assertInstanceOf(SzCoreDiagnostic.class, diagnostic1,
                                "SzDiagnostic instance is not an instance of SzCoreDiagnostic: "
                                + diagnostic1.getClass().getName());
                assertFalse(((SzCoreDiagnostic) diagnostic1).isDestroyed(),
                            "SzDiagnostic instance reporting that it is destroyed");

                env.destroy();
                env  = null;

                assertTrue(((SzCoreDiagnostic) diagnostic1).isDestroyed(),
                            "SzDiagnostic instance reporting that it is NOT destroyed");

            } catch (SzException e) {
                fail("Got SzException during test", e);

            } finally {
                if (env != null) env.destroy();
            }    
        });
    }

    @Test
    void testGetEngine() {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreEnvironment env  = null;
            
            try {
                env  = SzCoreEnvironment.newBuilder()
                                         .instanceName("GetEngine Instance")
                                         .settings(settings)
                                         .verboseLogging(false)
                                         .build();

                SzEngine engine1 = env.getEngine();
                SzEngine engine2 = env.getEngine();

                assertNotNull(engine1, "SzEngine was null");
                assertSame(engine1, engine2, "SzEngine not returning the same object");
                assertInstanceOf(SzCoreEngine.class, engine1,
                                "SzEngine instance is not an instance of SzCoreEngine: "
                                + engine1.getClass().getName());
                assertFalse(((SzCoreEngine) engine1).isDestroyed(),
                            "SzEngine instance reporting that it is destroyed");

                env.destroy();
                env  = null;

                assertTrue(((SzCoreEngine) engine1).isDestroyed(),
                            "SzEngine instance reporting that it is NOT destroyed");

            } catch (SzException e) {
                fail("Got SzException during test", e);

            } finally {
                if (env != null) env.destroy();
            }    
        });
    }

    @Test
    void testGetProduct() {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreEnvironment env  = null;
            
            try {
                env  = SzCoreEnvironment.newBuilder()
                                         .instanceName("GetProduct Instance")
                                         .settings(settings)
                                         .verboseLogging(false)
                                         .build();

                SzProduct product1 = env.getProduct();
                SzProduct product2 = env.getProduct();

                assertNotNull(product1, "SzProduct was null");
                assertSame(product1, product2, "SzProduct not returning the same object");
                assertInstanceOf(SzCoreProduct.class, product1,
                                "SzProduct instance is not an instance of SzCoreProduct: "
                                + product1.getClass().getName());
                assertFalse(((SzCoreProduct) product1).isDestroyed(),
                            "SzProduct instance reporting that it is destroyed");

                env.destroy();
                env  = null;

                assertTrue(((SzCoreProduct) product1).isDestroyed(),
                            "SzProduct instance reporting that it is NOT destroyed");

            } catch (SzException e) {
                fail("Got SzException during test", e);

            } finally {
                if (env != null) env.destroy();
            }    
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "Foo", "Bar", "Phoo" })
    void testGetInstanceName(String instanceName) {
        SzCoreEnvironment env  = null;
            
        try {
            env  = SzCoreEnvironment.newBuilder().instanceName(instanceName).build();

            assertEquals(instanceName, env.getInstanceName(),
                         "Instance names are not equal after building.");
        
        } finally {
            if (env != null) env.destroy();
        }
    }

    @ParameterizedTest
    @ValueSource(longs = { 10L, 12L, 0L })
    void testGetConfigId(Long configId) {
        SzCoreEnvironment env  = null;
        if (configId == 0L) configId = null;

        try {
            env  = SzCoreEnvironment.newBuilder().configId(configId).build();

            assertEquals(configId, env.getConfigId(),
                         "Config ID's are not equal after building.");
        
        } finally {
            if (env != null) env.destroy();
        }
    }

    @Test
    void testGetSettings() {
        SzCoreEnvironment env  = null;
            
        try {
            env  = SzCoreEnvironment.newBuilder().instanceName(BOOTSTRAP_SETTINGS).build();

            assertEquals(BOOTSTRAP_SETTINGS, env.getSettings(),
                         "Settings are not equal after building.");
        
        } finally {
            if (env != null) env.destroy();
        }

    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false})
    void testIsVerboseLogging(boolean verbose) {
        SzCoreEnvironment env  = null;
            
        try {
            env  = SzCoreEnvironment.newBuilder().verboseLogging(verbose).build();

            assertEquals(verbose, env.isVerboseLogging(),
                         "Verbose logging settings are not equal after building.");
        
        } finally {
            if (env != null) env.destroy();
        }
    }

    @ParameterizedTest
    @CsvSource({"1,10,Foo", "0,20,Bar", "2,30,Phoo"})
    void testHandleReturnCode(int returnCode, int errorCode, String errorMessage) {
        SzCoreEnvironment env  = null;
            
        try {
            env  = SzCoreEnvironment.newBuilder().instanceName(BOOTSTRAP_SETTINGS).build();

            try {
                env.handleReturnCode(returnCode, new NativeApi() {
                    public int getLastExceptionCode() { return errorCode; }
                    public String getLastException() { return errorMessage; }
                    public void clearLastException() { }
                });
                if (returnCode != 0) {
                    fail("The handleReturnCode() function did not throw an exception with return code: " + returnCode);
                }

            } catch (Exception e) {
                if (returnCode == 0) {
                    fail("Unexpected exception from handleReturnCode() with return code: " + returnCode, e);
                } else {
                    assertInstanceOf(SzException.class, e, "Type of exception is not as expected");
                    SzException sze = (SzException) e;
                    assertEquals(errorCode, sze.getErrorCode(), "Error code of exception is not as expected");
                    assertEquals(errorMessage, e.getMessage(), "Error message of exception is not as expected");
                }
            }
        } finally {
            if (env != null) env.destroy();
        }
    }

}
