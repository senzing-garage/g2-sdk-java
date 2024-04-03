package com.senzing.g2.engine;

/**
 * Interface implemented by any Senzing interface provided by {@link SzProvider}.
 * 
 * This allows you to pivot from an {@link SzEngine} that may encounter a 
 * configuration exception to an {@link SzConfigManager} that will allow you
 * to check if the active configuration from the {@link SzEngine} is still
 * the current default configuration reported by the {@link SzConfigManager}.
 */
public interface SzProvided {
    /**
     * Gets the {@link SzProvider} to which this instance belongs.
     * 
     * @return The {@link SzProvider} to which this instance belongs.
     */
    SzProvider getProvider();
}
