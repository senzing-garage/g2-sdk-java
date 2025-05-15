# g2-sdk-java

If you are beginning your journey with [Senzing],
please start with [Senzing Quick Start guides].

You are in the [Senzing Garage] where projects are "tinkered" on.
Although this GitHub repository may help you understand an approach to using Senzing,
it's not considered to be "production ready" and is not considered to be part of the Senzing product.
Heck, it may not even be appropriate for your application of Senzing!

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

[Senzing Garage]: https://github.com/senzing-garage
[Senzing Quick Start guides]: https://docs.senzing.com/quickstart/
[Senzing]: https://senzing.com/
