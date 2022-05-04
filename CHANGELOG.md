# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
[markdownlint](https://dlaa.me/markdownlint/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.0.0] - 2022-05-04

### Added in 3.0.0
- Added the following functions to `com.senzing.g2.engine.G2Engine`:
  - `findInterestingEntitiesByEntityID(...)`
  - `findInterestingEntitiesByRecordID(...)`
  - `findPathExcludingByEntityID(...)`
  - `findPathExcluduingByRecordID(...)`
  - `findPathIncludingSourceByEntityID(...)`
  - `findPathIncludingSourceByRecordID(...)`
  - `howEntityByEntityID(long, StringBuffer)`
  - `howEntityByEntityID(long, long, StringBuffer)`
  - `getVirtualEntityByRecordID(String, StringBuffer)`
  - `getVirtualEntityByRecordID(String, long, StringBuffer)`
- Added the following functions to `com.senzing.g2.engine.G2Product`:
  - `int validateLicenseStringBase64(String, StringBuffer)`

### Removed in 3.0.0
- The `G2Audit` functions including the entire `com.senzing.g2.engine.internal`
  package have been removed.  These funtions are no longer available for 
  external use.  Instead the `WithInfo` function variants in `G2Engine` should
  be used to incrementally aggregate repository statistics. 
- The Entity Type and Entity Class functions in `com.senzing.g2.engine.G2Config`
  are no longer relevant since entity types and entity classes are no longer 
  used by Senzing. The following functions have been removed:
  - `addDataSource(long, String)`
  - `addDataSourceWithID(long, String, int)`
  - `listEntityClassesV2()`
  - `addEntityClassV2()`
  - `deleteEntityClassV2()`
  - `listEntityTypesV2()`
  - `addEntityTypeV2()`
  - `deleteEntityTypeV2()`
- Many functions that were intended only for internal use have been removed
  from `com.senzing.g2.engine.G2Config`.  These functions have no replacement
  and should be removed from your application if they are used:
  - `listFeatureElementsV2()`
  - `getFeatureElementV2()`
  - `addFeatureElementV2()`
  - `deleteFeatureElementV2()`
  - `listFeatureClassesV2()`
  - `listFeaturesV2()`
  - `getFeatureV2()`
  - `addFeatureV2()`
  - `modifyFeatureV2()`
  - `deleteFeatureV2()`
  - `addElementToFeatureV2()`
  - `modifyElementForFeatureV2()`
  - `deleteElementFromFeatureV2()`
  - `listFeatureStandardizationFunctionsV2()`
  - `addFeatureStandardizationFunctionV2()`
  - `listFeatureExpressionFunctionsV2()`
  - `addFeatureExpressionFunctionV2()`
  - `modifyFeatureExpressionFunctionV2()`
  - `listFeatureComparisonFunctionsV2()`
  - `addFeatureComparisonFunctionV2()`
  - `addFeatureComparisonFunctionReturnCodeV2()`
  - `listFeatureDistinctFunctionsV2()`
  - `addFeatureDistinctFunctionV2()`
  - `listFeatureStandardizationFunctionCallsV2()`
  - `getFeatureStandardizationFunctionCallV2()`
  - `addFeatureStandardizationFunctionCallV2()`
  - `deleteFeatureStandardizationFunctionCallV2()`
  - `listFeatureExpressionFunctionCallsV2()`
  - `listFeatureExpressionFunctionCallsV2()`
  - `addFeatureExpressionFunctionCallV2()`
  - `deleteFeatureExpressionFunctionCallV2()`
  - `addFeatureExpressionFunctionCallElementV2()`
  - `deleteFeatureExpressionFunctionCallElementV2()`
  - `listFeatureComparisonFunctionCallsV2()`
  - `getFeatureComparisonFunctionCallV2()`
  - `addFeatureComparisonFunctionCallV2()`
  - `deleteFeatureComparisonFunctionCallV2()`
  - `addFeatureComparisonFunctionCallElementV2()`
  - `deleteFeatureComparisonFunctionCallElementV2()`
  - `listFeatureDistinctFunctionCallsV2()`
  - `getFeatureDistinctFunctionCallV2()`
  - `addFeatureDistinctFunctionCallV2()`
  - `deleteFeatureDistinctFunctionCallV2()`
  - `addFeatureDistinctFunctionCallElementV2()`
  - `deleteFeatureDistinctFunctionCallElementV2()`
  - `listAttributeClassesV2()`
  - `listAttributesV2()`
  - `getAttributeV2()`
  - `addAttributeV2()`
  - `deleteAttributeV2()`
  - `listRulesV2()`
  - `getRuleV2()`
  - `addRuleV2()`
  - `deleteRuleV2()`
  - `listRuleFragmentsV2()`
  - `getRuleFragmentV2()`
  - `addRuleFragmentV2()`
  - `deleteRuleFragmentV2()`
  - `addConfigSectionV2()`
  - `addConfigSectionFieldV2()`
  - `getCompatibilityVersionV2()`
  - `modifyCompatibilityVersionV2()`

### Changed in 3.0.0
- Changed "flag" constants from type `int` to `long`.  This includes the 
  constant definitions (e.g.: `G2Engine.G2_ENTITY_INCLUDE_ALL_FEATURES`) and
  in function parameters.
- Removed "V2" and "V3" suffixes from functions, replacing the "V1" versions
  that lacked suffix.
  - `com.senzing.g2.engine.G2Engine`:
    - `initV2()` --> `init()`
    - `initWithConfigIDV2()` --> `initWithConfigID()`
    - `reinitV2()` --> `reinit()`
    - `getRecordV2()` --> `getRecord()`
    - `searchByAttributesV2()` --> `searchByAttributes()`
    - `getEntityByEntityIDV2()` --> `getEntityByEntityID()`
    - `getEntityByRecordIDV2()` --> `getEntityByRecordID()`
    - `findPathByEntityIDV2()` --> `findPathByEntityID()`
    - `findPathByRecordIDV2()` --> `findPathByRecordID()`
    - `findNetworkByEntityIDV2()` --> `findNetworkByEntityID()`
    - `findNetworkByRecordIDV2()` --> `findNetworkByRecordIDV2()`
    - `whyEntityByEntityIDV2()` --> `whyEntityByEntityID()`
    - `whyEntityByRecordIDV2()` --> `whyEntityByRecordID()`
    - `whyRecordsV2()` --> `whyRecords()`
    - `whyEntitiesV2()` --> `whyEntities()`
  - `com.senzing.g2.engine.G2Config`:
    - `initV2()` --> `init()`
    - `listDataSourcesV2()` --> `listDataSources()`
    - `addDataSourceV2()` --> `addDataSource()`
    - `deleteDataSourceV2()` --> `deleteDataSource()`
  - `com.senzing.g2.engine.G2Product`:
    - `initV2()` --> `init()`
  - `com.senzing.g2.engine.G2Diagnostic`:
    - `initV2()` --> `init()`
    - `initWithConfigIDV2()` --> `initWithConfigID()`
    - `reinitV2()` --> `reinit()`
    - `getEntityListBySizeV2()` --> `getEntityListBySize()`
    - `fetchNextEntityBySizeV2()` --> `fetchNextEntityBySize()`
    - `closeEntityListBySizeV2()` --> `closeEntityListBySize()`
  - `com.senzing.g2.engine.G2ConfigMgr`:
    - `initV2()` --> `init()`
- The following functions no longer return `long` but take a `Result<Long>`
  parameter and now have an `int` return code (non-zero indicating an error)
  - `com.senzing.g2.engine.G2Engine`
    - `exportJSONEntityReportV2()`
  - `com.senzing.g2.engine.G2Config`
    - `long create()` --> `int create(Result<Long>)`
    - `long load(String)` --> `int load(String, Result<Long>)`
- The following functions that previously returned `void` now return `int` to
  indicate if a failure returned with a non-zero return code.
  - `com.senzing.g2.engine.G2Config`
    - `void close(long)` --> `int close(long)`

## [2.9.0] - 2021-09-15

### Initial open-source release
