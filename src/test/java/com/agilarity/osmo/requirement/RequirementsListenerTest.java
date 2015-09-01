package com.agilarity.osmo.requirement;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import osmo.tester.OSMOTester;
import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

public class RequirementsListenerTest {
  private Requirements requirements;
  private OSMOTester osmoTester;

  @BeforeMethod
  public void before() throws NoSuchMethodException, SecurityException {
    requirements = new Requirements();
    osmoTester = new OSMOTester();
    osmoTester.addListener(new RequirementAnnotationListener());
  }

  @Test
  public void shouldAddRequirements() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new DoSomething(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the there will be one requirement for each annotation
    assertThat(requirements.getRequirements()).containsAll(
        asList("DoSomething.shouldDoSomething", "DoSomething.shouldDoSomethingElse"));
  }

  @Test
  public void shouldCoverRequirements() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new DoSomething(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the requirements will be covered
    assertThat(requirements.getMissingCoverage()).isEmpty();
  }

  @Test
  public void shouldNotRequireAnnotations() {
    // GIVEN a model without requirement annotations
    osmoTester.addModelObject(new NoRequirementAnnotations(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the requirements will be covered
    assertThat(requirements.getMissingCoverage()).isEmpty();
  }

  @Test
  public void shouldRequireStep() {
    // GIVEN a model with a requirement that does not match a step
    osmoTester.addModelObject(new NoRequirementStep(requirements));

    try {
      // WHEN the tests are generated
      osmoTester.generate(1);
      fail("Expected MissingRequirementStepException");
    } catch (final MissingRequirementStepException e) {
      // THEN the missing step error will be reported
      assertThat(e.getMessage())
          .isEqualTo(
              "Add step [MyStep] to @Requirement or end of method for [NoRequirementStep.doesNotMatchStep]");
    }
  }

  public class DoSomething {
    @SuppressWarnings("unused")
    private final Requirements requirements;

    public DoSomething(final Requirements requirements) {
      super();
      this.requirements = requirements;
    }

    @TestStep
    public void doSomething() {
    }

    @Requirement
    public void shouldDoSomething() {
    }

    @Requirement("doSomething")
    public void shouldDoSomethingElse() {
    }
  }

  public class NoRequirementAnnotations {
    // The requirements are used by RequirementAnnotationListener, not the model directly.
    @SuppressWarnings("unused")
    private final Requirements requirements;

    public NoRequirementAnnotations(final Requirements requirements) {
      super();
      this.requirements = requirements;
    }

    @TestStep
    public void doNotAssertAnything() {
    }
  }

  public class NoRequirementStep {
    // The requirements are used by RequirementAnnotationListener, not the model directly.
    @SuppressWarnings("unused")
    private final Requirements requirements;

    public NoRequirementStep(final Requirements requirements) {
      super();
      this.requirements = requirements;
    }

    @TestStep
    public void myStep() {
    }

    @Requirement
    public void doesNotMatchStep() {

    }
  }
}
