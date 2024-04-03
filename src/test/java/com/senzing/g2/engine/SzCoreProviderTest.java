package com.senzing.g2.engine;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

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
                    "SDK instance name is not default instance name");
                assertEquals(provider.getSettings(), defaultSettings,
                    "SDK settings are not bootstrap settings");
                assertFalse(provider.isVerboseLogging(),
                    "SDK verbose logging did not default to false");
                assertEquals(provider.getThreadCount(), SzCoreProvider.DEFAULT_THREAD_COUNT,
                    "SDK thread count is not default thread count");
                assertNull(provider.getConfigId(), "SDK config ID is not null");
    
            } finally {
                if (provider != null) provider.destroy();
            }    
        });
    }

    @ParameterizedTest
    @CsvSource({"true,1", "true,2", "false,3", "false,4"})
    void testNewCustomBuilder(boolean verboseLogging, int threadCount) {
        this.performTest(() -> {
            File testRepoDir = this.createTestRepoDirectory("testNewCustomBuilder");
            RepositoryManager.createRepo(testRepoDir, true);

            String settings = this.readInitJsonFile(testRepoDir);
            
            SzCoreProvider provider = null;
            
            try {
                provider = SzCoreProvider.newBuilder()
                                         .instanceName("Custom Instance")
                                         .settings(settings)
                                         .verboseLogging(verboseLogging)
                                         .threads(threadCount)
                                         .build();

                assertEquals(provider.getInstanceName(), "Custom Instance",
                        "SDK instance name is not as expected");
                assertEquals(provider.getSettings(), settings,
                        "SDK settings are not as expected");
                assertEquals(provider.isVerboseLogging(), verboseLogging,
                        "SDK verbose logging did not default to false");
                assertEquals(provider.getThreadCount(), threadCount,
                    "SDK thread count is not default thread count");
                assertNull(provider.getConfigId(), "SDK config ID is not null");
    
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
                // get the first SDK
                provider1 = SzCoreProvider.newBuilder().instanceName("Instance 1").build();
    
                // ensure it is active
                try {
                    provider1.ensureActive();
                } catch (Exception e) {
                    fail("First SDK instance is not active.", e);
                }
    
                // destroy the first SDK
                provider1.destroy();
    
                // check it is now inactive
                try {
                    provider1.ensureActive();
                    fail("First SDK instance is still active.");
    
                } catch (Exception expected) {
                    // do nothing
                } finally {
                    // clear the provider1 reference
                    provider1 = null;
                }
    
                // create a second SDK instance
                provider2 = SzCoreProvider.newBuilder().instanceName("Instance 2").settings(BOOTSTRAP_SETTINGS).build();
    
                // ensure it is active
                try {
                    provider2.ensureActive();
                } catch (Exception e) {
                    fail("Second SDK instance is not active.", e);
                }
    
                // destroy the second SDK
                provider2.destroy();
    
                // check it is now inactive
                try {
                    provider2.ensureActive();
                    fail("Second SDK instance is still active.");
    
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
        try {
            provider = SzCoreProvider.newBuilder().threads(threadCount).build();

            try {
               String actual = provider.execute(() -> {
                    return expected;
               });

               assertEquals(expected, actual, "Unexpected result from execute()");

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
    void testExecuteFail(int threadCount, String expected) {
        SzCoreProvider provider = null;
        try {
            provider = SzCoreProvider.newBuilder().threads(threadCount).build();

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
    void testExecuteThreadPool(int threadCount, String expected) {
        SzCoreProvider provider = null;
        try {
            final long delay = 300L;

            provider = SzCoreProvider.newBuilder()
                .instanceName(expected).threads(threadCount).build();

            int executeCount = threadCount * 3;
            final String[]      threadNames = new String[executeCount];
            final long[]        startTimes  = new long[executeCount];
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
                                startTimes[threadIndex]  = System.nanoTime();
                                threadNames[threadIndex] = Thread.currentThread().getName();
                                Thread.sleep(delay);
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
            for (int index = 0; index < executeCount; index++) {
                threads[index].start();
            }
            for (int index = 0; index < executeCount; index++) {
                try {
                    threads[index].join();
                } catch (InterruptedException e) {
                    fail("Interrupted while joining threads");
                }
            }
            
            // check the basics
            for (int index = 0; index < executeCount; index++) {
                assertTrue((threadNames[index].indexOf(expected) >= 0),
                       "Thread name does not contain expected instance name");
                assertEquals(expected + "-" + index, results[index],
                            "At least one thread returned an unexpected result");
                assertNull(failures[index], 
                                    "At least one thread threw an exception");
            }
            
            // check the start time differences
            Map<Integer, Integer> phaseCounts = new TreeMap<>();
            long    minStart        = -1L;
            int     maxPhase        = 0;
            int     maxPhaseCount   = 0;
            for (int index = 0; index < executeCount; index++) {
                if (minStart < 0L || minStart > startTimes[index]) {
                    minStart = startTimes[index];
                }
            }
            for (int index = 0; index < executeCount; index++) {
                long diff = (startTimes[index] - minStart) / 1000000L;
                int phase = (int) (diff / delay);
                if (phaseCounts.containsKey(phase)) {
                    int phaseCount = phaseCounts.get(phase) + 1;
                    phaseCounts.put(phase, phaseCount);
                    if (phaseCount > maxPhase) maxPhase = phaseCount;
                } else {
                    phaseCounts.put(phase, 1);
                    if (maxPhaseCount == 0) maxPhaseCount = 1;
                }
                if (phase > maxPhase) maxPhase = phase;
            }
            int minPhaseCount = phaseCounts.values().stream().min(Integer::compareTo).get();

            assertNotEquals(1, phaseCounts.size(),
                            "Threads all executed in the same phase: " + phaseCounts);
            assertTrue(maxPhase < 5, 
                       "Maximum execution phase exceeded thread multiple: " + phaseCounts);
            assertTrue(maxPhaseCount <= threadCount,
                        "Too many threads (" + maxPhaseCount + ") executed in the same phase.  "
                        + "At most " + threadCount + " should have been possible: " + phaseCounts);
            assertNotEquals(threadCount - 1, minPhaseCount,
                            "Too few threads executed in at least one phase: " + phaseCounts);

        } finally {
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
                // get the first SDK
                provider1 = SzCoreProvider.newBuilder().instanceName("Instance 1").build();
    
                SzCoreProvider active = SzCoreProvider.getActiveInstance();

                assertNotNull(active, "No active instance found when it should have been: " 
                              + provider1.getInstanceName());
                assertSame(provider1, active,
                            "Active instance was not as expected: " 
                            + ((active == null) ? null : active.getInstanceName()));
    
                // destroy the first SDK
                provider1.destroy();
    
                active = SzCoreProvider.getActiveInstance();
                assertNull(active,
                           "Active instance found when there should be none: " 
                           + ((active == null) ? "" : active.getInstanceName()));
                            
                // create a second SDK instance
                provider2 = SzCoreProvider.newBuilder()
                    .instanceName("Instance 2").settings(BOOTSTRAP_SETTINGS).build();
    
                active = SzCoreProvider.getActiveInstance();
                assertNotNull(active, "No active instance found when it should have been: " 
                              + provider2.getInstanceName());
                assertSame(provider2, active,
                           "Active instance was not as expected: " 
                           + ((active == null) ? null : active.getInstanceName()));
                    
                // destroy the second SDK
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
            File testRepoDir = this.createTestRepoDirectory("testGetConfig");
            RepositoryManager.createRepo(testRepoDir, true);
            String settings = this.readInitJsonFile(testRepoDir);
            
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
            File testRepoDir = this.createTestRepoDirectory("testGetConfigManager");
            RepositoryManager.createRepo(testRepoDir, true);
            String settings = this.readInitJsonFile(testRepoDir);
            
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
            File testRepoDir = this.createTestRepoDirectory("testGetDiagnostic");
            RepositoryManager.createRepo(testRepoDir, true);
            String settings = this.readInitJsonFile(testRepoDir);
            
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
            File testRepoDir = this.createTestRepoDirectory("testGetEngine");
            RepositoryManager.createRepo(testRepoDir, true);
            String settings = this.readInitJsonFile(testRepoDir);
            
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
            File testRepoDir = this.createTestRepoDirectory("testGetProduct");
            RepositoryManager.createRepo(testRepoDir, true);
            String settings = this.readInitJsonFile(testRepoDir);
            
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

    @Test
    void testHandleReturnCode() {

    }

}
