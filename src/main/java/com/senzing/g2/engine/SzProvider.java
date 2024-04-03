package com.senzing.g2.engine;

/**
 * Provides a factory interface for obtaining the references to the Senzing SDK 
 * singleton instances that have been initialized.
 * 
 */
public interface SzProvider {
    /**
     * Provides a reference to the {@link SzProduct} singleton associated with
     * this {@link SzProvider}.
     * 
     * @return The {@link SzProduct} instance associated with this 
     *         {@link SzProvider}.
     * 
     * @throws IllegalStateException If this {@link SzProvider} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzProduct} instance. 
     */
    SzProduct getProduct() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzConfig} singleton associated with
     * this {@link SzProvider}.
     * 
     * @return The {@link SzProduct} instance associated with this 
     *         {@link SzProvider}.
     * 
     * @throws IllegalStateException If this {@link SzProvider} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzConfig} instance. 
     */
    SzConfig getConfig() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzEngine} singleton associated with
     * this {@link SzProvider}.
     * 
     * @return The {@link SzEngine} instance associated with this 
     *         {@link SzProvider}.
     * 
     * @throws IllegalStateException If this {@link SzProvider} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzEngine} instance. 
     */
    SzEngine getEngine() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzConfigManager} singleton associated with
     * this {@link SzProvider}.
     * 
     * @return The {@link SzConfigManager} instance associated with this 
     *         {@link SzProvider}.
     * 
     * @throws IllegalStateException If this {@link SzProvider} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzConfigManager} instance. 
     */
    SzConfigManager getConfigManager() throws IllegalStateException, SzException;

    /**
     * Provides a reference to the {@link SzDiagnostic} singleton associated with
     * this {@link SzProvider}.
     * 
     * @return The {@link SzDiagnostic} instance associated with this 
     *         {@link SzProvider}.
     * 
     * @throws IllegalStateException If this {@link SzProvider} instance has
     *                               been {@linkplain #destroy() destroyed}.
     * 
     * @throws SzException If there was a failure in obtaining or initializing
     *                     the {@link SzDiagnostic} instance. 
     */
    SzDiagnostic getDiagnostic() throws IllegalStateException, SzException;

   /**
    * Destroys this {@link SzProvider} and invalidates any SDK singleton
    * references that has previously provided.  If this instance has already
    * been destroyed then this method has no effect.
    */
   void destroy();

   /**
    * Checks if this instance has had its {@link #destroy()} method called.
    *
    * @return <code>true</code> if this instance has had its {@link #destroy()}
              method called, otherwise <code>false</code>.
    */
   boolean isDestroyed();
}
