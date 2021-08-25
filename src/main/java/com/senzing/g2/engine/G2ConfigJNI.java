package com.senzing.g2.engine;

public class G2ConfigJNI implements G2Config 
{
  static {
    System.loadLibrary("G2");
  }

  public native int initV2(String moduleName, String iniParams, boolean verboseLogging);

  public native int destroy();

  public native long create();
  public native long load(String jsonConfig);
  public native int save(long configHandle, StringBuffer response );
  public native void close(long configHandle);

  public native int addDataSource(long configHandle, String code);
  public native int addDataSourceWithID(long configHandle, String code, final int id);

  public native int listDataSources(long configHandle, StringBuffer response);
  
  public native int listDataSourcesV2(long configHandle, StringBuffer response);
  public native int addDataSourceV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteDataSourceV2(long configHandle, String inputJson);

  public native int listEntityClassesV2(long configHandle, StringBuffer response);
  public native int addEntityClassV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteEntityClassV2(long configHandle, String inputJson);

  public native int listEntityTypesV2(long configHandle, StringBuffer response);
  public native int addEntityTypeV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteEntityTypeV2(long configHandle, String inputJson);

  public native int listFeatureElementsV2(long configHandle, StringBuffer response);
  public native int getFeatureElementV2(long configHandle, String inputJson, StringBuffer response);
  public native int addFeatureElementV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteFeatureElementV2(long configHandle, String inputJson);

  public native int listFeatureClassesV2(long configHandle, StringBuffer response);

  public native int listFeaturesV2(long configHandle, StringBuffer response);
  public native int getFeatureV2(long configHandle, String inputJson, StringBuffer response);
  public native int addFeatureV2(long configHandle, String inputJson, StringBuffer response);
  public native int modifyFeatureV2(long configHandle, String inputJson);
  public native int deleteFeatureV2(long configHandle, String inputJson);
  public native int addElementToFeatureV2(long configHandle, String inputJson);
  public native int modifyElementForFeatureV2(long configHandle, String inputJson);
  public native int deleteElementFromFeatureV2(long configHandle, String inputJson);

  public native int listFeatureStandardizationFunctionsV2(long configHandle, StringBuffer response);
  public native int addFeatureStandardizationFunctionV2(long configHandle, String inputJson, StringBuffer response);

  public native int listFeatureExpressionFunctionsV2(long configHandle, StringBuffer response);
  public native int addFeatureExpressionFunctionV2(long configHandle, String inputJson, StringBuffer response);
  public native int modifyFeatureExpressionFunctionV2(long configHandle, String inputJson);
  
  public native int listFeatureComparisonFunctionsV2(long configHandle, StringBuffer response);
  public native int addFeatureComparisonFunctionV2(long configHandle, String inputJson, StringBuffer response);
  public native int addFeatureComparisonFunctionReturnCodeV2(long configHandle, String inputJson, StringBuffer response);
  
  public native int listFeatureDistinctFunctionsV2(long configHandle, StringBuffer response);
  public native int addFeatureDistinctFunctionV2(long configHandle, String inputJson, StringBuffer response);

  public native int listFeatureStandardizationFunctionCallsV2(long configHandle, StringBuffer response);
  public native int getFeatureStandardizationFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  public native int addFeatureStandardizationFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteFeatureStandardizationFunctionCallV2(long configHandle, String inputJson);

  public native int listFeatureExpressionFunctionCallsV2(long configHandle, StringBuffer response);
  public native int getFeatureExpressionFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  public native int addFeatureExpressionFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteFeatureExpressionFunctionCallV2(long configHandle, String inputJson);
  public native int addFeatureExpressionFunctionCallElementV2(long configHandle, String inputJson);
  public native int deleteFeatureExpressionFunctionCallElementV2(long configHandle, String inputJson);

  public native int listFeatureComparisonFunctionCallsV2(long configHandle, StringBuffer response);
  public native int getFeatureComparisonFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  public native int addFeatureComparisonFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteFeatureComparisonFunctionCallV2(long configHandle, String inputJson);
  public native int addFeatureComparisonFunctionCallElementV2(long configHandle, String inputJson);
  public native int deleteFeatureComparisonFunctionCallElementV2(long configHandle, String inputJson);

  public native int listFeatureDistinctFunctionCallsV2(long configHandle, StringBuffer response);
  public native int getFeatureDistinctFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  public native int addFeatureDistinctFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteFeatureDistinctFunctionCallV2(long configHandle, String inputJson);
  public native int addFeatureDistinctFunctionCallElementV2(long configHandle, String inputJson);
  public native int deleteFeatureDistinctFunctionCallElementV2(long configHandle, String inputJson);

  public native int listAttributeClassesV2(long configHandle, StringBuffer response);

  public native int listAttributesV2(long configHandle, StringBuffer response);
  public native int getAttributeV2(long configHandle, String inputJson, StringBuffer response);
  public native int addAttributeV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteAttributeV2(long configHandle, String inputJson);

  public native int listRulesV2(long configHandle, StringBuffer response);
  public native int getRuleV2(long configHandle, String inputJson, StringBuffer response);
  public native int addRuleV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteRuleV2(long configHandle, String inputJson);

  public native int listRuleFragmentsV2(long configHandle, StringBuffer response);
  public native int getRuleFragmentV2(long configHandle, String inputJson, StringBuffer response);
  public native int addRuleFragmentV2(long configHandle, String inputJson, StringBuffer response);
  public native int deleteRuleFragmentV2(long configHandle, String inputJson);

  public native int addConfigSectionV2(long configHandle, String inputJson);
  public native int addConfigSectionFieldV2(long configHandle, String inputJson);

  public native int getCompatibilityVersionV2(long configHandle, StringBuffer response);
  public native int modifyCompatibilityVersionV2(long configHandle, String inputJson);

  public native String getLastException();
  public native int getLastExceptionCode();
  public native void clearLastException();
}

