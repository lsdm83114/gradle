/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.artifacts.ivyservice.resolveengine.graph.builder;

import com.google.common.collect.Lists;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.graph.conflicts.CandidateModule;
import org.gradle.internal.id.IdGenerator;
import org.gradle.internal.resolve.resolver.ComponentMetaDataResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Resolution state for a given module.
 */
class ModuleResolveState implements CandidateModule {
    private final ComponentMetaDataResolver metaDataResolver;
    private final IdGenerator<Long> idGenerator;
    private final ModuleIdentifier id;
    private final List<EdgeState> unattachedDependencies = new LinkedList<EdgeState>();
    private final Map<ModuleVersionIdentifier, ComponentState> versions = new LinkedHashMap<ModuleVersionIdentifier, ComponentState>();
    private final List<SelectorState> selectors = Lists.newLinkedList();
    private final VariantNameBuilder variantNameBuilder;
    private ComponentState selected;

    ModuleResolveState(IdGenerator<Long> idGenerator, ModuleIdentifier id, ComponentMetaDataResolver metaDataResolver, VariantNameBuilder variantNameBuilder) {
        this.idGenerator = idGenerator;
        this.id = id;
        this.metaDataResolver = metaDataResolver;
        this.variantNameBuilder = variantNameBuilder;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public ModuleIdentifier getId() {
        return id;
    }

    @Override
    public Collection<ComponentState> getVersions() {
        if (this.versions.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<ComponentState> values = this.versions.values();
        if (areAllCandidatesForSelection(values)) {
            return values;
        }
        List<ComponentState> versions = Lists.newArrayListWithCapacity(values.size());
        for (ComponentState componentState : values) {
            if (componentState.isCandidateForConflictResolution()) {
                versions.add(componentState);
            }
        }
        return versions;
    }

    private static boolean areAllCandidatesForSelection(Collection<ComponentState> values) {
        boolean allCandidates = true;
        for (ComponentState value : values) {
            if (!value.isCandidateForConflictResolution()) {
                allCandidates = false;
                break;
            }
        }
        return allCandidates;
    }

    public ComponentState getSelected() {
        return selected;
    }

    /**
     * Selects the target component for this module for the first time.
     * Any existing versions will be evicted.
     */
    public void select(ComponentState selected) {
        assert this.selected == null;
        this.selected = selected;

        selectComponentAndEvictOthers(selected);
    }

    private void selectComponentAndEvictOthers(ComponentState selected) {
        for (ComponentState version : versions.values()) {
            version.evict();
        }
        selected.select();
    }

    /**
     * Changes the selected target component for this module.
     */
    public void changeSelection(ComponentState newSelection) {
        assert this.selected != null;
        assert newSelection != null;
        assert this.selected != newSelection;
        assert newSelection.getModule() == this;

        // Remove any outgoing edges for the current selection
        selected.removeOutgoingEdges();

        this.selected = newSelection;

        doRestart(newSelection);
    }

    /**
     * Clears the current selection for the module, to prepare for conflict resolution.
     * - For the current selection, disconnect and remove any outgoing dependencies.
     * - Make all 'selected' component versions selectable.
     */
    public void clearSelection() {
        if (selected != null) {
            selected.removeOutgoingEdges();
        }
        for (ComponentState version : versions.values()) {
            // TODO:DAZ Only the current selection should require the `makeSelectable` call.
            if (version.isSelected()) {
                version.makeSelectable();
            }
        }

        selected = null;
    }

    /**
     * Overrides the component selection for this module, when this module has been replaced by another.
     */
    public void restart(ComponentState selected) {
        if (this.selected != null) {
            clearSelection();
        }

        assert this.selected == null;
        assert selected != null;

        this.selected = selected;

        doRestart(selected);
    }

    private void doRestart(ComponentState selected) {
        selectComponentAndEvictOthers(selected);
        for (ComponentState version : versions.values()) {
            version.restartIncomingEdges(selected);
        }
        for (SelectorState selector : selectors) {
            selector.overrideSelection(selected);
        }
        if (!unattachedDependencies.isEmpty()) {
            restartUnattachedDependencies(selected);
        }
    }

    private void restartUnattachedDependencies(ComponentState selected) {
        if (unattachedDependencies.size()==1) {
            unattachedDependencies.get(0).restart(selected);
        } else {
            for (EdgeState dependency : new ArrayList<EdgeState>(unattachedDependencies)) {
                dependency.restart(selected);
            }
        }
        unattachedDependencies.clear();
    }

    public void addUnattachedDependency(EdgeState edge) {
        unattachedDependencies.add(edge);
    }

    public void removeUnattachedDependency(EdgeState edge) {
        unattachedDependencies.remove(edge);
    }

    public ComponentState getVersion(ModuleVersionIdentifier id, ComponentIdentifier componentIdentifier) {
        ComponentState moduleRevision = versions.get(id);
        if (moduleRevision == null) {
            moduleRevision = new ComponentState(idGenerator.generateId(), this, id, componentIdentifier, metaDataResolver, variantNameBuilder);
            versions.put(id, moduleRevision);
        }
        return moduleRevision;
    }

    public void addSelector(SelectorState selector) {
        selectors.add(selector);
    }

    public List<SelectorState> getSelectors() {
        return selectors;
    }

}
