/*
 * Copyright (c) 2017 Appolica Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appolica.fragmentcontroller;

/**
 * Implement this interface in the child fragments of {@link FragmentController} so they can
 * receive the onBackPressed event. In order to this to work, you have to call
 * {@link FragmentController#onBackPressed()} from the parent {@link android.app.Activity} or
 * whatever holds your controller and receives the onBackPressed event.
 *
 */
public interface OnBackPressedListener {

    /**
     * Called when an onBackPressed event was received.
     * @return true if the event was handled, false if not and you want to pass it
     * up in the chain.
     */
    boolean onBackPressed();

}
