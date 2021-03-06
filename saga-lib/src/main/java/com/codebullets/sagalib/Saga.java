/*
 * Copyright 2013 Stefan Domnanovits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codebullets.sagalib;

import java.util.Collection;

/**
 * Represents a single saga. A saga contains state and handles different messages and events.
 *
 * @param <SAGA_STATE> Type of the saga state persisted between message handling.
 */
public interface Saga<SAGA_STATE extends SagaState> {
    /**
     * Gets the current state of the saga.
     */
    SAGA_STATE state();

    /**
     * Sets the current state of the sage. When {@link #createNewState()} is called the implementer
     * should use this method to set the specific saga instance state.
     *
     * <p>
     * When loading an already running saga this method is called by the saga-lib before
     * any of the handler methods are called.</p>
     *
     * <p>
     * Once set the {@link #state()} method should return the provided instance.</p>
     *
     */
    void setState(SAGA_STATE state);

    /**
     * Instructs the sage to create a new empty instance of the saga state. After
     * this method has been called {@link #state()} is expected to return the new instance.
     */
    void createNewState();

    /**
     * Indicates whether the saga has finished. A finished saga will result in the state and
     * open timeouts becoming deleted.
     * @return True if saga is complete; otherwise false.
     */
    boolean isFinished();

    /**
     * <p>Returns a list of readers which are used to determine the instance key of
     * an incoming message assigned to the saga. This key is used to load the
     * existing state when a saga is continued.</p>
     *
     * <p>A key reader must be provided for all messages handled defined by
     * the {@link EventHandler} annotations of the saga.
     * A key reader is not needed for messages starting a saga.</p>
     */
    Collection<KeyReader> keyReaders();
}
