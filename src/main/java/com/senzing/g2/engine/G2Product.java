package com.senzing.g2.engine;

/**
 * Defines the Java interface to the G2 product functions.  The G2 product
 * functions provide information regarding the Senzing product installation
 * and user license.
 *
 */
public interface G2Product extends G2Fallible
{
  /**
   * Initializes the G2 product API with the specified module name,
   * init parameters and flag indicating verbose logging.  If the
   * <code>G2CONFIGFILE</code> init parameter is absent then the default
   * configuration from the repository is used.
   *
   * @param moduleName A short name given to this instance of the product API.
   * @param iniParams A JSON string containing configuration parameters.
   * @param verboseLogging Enable diagnostic logging which will print a massive
   *                       amount of information to stdout.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int init(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * Uninitializes the G2 product API.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int destroy();

  /**
   * Returns the currently configured license details.
   *
   * @return The JSON document describing the license details.
   */
  String license();

  /**
   * Determines whether a specified license file is valid
   *
   * @param licenseFile The path
   * @param errorResponse The {@link StringBuffer} to write any error response
   *                      to (if an error occurs).
   * @return Zero (0) for valid license, one (1) for invalid, and a negative
   *         number for errors.
   */
  int validateLicenseFile(String licenseFile, StringBuffer errorResponse);


  /**
   * Returns the currently installed version details
   *
   * @return JSON document of version details.
   */
  String version();
}
