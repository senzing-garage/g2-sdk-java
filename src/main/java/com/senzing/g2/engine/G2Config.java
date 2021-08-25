package com.senzing.g2.engine;

public interface G2Config extends G2Fallible
{
  int initV2(String moduleName, String iniParams, boolean verboseLogging);

  int destroy();

  long create();
  long load(String jsonConfig);
  int save(long configHandle, StringBuffer response );
  void close(long configHandle);

  int addDataSource(long configHandle, String code);
  int addDataSourceWithID(long configHandle, String code, int id);

  int listDataSources(long configHandle, StringBuffer response);

  int listDataSourcesV2(long configHandle, StringBuffer response);
  int addDataSourceV2(long configHandle, String inputJson, StringBuffer response);
  int deleteDataSourceV2(long configHandle, String inputJson);

  int listEntityClassesV2(long configHandle, StringBuffer response);
  int addEntityClassV2(long configHandle, String inputJson, StringBuffer response);
  int deleteEntityClassV2(long configHandle, String inputJson);

  int listEntityTypesV2(long configHandle, StringBuffer response);
  int addEntityTypeV2(long configHandle, String inputJson, StringBuffer response);
  int deleteEntityTypeV2(long configHandle, String inputJson);

  int listFeatureElementsV2(long configHandle, StringBuffer response);
  int getFeatureElementV2(long configHandle, String inputJson, StringBuffer response);
  int addFeatureElementV2(long configHandle, String inputJson, StringBuffer response);
  int deleteFeatureElementV2(long configHandle, String inputJson);

  int listFeatureClassesV2(long configHandle, StringBuffer response);

  int listFeaturesV2(long configHandle, StringBuffer response);
  int getFeatureV2(long configHandle, String inputJson, StringBuffer response);
  int addFeatureV2(long configHandle, String inputJson, StringBuffer response);
  int modifyFeatureV2(long configHandle, String inputJson);
  int deleteFeatureV2(long configHandle, String inputJson);
  int addElementToFeatureV2(long configHandle, String inputJson);
  int modifyElementForFeatureV2(long configHandle, String inputJson);
  int deleteElementFromFeatureV2(long configHandle, String inputJson);

  int listFeatureStandardizationFunctionsV2(long configHandle, StringBuffer response);
  int addFeatureStandardizationFunctionV2(long configHandle, String inputJson, StringBuffer response);

  int listFeatureExpressionFunctionsV2(long configHandle, StringBuffer response);
  int addFeatureExpressionFunctionV2(long configHandle, String inputJson, StringBuffer response);
  int modifyFeatureExpressionFunctionV2(long configHandle, String inputJson);
  
  int listFeatureComparisonFunctionsV2(long configHandle, StringBuffer response);
  int addFeatureComparisonFunctionV2(long configHandle, String inputJson, StringBuffer response);
  int addFeatureComparisonFunctionReturnCodeV2(long configHandle, String inputJson, StringBuffer response);
  
  int listFeatureDistinctFunctionsV2(long configHandle, StringBuffer response);
  int addFeatureDistinctFunctionV2(long configHandle, String inputJson, StringBuffer response);

  int listFeatureStandardizationFunctionCallsV2(long configHandle, StringBuffer response);
  int getFeatureStandardizationFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  int addFeatureStandardizationFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  int deleteFeatureStandardizationFunctionCallV2(long configHandle, String inputJson);

  int listFeatureExpressionFunctionCallsV2(long configHandle, StringBuffer response);
  int getFeatureExpressionFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  int addFeatureExpressionFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  int deleteFeatureExpressionFunctionCallV2(long configHandle, String inputJson);
  int addFeatureExpressionFunctionCallElementV2(long configHandle, String inputJson);
  int deleteFeatureExpressionFunctionCallElementV2(long configHandle, String inputJson);

  int listFeatureComparisonFunctionCallsV2(long configHandle, StringBuffer response);
  int getFeatureComparisonFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  int addFeatureComparisonFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  int deleteFeatureComparisonFunctionCallV2(long configHandle, String inputJson);
  int addFeatureComparisonFunctionCallElementV2(long configHandle, String inputJson);
  int deleteFeatureComparisonFunctionCallElementV2(long configHandle, String inputJson);

  int listFeatureDistinctFunctionCallsV2(long configHandle, StringBuffer response);
  int getFeatureDistinctFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  int addFeatureDistinctFunctionCallV2(long configHandle, String inputJson, StringBuffer response);
  int deleteFeatureDistinctFunctionCallV2(long configHandle, String inputJson);
  int addFeatureDistinctFunctionCallElementV2(long configHandle, String inputJson);
  int deleteFeatureDistinctFunctionCallElementV2(long configHandle, String inputJson);

  int listAttributeClassesV2(long configHandle, StringBuffer response);

  int listAttributesV2(long configHandle, StringBuffer response);
  int getAttributeV2(long configHandle, String inputJson, StringBuffer response);
  int addAttributeV2(long configHandle, String inputJson, StringBuffer response);
  int deleteAttributeV2(long configHandle, String inputJson);

  int listRulesV2(long configHandle, StringBuffer response);
  int getRuleV2(long configHandle, String inputJson, StringBuffer response);
  int addRuleV2(long configHandle, String inputJson, StringBuffer response);
  int deleteRuleV2(long configHandle, String inputJson);

  int listRuleFragmentsV2(long configHandle, StringBuffer response);
  int getRuleFragmentV2(long configHandle, String inputJson, StringBuffer response);
  int addRuleFragmentV2(long configHandle, String inputJson, StringBuffer response);
  int deleteRuleFragmentV2(long configHandle, String inputJson);

  int addConfigSectionV2(long configHandle, String inputJson);
  int addConfigSectionFieldV2(long configHandle, String inputJson);

  int getCompatibilityVersionV2(long configHandle, StringBuffer response);
  int modifyCompatibilityVersionV2(long configHandle, String inputJson);

}

