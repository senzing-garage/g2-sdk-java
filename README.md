# g2-sdk-java

## Overview

The Senzing G2 Java SDK provides the Java interface to the native Senzing SDK's.
This repository is dependent on the native Senzing G2 library that is part of
the Senzing G2 product via JNI and will not actually function without it.
Previously, this SDK was only available as part of the Senzing G2 product, but
is being made available as open source for publishing on the Maven Central 
Repository.

### Building

To build simply execute:

```console
mvn install
```

The JAR file will be contained in the `target` directory under the
name `g2.jar`.
