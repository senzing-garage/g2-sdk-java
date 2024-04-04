package com.senzing.g2.engine;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

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

import static com.senzing.g2.engine.SzCoreProvider.*;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzCoreProviderTest extends AbstractTest {
 
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
                SzCoreProvider.SETTINGS_ENVIRONMENT_VARIABLE);
    
            if (envSettings != null && envSettings.trim().length() == 0) {
                envSettings = null;
            } else if (envSettings != null) {
                envSettings = envSettings.trim();
            }
    
            String defaultSettings = (envSettings == null) 
                ? SzCoreProvider.BOOTSTRAP_SETTINGS : envSettings;
    
            SzCoreProvider provider = null;
            
            try {
                provider = SzCoreProvider.newBuilder().build();
    
                assertEquals(provider.getInstanceName(), SzCoreProvider.DEFAULT_INSTANCE_NAME,
                    "Provider instance name is not default instance name");
                assertEquals(provider.getSettings(), defaultSettings,
                    "Provider settings are not bootstrap settings");
                assertFalse(provider.isVerboseLogging(),
                    "Provider verbose logging did not default to false");
                assertNull(provider.getConfigId(), "Provider config ID is not null");
    
            } finally {
                if (provider != null) provider.destroy();
            }    
        });
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false})
    void testNewCustomBuilder(boolean verboseLogging) {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreProvider provider = null;
            
            try {
                provider = SzCoreProvider.newBuilder()
                                         .instanceName("Custom Instance")
                                         .settings(settings)
                                         .verboseLogging(verboseLogging)
                                         .build();

                assertEquals(provider.getInstanceName(), "Custom Instance",
                        "Provider instance name is not as expected");
                assertEquals(provider.getSettings(), settings,
                        "Provider settings are not as expected");
                assertEquals(provider.isVerboseLogging(), verboseLogging,
                        "Provider verbose logging did not default to false");
                assertNull(provider.getConfigId(), "Provider config ID is not null");
    
            } finally {
                if (provider != null) provider.destroy();
            }    
        });
    }

    @Test
    void testSingletonViolation() {
        this.performTest(() -> {
            SzCoreProvider provider1 = null;
            SzCoreProvider provider2 = null;
            try {
                provider1 = SzCoreProvider.newBuilder().build();
    
                try {
                    provider2 = SzCoreProvider.newBuilder().settings(BOOTSTRAP_SETTINGS).build();
        
                    // if we get here then we failed
                    fail("Was able to construct a second factory when first was not yet destroyed");
        
                } catch (IllegalStateException expected) {
                    // this exception was expected
                } finally {
                    if (provider2 != null) {
                        provider2.destroy();
                    }
                }
            } finally {
                if (provider1 != null) {
                    provider1.destroy();
                }
            }    
        });
    }

    @Test
    void testSingletonAdherence() {
        this.performTest(() -> {
            SzCoreProvider provider1 = null;
            SzCoreProvider provider2 = null;
            try {
                provider1 = SzCoreProvider.newBuilder().instanceName("Instance 1").build();
    
                provider1.destroy();
                provider1 = null;
    
                provider2 = SzCoreProvider.newBuilder().instanceName("Instance 2").settings(BOOTSTRAP_SETTINGS).build();
    
                provider2.destroy();
                provider2 = null;
    
            } finally {
                if (provider1 != null) {
                    provider1.destroy();
                }
                if (provider2 != null) {
                    provider2.destroy();
                }
            }    
        });
    }

    @Test
    void testDestroy() {
        this.performTest(() -> {
            SzCoreProvider provider1 = null;
            SzCoreProvider provider2 = null;
            try {
                // get the first Provider
                provider1 = SzCoreProvider.newBuilder().instanceName("Instance 1").build();
    
                // ensure it is active
                try {
                    provider1.ensureActive();
                } catch (Exception e) {
                    fail("First Provider instance is not active.", e);
                }
    
                // destroy the first Provider
                provider1.destroy();
    
                // check it is now inactive
                try {
                    provider1.ensureActive();
                    fail("First Provider instance is still active.");
    
                } catch (Exception expected) {
                    // do nothing
                } finally {
                    // clear the provider1 reference
                    provider1 = null;
                }
    
                // create a second Provider instance
                provider2 = SzCoreProvider.newBuilder().instanceName("Instance 2").settings(BOOTSTRAP_SETTINGS).build();
    
                // ensure it is active
                try {
                    provider2.ensureActive();
                } catch (Exception e) {
                    fail("Second Provider instance is not active.", e);
                }
    
                // destroy the second Provider
                provider2.destroy();
    
                // check it is now inactive
                try {
                    provider2.ensureActive();
                    fail("Second Provider instance is still active.");
    
                } catch (Exception expected) {
                    // do nothing
                } finally {
                    // clear the provider2 reference
                    provider2 = null;
                }
    
                provider2 = null;
    
            } finally {
                if (provider1 != null) {
                    provider1.destroy();
                }
                if (provider2 != null) {
                    provider2.destroy();
                }
            }    
        });
    }


    @ParameterizedTest
    @CsvSource({"1, Foo", "2, Bar", "3, Phoo", "4, Phoox"})
    void testExecute(int threadCount, String expected) {
        SzCoreProvider provider = null;

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            threadCount, threadCount, 10L, SECONDS, new LinkedBlockingQueue<>());

        List<Future<String>> futures = new ArrayList<>(threadCount);
        try {
            provider = SzCoreProvider.newBuilder().build();

            final SzCoreProvider prov = provider;

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
            if (provider != null) {
                provider.destroy();
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Foo", "Bar", "Phoo", "Phoox"})
    void testExecuteFail(String expected) {
        SzCoreProvider provider = null;
        try {
            provider = SzCoreProvider.newBuilder().build();

            try {
               provider.execute(() -> {
                    throw new SzException(expected);
               });

               fail("Expected SzException was not thrown");

            } catch (SzException e) {
                assertEquals(expected, e.getMessage(), "Unexpected exception messasge");

            } catch (Exception e) {
                fail("Failed execute with exception", e);
            }

        } finally {
            if (provider != null) {
                provider.destroy();
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
        SzCoreProvider provider = null;
        try {
            provider = SzCoreProvider.newBuilder().instanceName(expected).build();

            final Thread[]      threads     = new Thread[executeCount];
            final String[]      results     = new String[executeCount];
            final Exception[]   failures    = new Exception[executeCount];

            for (int index = 0; index < executeCount; index++) {
                final SzCoreProvider prov = provider;
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
                int executingCount = provider.getExecutingCount();
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
                int executingCount = provider.getExecutingCount();
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
            if (provider != null) {
                provider.destroy();
            }
        }
    }

    @Test
    void testGetActiveInstance() {
        this.performTest(() -> {
            SzCoreProvider provider1 = null;
            SzCoreProvider provider2 = null;
            try {
                // get the first Provider
                provider1 = SzCoreProvider.newBuilder().instanceName("Instance 1").build();
    
                SzCoreProvider active = SzCoreProvider.getActiveInstance();

                assertNotNull(active, "No active instance found when it should have been: " 
                              + provider1.getInstanceName());
                assertSame(provider1, active,
                            "Active instance was not as expected: " 
                            + ((active == null) ? null : active.getInstanceName()));
    
                // destroy the first Provider
                provider1.destroy();
    
                active = SzCoreProvider.getActiveInstance();
                assertNull(active,
                           "Active instance found when there should be none: " 
                           + ((active == null) ? "" : active.getInstanceName()));
                            
                // create a second Provider instance
                provider2 = SzCoreProvider.newBuilder()
                    .instanceName("Instance 2").settings(BOOTSTRAP_SETTINGS).build();
    
                active = SzCoreProvider.getActiveInstance();
                assertNotNull(active, "No active instance found when it should have been: " 
                              + provider2.getInstanceName());
                assertSame(provider2, active,
                           "Active instance was not as expected: " 
                           + ((active == null) ? null : active.getInstanceName()));
                    
                // destroy the second Provider
                provider2.destroy();
    
                active = SzCoreProvider.getActiveInstance();
                assertNull(active,
                    "Active instance found when there should be none: " 
                    + ((active == null) ? null : active.getInstanceName()));
                
                provider2 = null;
    
            } finally {
                if (provider1 != null) {
                    provider1.destroy();
                }
                if (provider2 != null) {
                    provider2.destroy();
                }
            }    
        });
 
    }

    @Test
    void testGetConfig() {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreProvider provider = null;
            
            try {
                provider = SzCoreProvider.newBuilder()
                                         .instanceName("GetConfig Instance")
                                         .settings(settings)
                                         .verboseLogging(false)
                                         .build();

                SzConfig config1 = provider.getConfig();
                SzConfig config2 = provider.getConfig();

                assertNotNull(config1, "SzConfig was null");
                assertSame(config1, config2, "SzConfig not returning the same object");
                assertSame(provider, config1.getProvider(),
                           "Provider for SzConfig is not the provider that produced it");
                assertInstanceOf(SzCoreConfig.class, config1,
                                "SzConfig instance is not an instance of SzCoreConfig: "
                                + config1.getClass().getName());
                assertFalse(((SzCoreConfig) config1).isDestroyed(),
                            "SzConfig instance reporting that it is destroyed");

                provider.destroy();
                provider = null;

                assertTrue(((SzCoreConfig) config1).isDestroyed(),
                            "SzConfig instance reporting that it is NOT destroyed");

            } catch (SzException e) {
                fail("Got SzException during test", e);

            } finally {
                if (provider != null) provider.destroy();
            }    
        });

    }

    @Test
    void testGetConfigManager() {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreProvider provider = null;
            
            try {
                provider = SzCoreProvider.newBuilder()
                                         .instanceName("GetConfigManager Instance")
                                         .settings(settings)
                                         .verboseLogging(false)
                                         .build();

                SzConfigManager configMgr1 = provider.getConfigManager();
                SzConfigManager configMgr2 = provider.getConfigManager();

                assertNotNull(configMgr1, "SzConfigManager was null");
                assertSame(configMgr1, configMgr2, "SzConfigManager not returning the same object");
                assertSame(provider, configMgr1.getProvider(),
                           "Provider for SzConfigManager is not the provider that produced it");
                assertInstanceOf(SzCoreConfigManager.class, configMgr1,
                                "SzConfigManager instance is not an instance of SzCoreConfigManager: "
                                + configMgr1.getClass().getName());
                assertFalse(((SzCoreConfigManager) configMgr1).isDestroyed(),
                            "SzConfigManager instance reporting that it is destroyed");

                provider.destroy();
                provider = null;

                assertTrue(((SzCoreConfigManager) configMgr1).isDestroyed(),
                            "SzConfigManager instance reporting that it is NOT destroyed");

            } catch (SzException e) {
                fail("Got SzException during test", e);

            } finally {
                if (provider != null) provider.destroy();
            }    
        });
    }

    @Test
    void testGetDiagnostic() {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreProvider provider = null;
            
            try {
                provider = SzCoreProvider.newBuilder()
                                         .instanceName("GetDiagnostic Instance")
                                         .settings(settings)
                                         .verboseLogging(false)
                                         .build();

                SzDiagnostic diagnostic1 = provider.getDiagnostic();
                SzDiagnostic diagnostic2 = provider.getDiagnostic();

                assertNotNull(diagnostic1, "SzDiagnostic was null");
                assertSame(diagnostic1, diagnostic2, "SzDiagnostic not returning the same object");
                assertSame(provider, diagnostic1.getProvider(),
                           "Provider for SzDiagnostic is not the provider that produced it");
                assertInstanceOf(SzCoreDiagnostic.class, diagnostic1,
                                "SzDiagnostic instance is not an instance of SzCoreDiagnostic: "
                                + diagnostic1.getClass().getName());
                assertFalse(((SzCoreDiagnostic) diagnostic1).isDestroyed(),
                            "SzDiagnostic instance reporting that it is destroyed");

                provider.destroy();
                provider = null;

                assertTrue(((SzCoreDiagnostic) diagnostic1).isDestroyed(),
                            "SzDiagnostic instance reporting that it is NOT destroyed");

            } catch (SzException e) {
                fail("Got SzException during test", e);

            } finally {
                if (provider != null) provider.destroy();
            }    
        });
    }

    @Test
    void testGetEngine() {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreProvider provider = null;
            
            try {
                provider = SzCoreProvider.newBuilder()
                                         .instanceName("GetEngine Instance")
                                         .settings(settings)
                                         .verboseLogging(false)
                                         .build();

                SzEngine engine1 = provider.getEngine();
                SzEngine engine2 = provider.getEngine();

                assertNotNull(engine1, "SzEngine was null");
                assertSame(engine1, engine2, "SzEngine not returning the same object");
                assertSame(provider, engine1.getProvider(),
                           "Provider for SzEngine is not the provider that produced it");
                assertInstanceOf(SzCoreEngine.class, engine1,
                                "SzEngine instance is not an instance of SzCoreEngine: "
                                + engine1.getClass().getName());
                assertFalse(((SzCoreEngine) engine1).isDestroyed(),
                            "SzEngine instance reporting that it is destroyed");

                provider.destroy();
                provider = null;

                assertTrue(((SzCoreEngine) engine1).isDestroyed(),
                            "SzEngine instance reporting that it is NOT destroyed");

            } catch (SzException e) {
                fail("Got SzException during test", e);

            } finally {
                if (provider != null) provider.destroy();
            }    
        });
    }

    @Test
    void testGetProduct() {
        this.performTest(() -> {
            String settings = this.getRepoSettings();
            
            SzCoreProvider provider = null;
            
            try {
                provider = SzCoreProvider.newBuilder()
                                         .instanceName("GetProduct Instance")
                                         .settings(settings)
                                         .verboseLogging(false)
                                         .build();

                SzProduct product1 = provider.getProduct();
                SzProduct product2 = provider.getProduct();

                assertNotNull(product1, "SzProduct was null");
                assertSame(product1, product2, "SzProduct not returning the same object");
                assertSame(provider, product1.getProvider(),
                           "Provider for SzProduct is not the provider that produced it");
                assertInstanceOf(SzCoreProduct.class, product1,
                                "SzProduct instance is not an instance of SzCoreProduct: "
                                + product1.getClass().getName());
                assertFalse(((SzCoreProduct) product1).isDestroyed(),
                            "SzProduct instance reporting that it is destroyed");

                provider.destroy();
                provider = null;

                assertTrue(((SzCoreProduct) product1).isDestroyed(),
                            "SzProduct instance reporting that it is NOT destroyed");

            } catch (SzException e) {
                fail("Got SzException during test", e);

            } finally {
                if (provider != null) provider.destroy();
            }    
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "Foo", "Bar", "Phoo" })
    void testGetInstanceName(String instanceName) {
        SzCoreProvider provider = null;
            
        try {
            provider = SzCoreProvider.newBuilder().instanceName(instanceName).build();

            assertEquals(instanceName, provider.getInstanceName(),
                         "Instance names are not equal after building.");
        
        } finally {
            if (provider != null) provider.destroy();
        }
    }

    @ParameterizedTest
    @ValueSource(longs = { 10L, 12L, 0L })
    void testGetConfigId(Long configId) {
        SzCoreProvider provider = null;
        if (configId == 0L) configId = null;

        try {
            provider = SzCoreProvider.newBuilder().configId(configId).build();

            assertEquals(configId, provider.getConfigId(),
                         "Config ID's are not equal after building.");
        
        } finally {
            if (provider != null) provider.destroy();
        }
    }

    @Test
    void testGetSettings() {
        SzCoreProvider provider = null;
            
        try {
            provider = SzCoreProvider.newBuilder().instanceName(BOOTSTRAP_SETTINGS).build();

            assertEquals(BOOTSTRAP_SETTINGS, provider.getSettings(),
                         "Settings are not equal after building.");
        
        } finally {
            if (provider != null) provider.destroy();
        }

    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false})
    void testIsVerboseLogging(boolean verbose) {
        SzCoreProvider provider = null;
            
        try {
            provider = SzCoreProvider.newBuilder().verboseLogging(verbose).build();

            assertEquals(verbose, provider.isVerboseLogging(),
                         "Verbose logging settings are not equal after building.");
        
        } finally {
            if (provider != null) provider.destroy();
        }
    }

    @ParameterizedTest
    @CsvSource({"1,10,Foo", "0,20,Bar", "2,30,Phoo"})
    void testHandleReturnCode(int returnCode, int errorCode, String errorMessage) {
        SzCoreProvider provider = null;
            
        try {
            provider = SzCoreProvider.newBuilder().instanceName(BOOTSTRAP_SETTINGS).build();

            try {
                provider.handleReturnCode(returnCode, new NativeApi() {
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
            if (provider != null) provider.destroy();
        }
    }

}
