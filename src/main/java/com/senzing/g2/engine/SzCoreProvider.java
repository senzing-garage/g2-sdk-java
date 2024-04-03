package com.senzing.g2.engine;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Provides a bare-metal implementation of {@link SzProvider} that directly
 * initializes the Senzing SDK interfaces and provides singleton access 
 * to those interfaces that will be valid until the created instance of 
 * {@link SzCoreProvider}.
 */
public class SzCoreProvider implements SzProvider {
    /**
     * The environment variable for obtaining the Senzing SDK settings
     * from the environment.  If a value is set in the environment then
     * it will be used by default for initializing the Senzing SDK unless
     * the {@link Builder#settings(String)} method is used to provide
     * different settings.
     * <p>
     * The value of this constant is <code>{@value}</code>.
     * 
     * @see Builder#settings(String)
     */
    public static final String SETTINGS_ENVIRONMENT_VARIABLE
        = "SENZING_ENGINE_CONFIGURATION_JSON";

    /**
     * The default instance name to use for the Senzing SDK.  The value is
     * <code>"Senzing Instance"</code>.  An explicit vaklue can be provided
     * via {@link Builder#instanceName(String)} during SDK initialization.
     * <p>
     * The value of this constant is <code>{@value}</code>.
     * 
     * @see Builder#instanceName(String)
     */
    public static final String DEFAULT_INSTANCE_NAME = "Senzing Instance";

    /**
     * The bootstrap settings with which to initialize the SDK when the {@link 
     * #SETTINGS_ENVIRONMENT_VARIABLE} is <b>not</b> set and an explicit value
     * settings value has not been provided via {@link 
     * Builder#settings(String)}.  If this is used it will initialize Senzing
     * for access to only the {@link SzProduct} and {@link SzConfig} interfaces
     * when Senzing installed in the default location for the platform.  The
     * value of this constant is <code>"{ }"</code>.
     * <p>
     * <b>NOTE:</b> Using these settings is only useful for accessing the
     * {@link SzProduct} and {@link SzConfig} interfaces since {@link
     * SzEngine}, {@link SzConfigManager} and {@link SzDiagnostic} require
     * database configuration to connect to the Senzing repository.
     * 
     * <p>
     * The value of this constant is <code>{@value}</code>.
     * 
     * @see SzCoreProvider#SETTINGS_ENVIRONMENT_VARIABLE
     * @see Builder#settings(String)
     */
    public static final String BOOTSTRAP_SETTINGS = "{ }";

    /**
     * The default number of threads to allocate for executing Senzing SDK
     * operations to restrain memory consumption for use by Senzing.  This
     * defaults to one (<code>1</code>) but can be modified via {@link
     * Builder#threads(int)}.
     * <p>
     * The value of this constant is <code>{@value}</code>.
     *
     * @see Builder#threads(int) 
     */
    public static final int DEFAULT_THREAD_COUNT = 1;

    /**
     * Enumerates the possible states for an instance of {@link SzCoreProvider}.
     */
    private static enum State {
        /**
         * If an {@link SenzingSdk} instance is in the "active" state then it
         * is initialized and ready to use.  Only one instance of {@link SenzingSdk}
         * can exist in the {@link #ACTIVE} or {@link #DESTROYING} state.   is
         * the one and only instance that will exist in that state since the
         * Senzing SDK cannot be initialized heterogeneously within a single 
         * process.
         * 
         * @see SenzingSdk#getActiveInstance()
         */
        ACTIVE,

        /**
         * An instance {@link SenzingSdk} moves to the "destroying" state when
         * the {@link SenzingSdk#destroy()} method has been called but has not
         * yet completed any Senzing operations that are already in-progress.
         * In this state, the {@link SenzingSdk} will fast-fail any newly attempted
         * operations with an {@link IllegalStateException}, but will complete
         * those Senzing operations that were initiated before {@link
         * SenzingSdk#destroy()} was called.
         */
        DESTROYING,

        /**
         * An instance of {@link SenzingSdk} moves to the "destroyed" state when
         * the {@link SenzingSdk#destroy()} method has completed and there are no
         * more Senzing operations that are in-progress.  Once an {@link #ACTIVE}
         * instance moves to {@link #DESTROYED} then a new active instance can 
         * be created and initialized.
         */

        DESTROYED;
    }

    /**
     * Creates a new instance of {@link Builder} for setting up an instance
     * of {@link SzCoreProvider}.  Keep in mind that while multiple {@link Builder}
     * instances can exists, <b>only one active instance</b> of {@link SzCoreProvider}
     * can exist at time.  An active instance is one that has not yet been
     * destroyed.
     * 
     * @return The {@link Builder} for configuring and initializing the Senzing SDK.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * The currently instance of the {@link SzCoreProvider}.
     */
    private static SzCoreProvider current_instance = null;

    /** 
     * Gets the current active instance of {@link SzCoreProvider}.  An active instance
     * is is one that has been constructed and has not yet been destroyed.  There
     * can be at most one active instance.  If no active instance exists then 
     * <code>null</code> is returned.
     * 
     * @return The current active instance of {@link SzCoreProvider}, or 
     *         <code>null</code> if there is no active instance.
     */
    public static SzCoreProvider getActiveInstance() {
        synchronized (SzCoreProvider.class) {
            if (current_instance == null) return null;
            synchronized (current_instance) {
                State state = current_instance.state;
                switch (state) {
                    case DESTROYING:
                        // wait until destroyed and fall through
                        waitUntilDestroyed(current_instance);
                    case DESTROYED:
                        // if still set but destroyed, clear it and fall through
                        current_instance = null;
                    case ACTIVE:
                        // return the current instance
                        return current_instance;
                    default:
                        throw new IllegalStateException(
                            "Unrecognized Senzing SDK state: " + state);
                }
            }
        }
    }

    /**
     * Waits until the specified {@link SzCoreProvider} instance has been destroyed.
     * Use this when obtaining an instance of {@link SzCoreProvider} in the {@link 
     * State#DESTROYING} and you want to wait until it is fully destroyed.
     * 
     * @param sdkInstacne The non-null {@link SzCoreProvider} instance to wait on.
     * 
     * @throws NullPointerException If the specified parameter is <code>null</code>.
     */
    private static void waitUntilDestroyed(SzCoreProvider sdkInstance) 
    {
        Objects.requireNonNull(sdkInstance, "The specified SenzingSdk cannot be null");
        synchronized (sdkInstance) {
            while (sdkInstance.state != State.DESTROYED) {
                try {
                    sdkInstance.wait(5000L);
                } catch (InterruptedException ignore) {
                    // ignore the exception
                }
            }
        }
    }

    /**
     * The instance name to initialize the API's with.
     */
    private String instanceName = null;

    /**
     * The settings to initialize the API's with.
     */
    private String settings = null;

    /**
     * The flag indicating if verbose logging is enabled.
     */
    private boolean verboseLogging = false;

    /**
     * The explicit configuration ID to initialize with or <code>null</code> if
     * using the default configuration.
     */
    private Long configId = null;

    /**
     * The number of executor threads to initialize with.
     */
    private int threadCount = 0;

    /**
     * The {@link ThreadPoolExecutor} to be used by the API implementations.
     */
    private ThreadPoolExecutor threadPool = null;

    /**
     * The {@link SzCoreProduct} singleton instance to use.
     */
    private SzCoreProduct coreProduct = null;

    /**
     * The {@link SzCoreConfig} singleton instance to use.
     */
    private SzCoreConfig coreConfig = null;

    /**
     * The {@link SzCoreEngine} singleton intance to use.
     */
    private SzCoreEngine coreEngine = null;

    /**
     * The {@link SzCoreConfigManager} singleton instance to use.
     */
    private SzCoreConfigManager coreConfigMgr = null;

    /**
     * The {@link SzCoreDiagnostic} singleton instance to use.
     */
    private SzCoreDiagnostic coreDiagnostic = null;

    /**
     * The {@link State} for this instance.
     */
    private State state = null;

    /**
     * Private constructor used by the builder to construct the SDK.
     *  
     * @param instanceName The Senzing SDK instance name.
     * @param settings The Senzing SDK settings.
     * @param verboseLogging The verbose logging setting for Senzing SDK.
     * @param configId The explicit config ID for the Senzing SDK initialization, or
     *                 <code>null</code> if using the default configuration.
     * @param threadCount The number of threads to initialize the executor pool with.
     */
    private SzCoreProvider(String   instanceName,
                           String   settings,
                           boolean  verboseLogging,
                           Long     configId,
                           int      threadCount) 
    {
        // set the fields
        this.instanceName   = instanceName;
        this.settings       = settings;
        this.verboseLogging = verboseLogging;
        this.configId       = configId;
        this.threadCount    = threadCount;

        // create the executor thread pool
        this.threadPool = new ThreadPoolExecutor(
            this.threadCount, this.threadCount, 10L, SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactory() {
                private int threadIndex = 0;
                public synchronized Thread newThread(Runnable r) {
                  return new Thread(r, "Executor-" + instanceName + "-" + this.threadIndex++);
                }
            });

        synchronized (SzCoreProvider.class) {
            SzCoreProvider activeSDK = getActiveInstance();
            if (activeSDK != null) {
                throw new IllegalStateException(
                    "At most one active instance of SzCoreProvider can be initialized.  "
                    + "Another instance was already initialized.");
            }

            // set the state
            this.state = State.ACTIVE;

            // set the current instance
            current_instance = this;
        }
    }

    /**
     * Gets the associated Senzing SDK instance name for initialization.
     * 
     * @return The associated Senzing SDK instance name for initialization.
     */
    String getInstanceName() {
        return this.instanceName;
    }

    /**
     * Gets the associated Senzing SDK settings for initialization.
     * 
     * @return The associated Senzing SDK settings for initialization.
     */
    String getSettings() {
        return this.settings;
    }

    /**
     * Gets the verbose logging setting to indicating if verbose logging
     * should be enabled (<code>true</code>) or disabled (<code>false</code>).
     * 
     * @return <code>true</code> if verbose logging should be enabled, otherwise
     *         <code>false</code>.
     */
    boolean isVerboseLogging() {
        return this.verboseLogging;
    }   

    /**
     * Gets the explicit configuraiton ID with which to initialize the Senzing SDK.
     * This returns <code>null</code> if the default configuration ID configured in
     * the repository should be used.
     * 
     * @return The explicit configuration ID with which to initialize the Senzing SDK,
     *         or <code>null</code> if the default configuration ID configured in the
     *         repository should be used.
     */
    Long getConfigId() {
        return this.configId;
    }
    
    /**
     * Gets the configued thread count for the executor thread pool.
     * 
     * @return The configued thread count for the executor thread pool.
     */
    int getThreadCount() {
        return this.threadCount;
    }

    /**
     * Executes the specified {@link Callable} task using this instance's
     * executor thread pool and returns the result if successful.  This
     * will throw any exception produced by the {@link Callable} task,
     * wrapping it in an {@link SzException} if it is a checked exception
     * that is not of type {@link SzException}.
     * 
     * @param <T> The return type.
     * @param task The {@link Callable} task to execute.
     * @return The result from the {@link Callable} task.
     * @throws SzException If the {@link Callable} task triggers a failure.
     * @throws IllegalStateException If this {@link SzCoreProvider} instance has
     *                               already been destroyed.
     */
    <T> T execute(Callable<T> task)
        throws SzException, IllegalStateException
    {
        Future<T> future = null;
        synchronized (this) {
            if (this.state != State.ACTIVE) {
                throw new IllegalStateException(
                    "SenzingSdk has been destroyed");
            }

            // submit the task
            try {
                future = this.threadPool.submit(task);

            } catch (RejectedExecutionException e) {
                // this should NOT happen if not destroyed, but if
                // it does then rethrow it
                throw e;
            }
        }
        
        try {
            return future.get();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        } catch (ExecutionException e) {
            // get the cause
            Throwable cause = e.getCause();

            // rethrow SzException's as SzException
            if (cause instanceof SzException) {
                throw ((SzException) cause);
            }

            // rethrow runtime exceptions as runtime exceptions
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }

            // rethrow errors as errors
            if (cause instanceof Error) {
                throw ((Error) cause);
            }

            // wrap any checked exception in SzException and rethrow
            throw new SzException(cause);
        }
    }

    /**
     * Ensures this instance is still active and if not will throw 
     * an {@link IllegalStateException}.
     *
     * @throws IllegalStateException If this instance is not active.
     */
    synchronized void ensureActive() throws IllegalStateException {
        if (this.state != State.ACTIVE) {
            throw new IllegalStateException(
                "The Senzing SDK has already been destroyed.");
        }
    }

    /**
     * Handles the Senzing JNI return code and coverts it to the proper
     * {@link SzException} if it is not zero (0).
     * 
     * @param returnCode The return code to handle.
     * @param nativeApi The {@link NativeApi} implementation that produced the
     *                  return code on this current thread.
     */
    void handleReturnCode(int returnCode, NativeApi nativeApi) 
        throws SzException
    {
        if (returnCode == 0) return;
        // TODO(barry): Map the exception properly
        int     errorCode   = nativeApi.getLastExceptionCode();
        String  message     = nativeApi.getLastException();
        nativeApi.clearLastException();
        throw new SzException(errorCode, message);
    }

    @Override
    public SzConfig getConfig() 
        throws IllegalStateException, SzException 
    {
        synchronized (this) {
            this.ensureActive();
            if (this.coreConfig == null) {
                this.coreConfig = new SzCoreConfig(this);
            }
        }

        // return the configured SDK
        return this.coreConfig;
    }

    @Override
    public SzConfigManager getConfigManager()
       throws IllegalStateException, SzException 
    {
        synchronized (this) {
            this.ensureActive();
            if (this.coreConfigMgr == null) {
                this.coreConfigMgr = new SzCoreConfigManager(this);
            }
        }

        // return the configured SDK
        return this.coreConfigMgr;
    }

    @Override
    public SzDiagnostic getDiagnostic() 
       throws IllegalStateException, SzException 
    {
        synchronized (this) {
            this.ensureActive();
            if (this.coreDiagnostic == null) {
                this.coreDiagnostic = new SzCoreDiagnostic(this);
            }
        }

        // return the configured SDK
        return this.coreDiagnostic;
    }

    @Override
    public SzEngine getEngine() 
       throws IllegalStateException, SzException 
    {
        synchronized (this) {
            this.ensureActive();
            if (this.coreEngine == null) {
                this.coreEngine = new SzCoreEngine(this);
            }
        }

        // return the configured SDK
        return this.coreEngine;
    }

    @Override
    public SzProduct getProduct() 
       throws IllegalStateException, SzException 
    {
        synchronized (this) {
            this.ensureActive();
            if (this.coreProduct == null) {
                this.coreProduct = new SzCoreProduct(this);
            }
        }

        // return the configured SDK
        return this.coreProduct;
    }

    @Override
    public void destroy() {
        synchronized (this) {
            // check if this has already been called
            if (this.state != State.ACTIVE) return;
            
            // set the flag for destroying
            this.state = State.DESTROYING;

            // shutdown the thread pool
            this.threadPool.shutdown();
        }

        // await termination
        while (!this.threadPool.isTerminated()) {
            try {
                this.threadPool.awaitTermination(5, SECONDS);
            } catch (InterruptedException ignore) {
                // do nothing
            }
        }

        // once we get here we can really shut things down
        if (this.coreEngine != null) {
            this.coreEngine.destroy();
            this.coreEngine = null;
        }
        if (this.coreDiagnostic != null) {
            this.coreDiagnostic.destroy();
            this.coreDiagnostic = null;
        }
        if (this.coreConfigMgr != null) {
            this.coreConfigMgr.destroy();
            this.coreConfigMgr = null;
        }
        if (this.coreConfig != null) {
            this.coreConfig.destroy();
            this.coreConfig = null;
        }
        if (this.coreProduct != null) {
            this.coreProduct.destroy();
            this.coreProduct = null;
        }

        // set the state
        synchronized (this) {
            this.state = State.DESTROYED;
        }

    }

    @Override
    public boolean isDestroyed() {
        synchronized (this) {
            return this.state != State.ACTIVE;
        }
    }

    /**
     * The builder class for creating an instance of {@link SzCoreProvider}.
     */
    public static class Builder {
        /**
         * The settings for the builder which default to {@link 
         * SzCoreProvider#BOOTSTRAP_SETTINGS}.
         */
        private String settings = BOOTSTRAP_SETTINGS;

        /**
         * The instance name for the builder which defaults to {@link 
         * SzCoreProvider#DEFAULT_INSTANCE_NAME}.
         */
        private String instanceName = DEFAULT_INSTANCE_NAME;

        /**
         * The verbose logging setting for the builder which defaults
         * to <code>false</code>.
         */
        private boolean verboseLogging = false;

        /**
         * The default number of threads for the builder which defaults
         * to {@link SzCoreProvider#DEFAULT_THREAD_COUNT}.
         */
        private int threadCount = DEFAULT_THREAD_COUNT;

        /**
         * The config ID for the builder.  If not provided explicitly then
         * the configured default configuration in the Senzing repository
         * is used.
         */
        private Long configId = null;

        /**
         * Private constructor.
         */
        public Builder() {
            this.settings       = getDefaultSettings();
            this.instanceName   = DEFAULT_INSTANCE_NAME;
            this.threadCount    = DEFAULT_THREAD_COUNT;
            this.verboseLogging = false;
            this.configId       = null;
        }

        /**
         * Obtains the default Senzing SDK settings from the system environment 
         * using the {@link SzCoreProvider#SETTINGS_ENVIRONMENT_VARIABLE}.  If the 
         * settings are not available from the environment then the bootstrap
         * settings defined by {@link SzCoreProvider#BOOTSTRAP_SETTINGS} are returned.
         * 
         * @return The default Senzing SDK settings.
         */
        protected static String getDefaultSettings() {
            String envSettings = System.getenv(SETTINGS_ENVIRONMENT_VARIABLE);
            if (envSettings != null && envSettings.trim().length() > 0) {
                return envSettings.trim();
            } else {
                return BOOTSTRAP_SETTINGS;
            }
        }

        /**
         * Provides the Senzing SDK settings to configure the Senzing SDK.
         * If this is set to <code>null</code> or empty-string then an attempt
         * will be made to obtain the settings from the system environment via
         * the {@link SzCoreProvider#SETTINGS_ENVIRONMENT_VARIABLE} with a fallback
         * to the {@link SzCoreProvider#BOOTSTRAP_SETTINGS} if the environment
         * variable is not set.
         * 
         * @param settings The Senzing SDK settings, or <code>null</code> or 
         *                 empty-string to restore the default value.
         * 
         * @return A reference to this instance.
         * 
         * @see SzCoreProvider#SETTINGS_ENVIRONMENT_VARIABLE
         * @see SzCoreProvider#BOOTSTRAP_SETTINGS                
         */
        public Builder settings(String settings) {
            if (settings != null && settings.trim().length() == 0) {
                settings = null;
            }
            this.settings = (settings == null)
                ? getDefaultSettings() : settings.trim();
            return this;
        }

        /**
         * Provides the Senzing SDK instance name to configure the Senzing SDK.
         * Call this method to override the default value of {@link 
         * SzCoreProvider#DEFAULT_INSTANCE_NAME}.
         * 
         * @param instanceName The instance name to initialize the Senzing SDK, or
         *                     <code>null</code> or empty-string to restore the 
         *                     default value.
         * 
         * @return A reference to this instance.
         * 
         * @see SzCoreProvider#DEFAULT_INSTANCE_NAME
         */
        public Builder instanceName(String instanceName) {
            if (instanceName != null && instanceName.trim().length() == 0) {
                instanceName = null;
            }
            this.instanceName = (instanceName == null) 
                ? DEFAULT_INSTANCE_NAME : instanceName.trim();
            return this;
        }

        /**
         * Sets the verbose logging flag for configuring the Senzing SDK.
         * Call this method to explicitly set the value.  If not called, the
         * default value is <code>false</code>.
         * 
         * @param verboseLogging <code>true</code> if verbose logging should be
         *                       enabled, otherwise <code>false</code>.
         * 
         * @return A reference to this instance.
         */
        public Builder verboseLogging(boolean verboseLogging) {
            this.verboseLogging = verboseLogging;
            return this;
        }

        /**
         * Sets the explicit configuration ID to use to initialize the Senzing SDK.
         * If not specified then the default configuration ID obtained from the
         * Senzing repository is used.
         * 
         * @param configId The explicit configuration ID to use to initialize the
         *                 Senzing SDK, or <code>null</code> if the default 
         *                 configuration ID from the Senzing repository should be
         *                 used.
         * 
         * @return A reference to this instance.
         */
        public Builder configId(Long configId) {
            this.configId = configId;
            return this;
        }

        /**
         * Sets the number of Senzing SDK execution threads to initialize for 
         * using the Senzing SDK.  This defaults to {@link 
         * SzCoreProvider#DEFAULT_THREAD_COUNT} if not expliitly set.
         * 
         * @param threadCount The number of Senzing SDK to initialize for use 
         *                    with Senzing operations.
         * 
         * @return A reference to this instance.
         * 
         * @throws IllegalArgumentException If the specifed thread count is less-than
         *                                  or equal-to zero (0).
         */
        public Builder threads(int threadCount) throws IllegalArgumentException {
            if (threadCount <= 0) {
                throw new IllegalArgumentException(
                    "The specified thread count must be a positive number: " 
                    + threadCount);
            }
            this.threadCount = threadCount;
            return this;
        }

        /**
         * This method creates a new {@link SzCoreProvider} instance based on this
         * {@link Builder} instance.  This method will throw an {@link 
         * IllegalStateException} if another active {@link SzCoreProvider} instance
         * exists since only one active instance can exist within a process at
         * any given time.  An active instance is one that has been constructed, 
         * but has not yet been destroyed.
         * 
         * @return The newly created {@link SzCoreProvider} instance.
         * 
         * @throws IllegalStateException If another active {@link SzCoreProvider}
         *                               instance exists when this method is
         *                               invoked.
         */
        public SzCoreProvider build() throws IllegalStateException
        {
            return new SzCoreProvider(this.instanceName,
                                  this.settings,
                                  this.verboseLogging,
                                  this.configId,
                                  this.threadCount);
        }

    }
}
