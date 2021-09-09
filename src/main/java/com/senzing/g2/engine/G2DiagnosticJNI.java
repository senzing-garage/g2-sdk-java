package com.senzing.g2.engine;

/**
 * Implements the {@link G2Diagnostic} to call the native implementations of
 * each function.
 */
public class G2DiagnosticJNI implements G2Diagnostic {
  static {
    System.loadLibrary("G2");
  }

  /**
   * {@inheritDoc}
   */
  public native int initV2(String   moduleName,
                           String   iniParams,
                           boolean  verboseLogging);

  /**
   * {@inheritDoc}
   */
  public native int initWithConfigIDV2(String   moduleName,
                                       String   iniParams,
                                       long     initConfigID,
                                       boolean  verboseLogging);

  /**
   * {@inheritDoc}
   */
  public native int reinitV2(long initConfigID);

  /**
   * {@inheritDoc}
   */
  public native int destroy();

  /**
   * {@inheritDoc}
   */
  public native long getTotalSystemMemory();

  /**
   * {@inheritDoc}
   */
  public native long getAvailableMemory();

  /**
   * {@inheritDoc}
   */
  public native int getPhysicalCores();

  /**
   * {@inheritDoc}
   */
  public native int getLogicalCores();

  /**
   * {@inheritDoc}
   */
  public native int checkDBPerf(int secondsToRun, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getDBInfo(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int findEntitiesByFeatureIDs(String       features,
                                             StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getDataSourceCounts(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getMappingStatistics(boolean      includeInternalFeatures,
                                         StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getGenericFeatures(String       featureType,
                                       long         maximumEstimatedCount,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getEntitySizeBreakdown(long         minimumEntitySize,
                                           boolean      includeInternalFeatures,
                                           StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getEntityDetails(long         entityID,
                                     boolean      includeInternalFeatures,
                                     StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getResolutionStatistics(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getRelationshipDetails(long         relationshipID,
                                           boolean      includeInternalFeatures,
                                           StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getEntityResume(long entityID, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getFeature(long libFeatID, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native long getEntityListBySize(long entitySize);

  /**
   * {@inheritDoc}
   */
  public native String fetchNextEntityBySize(long entityListBySizeHandle);

  /**
   * {@inheritDoc}
   */
  public native void closeEntityListBySize(long entityListBySizeHandle);

  /**
   * {@inheritDoc}
   */
  public native int getEntityListBySizeV2(long          entitySize,
                                          Result<Long>  exportHandle);

  /**
   * {@inheritDoc}
   */
  public native int fetchNextEntityBySizeV2(long          entityListBySizeHandle,
                                            StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  public native int closeEntityListBySizeV2(long entityListBySizeHandle);

  /**
   * {@inheritDoc}
   */
  public native String getLastException();

  /**
   * {@inheritDoc}
   */
  public native int getLastExceptionCode();

  /**
   * {@inheritDoc}
   */
  public native void clearLastException();
}
