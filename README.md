[![Stories in Ready](https://badge.waffle.io/agilarity/testng-osmo.png?label=ready&title=Ready)](https://waffle.io/agilarity/testng-osmo)
[![Build Status](https://travis-ci.org/agilarity/testng-osmo.svg?branch=master)](https://travis-ci.org/agilarity/testng-osmo)

This project provides support for model based testing with Teemu Kanstren's [OSMO Tester](https://code.google.com/p/osmo/).

###Sugar
1. <code>OsmoTestRunner</code> is a thin wrapper over <code>OSMOTester</code> that guarantees every requirement has been met.
2. <code>Feature</code> is a model that focuses on one step and automatically adds a requirement.  

### Usage:
1. Add dependency.
```xml
<dependency>
    <groupId>com.agilarity</groupId>
    <artifactId>osmo-sugar</artifactId>
    <version>1.0.0</version>
</dependency>
```
2. Add the runner to your test method.
```java
	public class SmokeTest {
	
	    @Test
	    public void shouldDetectSmokeLevels() {
			OSMOConfiguration configuration = createSmokeTestConfiguration();
	        new OsmoTestRunner(configuration).generateTests();
	    }
	}
```
3. Run your test class from your favorite testing tool.
