/*
 * Copyright 2013 Simba Open Source
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.simbasecurity.common.filter.action;

import java.util.List;

import org.simbasecurity.api.service.thrift.ActionDescriptor;

public abstract class ActionFactory {

    /**
     * Create a list of actions to execute in order.
     *
     * @param actionDescriptor the action descriptor
     * @return a list of actions
     */
    protected abstract List<Action> create(ActionDescriptor actionDescriptor);

    /**
     * Execute the actions in the descriptor.
     *
     * @param actionDescriptor the descriptor to execute
     * @throws Exception when an exception occurs while executing
     * @see #create(org.simbasecurity.api.service.thrift.ActionDescriptor)
     */
    public final void execute(final ActionDescriptor actionDescriptor) throws Exception {
        final List<Action> actions = create(actionDescriptor);
        for (final Action action : actions) {
            action.execute();
        }
    }
}
