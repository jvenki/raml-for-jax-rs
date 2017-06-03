/*
 * Copyright 2013-2017 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.jaxrs.generator.v10.typegenerators;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.raml.jaxrs.generator.CurrentBuild;
import org.raml.jaxrs.generator.builders.BuildPhase;
import org.raml.jaxrs.generator.extension.types.TypeContext;
import org.raml.jaxrs.generator.v10.V10GType;
import org.raml.jaxrs.generator.v10.V10TypeRegistry;

/**
 * A Type Extension to facilitate generation of RAML types without the need for an interface.
 *
 * Note: It is an irony to create SimpleNoInterfaceExtension as a child of SimpleInheritanceExtension... as it should be the other
 * way round. But I just want to have as small a footprint as possible in the 3rd party library.
 *
 * @author jVenki
 * @since 2/6/17
 */
public class SimpleNoInterfaceExtension extends SimpleInheritanceExtension {

  public SimpleNoInterfaceExtension(V10GType originalType, V10TypeRegistry registry, CurrentBuild currentBuild) {
    super(originalType, registry, currentBuild);
  }

  @Override
  public TypeSpec.Builder onType(TypeContext context, TypeSpec.Builder builder, V10GType objectType,
                                 BuildPhase buildPhase) {

    if (buildPhase == null || buildPhase == BuildPhase.INTERFACE) {
      // We dont generate Interfaces. But only if we return something here, the next phase IMPLEMENTATION is called.
      // Therefore lets complete our job in this phase itself.
      return super.buildTypeImplementation(context, objectType);
    } else {
      return null;
    }
  }

  @Override
  protected ClassName getInterfaceName(V10GType originalType, TypeContext context) {
    // We dont generate interfaces. Therefore return NULL so that there is no IMPLEMENTS
    return null;
  }

  @Override
  protected ClassName getImplementationName(V10GType originalType, TypeContext context) {
    return (ClassName) originalType.defaultJavaTypeName(context.getModelPackage());
  }
}
