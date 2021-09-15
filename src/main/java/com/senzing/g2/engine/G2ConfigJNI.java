package com.senzing.g2.engine;

/**
 * Implements the {@link G2Config} interface to call the native implementations
 * of the functions.
 */
@SuppressWarnings({"deprecation", "removal"})
public class G2ConfigJNI implements G2Config 
{
  static {
    System.loadLibrary("G2");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public native int initV2(String   moduleName,
                           String   iniParams,
                           boolean verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int destroy();

  /**
   * {@inheritDoc}
   */
  @Override
  public native long create();

  /**
   * {@inheritDoc}
   */
  @Override
  public native long load(String jsonConfig);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int save(long configHandle, StringBuffer response );

  /**
   * {@inheritDoc}
   */
  @Override
  public native void close(long configHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addDataSource(long configHandle, String code);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addDataSourceWithID(long    configHandle,
                                        String  code,
                                        int     id);

  /**
   * {@inheritDoc}
   */
  @Override
  @Deprecated(since = "2.9.0", forRemoval = true)
  public native int listDataSources(long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listDataSourcesV2(long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addDataSourceV2(long          configHandle,
                                    String        inputJson,
                                    StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteDataSourceV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listEntityClassesV2(long          configHandle,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addEntityClassV2(long         configHandle,
                                     String       inputJson,
                                     StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteEntityClassV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listEntityTypesV2(long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addEntityTypeV2(long          configHandle,
                                    String        inputJson,
                                    StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteEntityTypeV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeatureElementsV2(long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getFeatureElementV2(long          configHandle,
                                        String        inputJson,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureElementV2(long          configHandle,
                                        String        inputJson,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteFeatureElementV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeatureClassesV2(long         configHandle,
                                         StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeaturesV2(long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getFeatureV2(long         configHandle,
                                 String       inputJson,
                                 StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureV2(long         configHandle,
                                 String       inputJson,
                                 StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int modifyFeatureV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteFeatureV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addElementToFeatureV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int modifyElementForFeatureV2(long    configHandle,
                                              String  inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteElementFromFeatureV2(long   configHandle,
                                               String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeatureStandardizationFunctionsV2(
      long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureStandardizationFunctionV2(
      long configHandle, String inputJson, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeatureExpressionFunctionsV2(
      long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureExpressionFunctionV2(
      long configHandle, String inputJson, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int modifyFeatureExpressionFunctionV2(long    configHandle,
                                                      String  inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeatureComparisonFunctionsV2(long         configHandle,
                                                     StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureComparisonFunctionV2(long         configHandle,
                                                   String       inputJson,
                                                   StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureComparisonFunctionReturnCodeV2(
      long configHandle, String inputJson, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeatureDistinctFunctionsV2(long         configHandle,
                                                   StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureDistinctFunctionV2(long         configHandle,
                                                 String       inputJson,
                                                 StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeatureStandardizationFunctionCallsV2(
      long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getFeatureStandardizationFunctionCallV2(
      long configHandle, String inputJson, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureStandardizationFunctionCallV2(
      long configHandle, String inputJson, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteFeatureStandardizationFunctionCallV2(
      long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeatureExpressionFunctionCallsV2(
      long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getFeatureExpressionFunctionCallV2(
      long configHandle, String inputJson, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureExpressionFunctionCallV2(
      long configHandle, String inputJson, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteFeatureExpressionFunctionCallV2(long    configHandle,
                                                          String  inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureExpressionFunctionCallElementV2(
      long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteFeatureExpressionFunctionCallElementV2(
      long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeatureComparisonFunctionCallsV2(
      long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getFeatureComparisonFunctionCallV2(
      long configHandle, String inputJson, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureComparisonFunctionCallV2(
      long configHandle, String inputJson, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteFeatureComparisonFunctionCallV2(long    configHandle,
                                                          String  inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureComparisonFunctionCallElementV2(
      long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteFeatureComparisonFunctionCallElementV2(
      long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listFeatureDistinctFunctionCallsV2(
      long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getFeatureDistinctFunctionCallV2(long         configHandle,
                                                     String       inputJson,
                                                     StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureDistinctFunctionCallV2(long         configHandle,
                                                     String       inputJson,
                                                     StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteFeatureDistinctFunctionCallV2(long    configHandle,
                                                        String  inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addFeatureDistinctFunctionCallElementV2(
      long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteFeatureDistinctFunctionCallElementV2(
      long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listAttributeClassesV2(long         configHandle,
                                           StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listAttributesV2(long         configHandle,
                                     StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getAttributeV2(long         configHandle,
                                   String       inputJson,
                                   StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addAttributeV2(long         configHandle,
                                   String       inputJson,
                                   StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteAttributeV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listRulesV2(long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getRuleV2(long          configHandle,
                              String        inputJson,
                              StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addRuleV2(long          configHandle,
                              String        inputJson,
                              StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteRuleV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listRuleFragmentsV2(long          configHandle,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getRuleFragmentV2(long          configHandle,
                                      String        inputJson,
                                      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addRuleFragmentV2(long          configHandle,
                                      String        inputJson,
                                      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteRuleFragmentV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addConfigSectionV2(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addConfigSectionFieldV2(long    configHandle,
                                            String  inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getCompatibilityVersionV2(long          configHandle,
                                              StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int modifyCompatibilityVersionV2(long   configHandle,
                                                 String inputJson);

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

