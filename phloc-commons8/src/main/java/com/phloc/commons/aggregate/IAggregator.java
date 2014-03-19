/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.aggregate;

import java.util.Collection;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Aggregate a list of input objects to an output object.
 *
 * @author Philip Helger
 * @param <SRCTYPE>
 *        The input type.
 * @param <DSTTYPE>
 *        The output type.
 */
public interface IAggregator <SRCTYPE, DSTTYPE> extends Function <Collection <SRCTYPE>, DSTTYPE>
{
  @Nullable
  DSTTYPE aggregate (@Nonnull Collection <SRCTYPE> aObjects);

  default DSTTYPE apply (final Collection <SRCTYPE> t)
  {
    return aggregate (t);
  }
}
