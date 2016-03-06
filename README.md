[![Stories in Ready](https://badge.waffle.io/agilarity/osmo-sugar.png?label=ready&title=Ready)](https://waffle.io/agilarity/osmo-sugar)
[![Build Status](https://travis-ci.org/agilarity/osmo-sugar.svg?branch=master)](https://travis-ci.org/agilarity/osmo-sugar)

OSMO Sugar provides support for model based testing with Teemu Kanstrén's [OSMO Tester](https://github.com/mukatee/osmo).

###Sugar
1. The <code>RequirementEnforcingOsmoTester</code> is a thin wrapper over <code>OSMOTester</code> that guarantees every requirement has been met.
2. The <code>RequirementAnnotationListener</code> automates requirement registration and coverage. Just add <code>@Requirement</code> annotations to methods that assert requirements. The default <code>RequirementNamingStrategy</code> will prefer the value provided in the <code>@Requirement</code> annotation. Otherwise, it will use the name of the annotated method.

###Getting started.
1. Add the dependency.
```xml
<dependency>
    <groupId>com.agilarity</groupId>
    <artifactId>osmo-sugar</artifactId>
    <version>2.0.5</version>
</dependency>
```
2. Write single step models that cover the user actions under test. Add a <code>Requirements</code> field to the model so that OSMO knows you want to track requirements.
3. Add a <code>@Requirement</code> annotation to each method that assures a requirement scenario has been covered.
4. Run your test class from your favorite testing tool, like [JUnit](http://junit.org/) or [TestNG](http://testng.org/).

See the <code>com.agilarity.osmo.example.vend</code> in the test package for a vending machine example.