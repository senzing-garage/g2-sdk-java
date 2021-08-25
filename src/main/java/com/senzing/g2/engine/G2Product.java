package com.senzing.g2.engine;

public interface G2Product extends G2Fallible
{
  /**
   * Initializes the G2 product object
   */
  int initV2(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * Uninitializes the G2 product object.
   */
  int destroy();

  /**
   * Returns the currently configured license details
   *
   * @return JSON document of license details
   */
  String license();

  
  /**
   * Determines whether a specified license file is valid
   *
   * @return 0 for valid license, 1 for invalid, and a negative number for errors.
   */
  int validateLicenseFile(String licenseFile,StringBuffer errorResponse);


  /**
   * Returns the currently configured version details
   *
   * @return JSON document of version details
   */
  String version();

}
