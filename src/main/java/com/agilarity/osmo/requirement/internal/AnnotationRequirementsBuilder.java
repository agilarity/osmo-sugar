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

package com.agilarity.osmo.requirement.internal;

import static com.agilarity.osmo.requirement.internal.AnnotationAccesor.getValue;
import static java.lang.Character.toUpperCase;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.agilarity.osmo.requirement.AnnotatedRequirement;
import com.agilarity.osmo.requirement.internal.locator.RequirementStepLocator;
import com.agilarity.osmo.requirement.name.RequirementNamingStrategy;

import osmo.tester.model.FSM;
import osmo.tester.model.ModelFactory;
import osmo.tester.model.TestModels;

/**
 * Build the list of annotated requirements.
 */
public class AnnotationRequirementsBuilder {
  private final transient RequirementNamingStrategy requirementNamingStrategy;
  private final transient Class<? extends Annotation> requirementAnnotationClass;
  private final transient Collection<AnnotatedRequirement> annotatedRequirements;

  /**
   * Create builder.
   */
  public AnnotationRequirementsBuilder(final FSM fsm, final ModelFactory modelFactory,
      final RequirementNamingStrategy requirementNamingStrategy,
      final Class<? extends Annotation> requirementAnnotationClass) {
    super();
    this.requirementNamingStrategy = requirementNamingStrategy;
    this.requirementAnnotationClass = requirementAnnotationClass;
    annotatedRequirements = new ArrayList<AnnotatedRequirement>();
    buildAnnotatedRequirements(fsm, modelFactory);
  }

  /**
   * @return the annotatedRequirements.
   */
  public Collection<AnnotatedRequirement> getAnnotatedRequirements() {
    return annotatedRequirements;
  }

  private void buildAnnotatedRequirements(final FSM fsm, final ModelFactory modelFactory) {
    final List<Method> requirementMethods = fetchRequirementMethods(modelFactory);
    requirementMethods.forEach(method -> makeAnnotatedRequirements(fetchSteps(fsm), method));
  }

  private void makeAnnotatedRequirements(final Collection<String> steps, final Method method) {
    final String id = getValue(method.getAnnotation(requirementAnnotationClass));

    final List<String> requirementSteps = new RequirementStepLocator(method, steps,
        requirementAnnotationClass).getSteps();

    requirementSteps.forEach(step -> addAnnotatedRequirementForStep(id, capitalize(step),
        method.getName()));
  }

  private void addAnnotatedRequirementForStep(final String id, final String step,
      final String method) {
    annotatedRequirements
        .add(new AnnotatedRequirement(requirementNamingStrategy, id, step, method));
  }

  private List<Method> fetchRequirementMethods(final ModelFactory modelFactory) {

    final TestModels testModels = new TestModels();
    modelFactory.createModelObjects(testModels);

    return testModels.getModels().stream()
        .flatMap(modelObject -> Arrays.stream(modelObject.getObject().getClass().getMethods()))
        .filter(method -> method.getAnnotation(requirementAnnotationClass) != null)
        .collect(toList());
  }

  private List<String> fetchSteps(final FSM fsm) {
    return fsm.getTransitions().stream().map(fsmTransition -> fsmTransition.getName().toString())
        .collect(toList());
  }

  private String capitalize(final String word) {
    return toUpperCase(word.charAt(0)) + word.substring(1);
  }
}
