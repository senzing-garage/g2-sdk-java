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
  @Override
  public native int init(String   moduleName,
                         String   iniParams,
                         boolean  verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int initWithConfigID(String   moduleName,
                                     String   iniParams,
                                     long     initConfigID,
                                     boolean  verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reinit(long initConfigID);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int destroy();

  /**
   * {@inheritDoc}
   */
  @Override
  public native long getTotalSystemMemory();

  /**
   * {@inheritDoc}
   */
  @Override
  public native long getAvailableMemory();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getPhysicalCores();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getLogicalCores();

  /**
   * {@inheritDoc}	
   */
  @Override
  public native int purgeRepository();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int checkDBPerf(int secondsToRun, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getDBInfo(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findEntitiesByFeatureIDs(String       features,
                                             StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getDataSourceCounts(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getMappingStatistics(boolean      includeInternalFeatures,
                                         StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getGenericFeatures(String       featureType,
                                       long         maximumEstimatedCount,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntitySizeBreakdown(long         minimumEntitySize,
                                           boolean      includeInternalFeatures,
                                           StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityDetails(long         entityID,
                                     boolean      includeInternalFeatures,
                                     StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getResolutionStatistics(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getRelationshipDetails(long         relationshipID,
                                           boolean      includeInternalFeatures,
                                           StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityResume(long entityID, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getFeature(long libFeatID, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityListBySize(long          entitySize,
                                        Result<Long>  exportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int fetchNextEntityBySize(long          entityListBySizeHandle,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int closeEntityListBySize(long entityListBySizeHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native String getLastException();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getLastExceptionCode();

  /**
   * {@inheritDoc}
   */
  @Override
  public native void clearLastException();
}
