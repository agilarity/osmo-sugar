[![Stories in Ready](https://badge.waffle.io/agilarity/osmo-sugar.png?label=ready&title=Ready)](https://waffle.io/agilarity/osmo-sugar)
[![Build Status](https://travis-ci.org/agilarity/osmo-sugar.svg?branch=master)](https://travis-ci.org/agilarity/osmo-sugar)

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
    <version>1.0.1</version>
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
