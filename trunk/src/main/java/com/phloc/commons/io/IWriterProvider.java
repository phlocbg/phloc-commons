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
package com.phloc.commons.io;

import java.io.Writer;

import javax.annotation.Nullable;

/**
 * A callback interface to retrieve {@link Writer} objects.
 * 
 * @author philip
 */
public interface IWriterProvider
{
  /**
   * Get the writer to write to an object. Each time this method is call, a new
   * {@link Writer} needs to be created!
   * 
   * @return <code>null</code> if resolving failed.
   */
  @Nullable
  Writer getWriter ();
}