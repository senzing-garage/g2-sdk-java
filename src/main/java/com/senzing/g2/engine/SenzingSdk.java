package com.senzing.g2.engine;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

/**
 * Provides a bare-metal implementation of {@link SzFactory} that directly
 * initializes the Senzing SDK interfaces and provides singleton access 
 * to those interfaces that will be valid until the created instance of 
 * {@link SenzingSdk}.
 */
public class SenzingSdk implements SzFactory {
    /**
     * The environment variable for obtaining the Senzing SDK settings
     * from the environment.  If a value is set in the environment then
     * it will be used by default for initializing the Senzing SDK unless
     * the {@link Builder#settings(String)} method is used to provide
     * different settings.
     * 
     * @value <code>"SENZING_ENGINE_CONFIGURATION_JSON"</code>
     * @see Builder#settings(String)
     */
    private static final String SETTINGS_ENVIRONMENT_VARIABLE
        = "SENZING_ENGINE_CONFIGURATION_JSON";

    /**
     * The default instance name to use for the Senzing SDK.  The value is
     * <code>"Senzing Instance"</code>.  An explicit vaklue can be provided
     * via {@link Builder#instanceName(String)} during SDK initialization.
     * 
     * @value <code>"Senzing Instance"</code>
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
     * @value <code>"{ }"</code>
     * @see SenzingSdk#SETTINGS_ENVIRONMENT_VARIABLE
     * @see Builder#settings(String)
     */
    public static final String BOOTSTRAP_SETTINGS = "{ }";

    /**
     * The default number of threads to allocate for executing Senzing SDK
     * operations to restrain memory consumption for use by Senzing.  This
     * defaults to one (<code>1</code>) but can be modified via {@link
     * Builder#threads(int)}.
     * 
     * @value <code>1</code>
     * @see Builder#threads(int) 
     */
    public static final int DEFAULT_THREAD_COUNT = 1;

    /**
     * Enumerates the possible states for an instance of {@link SenzingSdk}.
     */
    private static enum State {
        /**
         * If an {@link SenzingSDK} instance is in the "active" state then it
         * is initialized and ready to use.  Only one instance of {@link SenzingSDK}
         * can exist in the {@link #ACTIVE} or {@link #DESTROYING} state.   is
         * the one and only instance that will exist in that state since the
         * Senzing SDK cannot be initialized heterogeneously within a single 
         * process.
         * 
         * @see SenzingSDK#getActiveInstance()
         */
        ACTIVE,

        /**
         * An instance {@link SenzingSDK} moves to the "destroying" state when
         * the {@link SenzingSDK#destroy()} method has been called but has not
         * yet completed any Senzing operations that are already in-progress.
         * In this state, the {@link SenzingSDK} will fast-fail any newly attempted
         * operations with an {@link IllegalStateException}, but will complete
         * those Senzing operations that were initiated before {@link
         * SenzingSDK#destroy()} was called.
         */
        DESTROYING,

        /**
         * An instance of {@link SenzingSDK} moves to the "destroyed" state when
         * the {@link SenzingSDK#destroy()} method has completed and there are no
         * more Senzing operations that are in-progress.  Once an {@link #ACTIVE}
         * instance moves to {@link #DESTROYED} then a new active instance can 
         * be created and initialized.
         */

        DESTROYED;
    }

    /**
     * Creates a new instance of {@link Builder} for setting up an instance
     * of {@link SenzingSdk}.  Keep in mind that while multiple {@link Builder}
     * instances can exists, <b>only one active instance</b> of {@link SenzingSdk}
     * can exist at time.  An active instance is one that has not yet been
     * destroyed.
     * 
     * @return The {@link Builder} for configuring and initializing the Senzing SDK.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * The currently instance of the {@link SenzingSdk}.
     */
    private static SenzingSdk current_instance = null;

    /** 
     * Gets the current active instance of {@link SenzingSdk}.  An active instance
     * is is one that has been constructed and has not yet been destroyed.  There
     * can be at most one active instance.  If no active instance exists then 
     * <code>null</code> is returned.
     * 
     * @return The current active instance of {@link SenzingSdk}, or 
     *         <code>null</code> if there is no active instance.
     */
    public static SenzingSdk getActiveInstance() {
        synchronized (SenzingSdk.class) {
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
     * Waits until the specified {@link SenzingSdk} instance has been destroyed.
     * Use this when obtaining an instance of {@link SenzingSdk} in the {@link 
     * State#DESTROYING} and you want to wait until it is fully destroyed.
     * 
     * @param sdkInstacne The non-null {@link SenzingSdk} instance to wait on.
     * 
     * @throws NullPointerException If the specified parameter is <code>null</code>.
     */
    private static void waitUntilDestroyed(SenzingSdk sdkInstance) 
    {
        Objects.requireNonNull(sdkInstance, "The specified SenzingSDK cannot be null");
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
     * The {@link ExecutorService} to be used by the API implementations.
     */
    private ExecutorService executorService = null;

    /**
     * The {@link SzProductSdk} singleton instance to use.
     */
    private SzProductSdk productSdk = null;

    /**
     * The {@link SzConfigSdk} singleton instance to use.
     */
    private SzConfigSdk configSdk = null;

    /**
     * The {@link SzEngineSdk} singleton intance to use.
     */
    private SzEngineSdk engineSdk = null;

    /**
     * The {@link SzConfigManagerSdk} singleton instance to use.
     */
    private SzConfigManagerSdk configMgrSdk = null;

    /**
     * The {@link SzDiagnosticSdk} singleton instance to use.
     */
    private SzDiagnosticSdk diagnosticSdk = null;

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
    private SenzingSdk(String   instanceName,
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

        synchronized (SenzingSdk.class) {
            SenzingSdk activeSDK = getActiveInstance();
            if (activeSDK != null) {
                throw new IllegalStateException(
                    "At most one active instance of SenzingSDK can be initialized.  "
                    + "Another instance was already initialized.");
            }
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
     * @throws IllegalStateException If this {@link SenzingSDK} instance has
     *                               already been destroyed.
     */
    <T> T execute(Callable<T> task) 
        throws SzException, IllegalStateException
    {
        Future<T> future = null;
        synchronized (this) {
            if (this.state != State.ACTIVE) {
                throw new IllegalStateException(
                    "SenzingSDK has been destroyed");
            }

            // submit the task
            try {
                future = this.executorService.submit(task);

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
     * @param fallible The {@link G2Fallible} implementation that produced the
     *                 return code on this current thread.
     */
    void handleReturnCode(int returnCode, G2Fallible fallible) 
        throws SzException
    {
        if (returnCode == 0) return;
        // TODO(barry): Map the exception properly
        throw new SzException(fallible.getLastExceptionCode() 
            + ":" + fallible.getLastException());
    }

    @Override
    public SzConfig getConfig() 
        throws IllegalStateException, SzException 
    {
        synchronized (this) {
            this.ensureActive();
            if (this.configSdk == null) {
                this.configSdk = new SzConfigSdk(this);
            }
        }

        // return the configured SDK
        return this.configSdk;
    }

    @Override
    public SzConfigManager getConfigManager()
       throws IllegalStateException, SzException 
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SzDiagnostic getDiagnostic() 
       throws IllegalStateException, SzException 
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SzEngine getEngine() 
       throws IllegalStateException, SzException 
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SzProduct getProduct() 
       throws IllegalStateException, SzException 
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }


    /**
     * The builder class for creating an instance of {@link SenzingSdk}.
     */
    public static class Builder {
        /**
         * The settings for the builder which default to {@link 
         * SenzingSdk#BOOTSTRAP_SETTINGS}.
         */
        private String settings = BOOTSTRAP_SETTINGS;

        /**
         * The instance name for the builder which defaults to {@link 
         * SenzingSdk#DEFAULT_INSTANCE_NAME}.
         */
        private String instanceName = DEFAULT_INSTANCE_NAME;

        /**
         * The verbose logging setting for the builder which defaults
         * to <code>false</code>.
         */
        private boolean verboseLogging = false;

        /**
         * The default number of threads for the builder which defaults
         * to {@link SenzingSdk#DEFAULT_THREAD_COUNT}.
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
         * using the {@link SenzingSdk#SETTINGS_ENVIRONMENT_VARIABLE}.  If the 
         * settings are not available from the environment then the bootstrap
         * settings defined by {@link SenzingSdk#BOOTSTRAP_SETTINGS} are returned.
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
         * the {@link SenzingSdk#SETTINGS_ENVIRONMENT_VARIABLE} with a fallback
         * to the {@link SenzingSdk#BOOTSTRAP_SETTINGS} if the environment
         * variable is not set.
         * 
         * @param settings The Senzing SDK settings, or <code>null</code> or 
         *                 empty-string to restore the default value.
         * 
         * @return A reference to this instance.
         * 
         * @see SenzingSdk#SETTINGS_ENVIRONMENT_VARIABLE
         * @see SenzingSdk#BOOTSTRAP_SETTINGS                
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
         * SenzingSdk#DEFAULT_INSTANCE_NAME}.
         * 
         * @param instanceName The instance name to initialize the Senzing SDK, or
         *                     <code>null</code> or empty-string to restore the 
         *                     default value.
         * 
         * @return A reference to this instance.
         * 
         * @see SenzingSdk#DEFAULT_INSTANCE_NAME
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
         * @parma verboseLogging <code>true</code> if verbose logging should be
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
         * SenzingSdk#DEFAULT_THREAD_COUNT} if not expliitly set.
         * 
         * @param threadCount The number of Senzing SDK to initialize for use 
         *                    with Senzing operations.
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
         * This method 
         */
        public SenzingSdk create() {
            return new SenzingSdk(this.instanceName,
                                  this.settings,
                                  this.verboseLogging,
                                  this.configId,
                                  this.threadCount);
        }

    }
}
