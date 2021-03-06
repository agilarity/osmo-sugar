/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Joseph A. Cruz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agilarity.osmo.requirement;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.agilarity.osmo.requirement.errors.MissingRequirementsObjectException;
import com.agilarity.osmo.requirement.internal.AnnotationRequirementsBuilder;
import com.agilarity.osmo.requirement.name.RequirementNamingStrategy;
import com.agilarity.osmo.requirement.name.SimpleRequirementNamingStrategy;

import osmo.tester.OSMOConfiguration;
import osmo.tester.generator.listener.AbstractListener;
import osmo.tester.generator.testsuite.TestCase;
import osmo.tester.generator.testsuite.TestCaseStep;
import osmo.tester.model.FSM;
import osmo.tester.model.ModelFactory;
import osmo.tester.model.Requirements;

/**
 * Add requirements from the test model and cover them on success.
 */
public class RequirementAnnotationListener extends AbstractListener {
  private final transient ModelFactory modelFactory;
  private final transient RequirementNamingStrategy requirementNamingStrategy;
  private transient Requirements requirements;
  private transient AnnotatedRequirement failingRequirement;
  private transient Collection<AnnotatedRequirement> passingRequirements;
  private transient Collection<AnnotatedRequirement> annotatedRequirements;
  private transient TestCase failingTestCase;
  private transient Throwable error;
  private transient Class<? extends Annotation> requirementAnnotationClass = Requirement.class;

  /**
   * Use the @{code RequirementNamingStrategy} by default.
   */
  public RequirementAnnotationListener(final ModelFactory modelFactory) {
    this(modelFactory, new SimpleRequirementNamingStrategy());
  }

  /**
   * Inject the {@code RequirementNamingStrategy}.
   */
  public RequirementAnnotationListener(final ModelFactory modelFactory,
      final RequirementNamingStrategy requirementNamingStrategy) {
    super();
    this.modelFactory = modelFactory;
    this.requirementNamingStrategy = requirementNamingStrategy;
  }

  /**
   * @return the failingRequirement.
   */
  public AnnotatedRequirement getFailingRequirement() {
    return failingRequirement;
  }

  /**
   * @return the passingRequirements.
   */
  public Collection<AnnotatedRequirement> getPassingRequirements() {
    return passingRequirements;
  }

  /**
   * @return the annotatedRequirements.
   */
  public Collection<AnnotatedRequirement> getAnnotatedRequirements() {
    return annotatedRequirements;
  }

  /**
   * @return the failing test case.
   */
  public TestCase getFailingTestCase() {
    return failingTestCase;
  }

  /**
   * @return the error.
   */
  public Throwable getError() {
    return error;
  }

  /**
   * @return the requirementAnnotationClass.
   */
  public Class<? extends Annotation> getRequirementAnnotationClass() {
    return requirementAnnotationClass;
  }

  /**
   * @param requirementAnnotationClass the annotationClass to set.
   */
  public void setRequirementAnnotationClass(
      final Class<? extends Annotation> requirementAnnotationClass) {
    this.requirementAnnotationClass = requirementAnnotationClass;
  }

  /**
   * Register the requirements for a step.
   */
  @Override
  public void init(final long seed, final FSM fsm, final OSMOConfiguration config) {
    requirements = fsm.getRequirements();
    if (requirements == null) {
      throw new MissingRequirementsObjectException();
    }

    passingRequirements = new ArrayList<AnnotatedRequirement>();

    final AnnotationRequirementsBuilder builder = new AnnotationRequirementsBuilder(fsm,
        modelFactory, requirementNamingStrategy, requirementAnnotationClass);
    annotatedRequirements = builder.getAnnotatedRequirements();
    annotatedRequirements.forEach(requirement -> requirements.add(requirement.getName()));
  }

  /**
   * Cover the requirements because the step did not fail.
   */
  @Override
  public void stepDone(final TestCaseStep step) {
    getAnnotatedRequirementsForStep(step.getName()).forEach(
        requirement -> passRequirement(requirement));

  }

  private void passRequirement(final AnnotatedRequirement requirement) {
    requirements.covered(requirement.getName());
    passingRequirements.add(requirement);
  }

  /**
   * Uncover the failing requirement when known or all steps requirements otherwise.
   */
  @Override
  public void testError(final TestCase test, final Throwable error) {
    this.error = error;
    failingTestCase = test;
    final String step = test.getCurrentStep().getName();

    if (!uncoverFailingRequirement(step, error)) {
      uncoverAllStepRequirements(step);
    }
  }

  private void uncoverAllStepRequirements(final String step) {
    final List<String> list = getAnnotatedRequirementsForStep(step).stream()
        .map(requirement -> requirement.getName()).collect(toList());
    requirements.getFullCoverage().removeAll(list);
  }

  private boolean uncoverFailingRequirement(final String step, final Throwable error) {
    final String method = getThrowingMethod(error);
    final Optional<AnnotatedRequirement> sourceOfError = getAnnotatedRequirementsForStep(step)
        .stream().filter(requirement -> requirement.getMethod().equals(method)).findFirst();

    if (sourceOfError.isPresent()) {
      failingRequirement = sourceOfError.get();
      final List<String> nameAsList = asList(failingRequirement.getName());

      passingRequirements.remove(failingRequirement);

      // Use removeAll instead of remove to assure every reference to the name is removed.
      requirements.getFullCoverage().removeAll(nameAsList);

      return true;
    }

    return false;
  }

  private String getThrowingMethod(final Throwable error) {
    final StackTraceElement throwingMethod = error.getStackTrace()[0];
    return throwingMethod.getMethodName();
  }

  private List<AnnotatedRequirement> getAnnotatedRequirementsForStep(final String step) {
    return annotatedRequirements.stream().filter(requirement -> requirement.getStep().equals(step))
        .collect(toList());
  }
}
