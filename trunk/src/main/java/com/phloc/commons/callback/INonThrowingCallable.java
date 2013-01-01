/**
 * Copyright (C) 2006-2013 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.commons.callback;

import java.util.concurrent.Callable;

/**
 * Like the {@link Callable} interface but not throwing an exception!
 * 
 * @author philip
 * @param <DATATYPE>
 *        The return type of the call.
 */
public interface INonThrowingCallable <DATATYPE> extends IThrowingCallable <DATATYPE>
{
  /**
   * The call back method to be called.
   * 
   * @return Anything. May be <code>null</code> or non- <code>null</code>
   *         depending on the implementation.
   */
  DATATYPE call ();
}
