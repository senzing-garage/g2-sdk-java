package com.senzing.g2.engine;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.senzing.util.JsonUtilities.normalizeJsonText;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzCoreProductTest extends AbstractTest {
    private SzCoreEnvironment session = null;

    @BeforeAll
    public void initializeEnvironment() {
        this.initializeTestEnvironment();
        String settings = this.getRepoSettings();
        
        String instanceName = this.getClass().getSimpleName();

        this.session = SzCoreEnvironment.newBuilder()
                                      .instanceName(instanceName)
                                      .settings(settings)
                                      .verboseLogging(false)
                                      .build();
    }

    @AfterAll
    public void teardownEnvironment() {
        if (this.session != null) {
            this.session.destroy();
            this.session = null;
        }
        this.teardownTestEnvironment();
    }

    @Test
    void testGetLicense() {
        this.performTest(() -> {
            try {
            SzProduct product = this.session.getProduct();

                String license = product.getLicense();

                Object jsonData = normalizeJsonText(license);

                validateJsonDataMap(
                    jsonData,
                "customer", "contract", "issueDate", "licenseType",
                    "licenseLevel", "billing", "expireDate", "recordLimit");

            } catch (Exception e) {
                fail("Failed testGetLicense test with exception", e);
            }
        });
    }

    @Test
    void testGetVersion() {
        this.performTest(() -> {
            try {
            SzProduct product = this.session.getProduct();

                String version = product.getVersion();

                Object jsonData = normalizeJsonText(version);

                validateJsonDataMap(
                    jsonData,
                    false,
                    "VERSION", "BUILD_NUMBER", "BUILD_DATE", "COMPATIBILITY_VERSION");
          
            } catch (Exception e) {
                fail("Failed testGetVersion test with exception", e);
            }
        });
    }
}
