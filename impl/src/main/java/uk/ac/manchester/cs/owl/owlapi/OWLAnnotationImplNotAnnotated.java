/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package uk.ac.manchester.cs.owl.owlapi;

import static org.semanticweb.owlapi.util.OWLAPIPreconditions.checkNotNull;

import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.OWLObjectTypeIndexProvider;

/**
 * @author Matthew Horridge, The University Of Manchester, Bio-Health
 *         Informatics Group
 * @since 2.0.0
 */
public class OWLAnnotationImplNotAnnotated extends OWLObjectImpl implements
        OWLAnnotation {

    private static final long serialVersionUID = 40000L;
    @Nonnull
    private final OWLAnnotationProperty property;
    @Nonnull
    private final OWLAnnotationValue value;

    @Override
    protected int index() {
        return OWLObjectTypeIndexProvider.ANNOTATION_TYPE_INDEX_BASE + 1;
    }

    /**
     * @param property
     *        annotation property
     * @param value
     *        annotation value
     */
    public OWLAnnotationImplNotAnnotated(
            @Nonnull OWLAnnotationProperty property,
            @Nonnull OWLAnnotationValue value) {
        this.property = checkNotNull(property, "property cannot be null");
        this.value = checkNotNull(value, "value cannot be null");
    }

    @Override
    public Stream<OWLAnnotation> annotations() {
        return Stream.empty();
    }

    @Override
    public OWLAnnotationProperty getProperty() {
        return property;
    }

    @Override
    public OWLAnnotationValue getValue() {
        return value;
    }

    @Override
    public OWLAnnotation getAnnotatedAnnotation(
            @Nonnull Set<OWLAnnotation> annotations) {
        if (annotations.isEmpty()) {
            return this;
        }
        return new OWLAnnotationImpl(property, value, annotations.stream());
    }

    @Override
    public boolean isDeprecatedIRIAnnotation() {
        return property.isDeprecated() && value instanceof OWLLiteral
                && ((OWLLiteral) value).isBoolean()
                && ((OWLLiteral) value).parseBoolean();
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) && obj instanceof OWLAnnotation) {
            OWLAnnotation other = (OWLAnnotation) obj;
            return other.getProperty().equals(property)
                    && other.getValue().equals(value)
                    && other.getAnnotations().equals(getAnnotations());
        }
        return false;
    }

    @Override
    protected int compareObjectOfSameType(OWLObject object) {
        OWLAnnotation other = (OWLAnnotation) object;
        int diff = getProperty().compareTo(other.getProperty());
        if (diff != 0) {
            return diff;
        } else {
            return getValue().compareTo(other.getValue());
        }
    }
}
