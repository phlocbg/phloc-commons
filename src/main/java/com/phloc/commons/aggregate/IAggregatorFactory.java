/**
 * Copyright (C) 2006-2012 phloc systems
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

import com.phloc.commons.factory.IFactory;

/**
 * Base interface for a factory creating {@link IAggregator} objects.
 * 
 * @author philip
 * @param <SRCTYPE>
 *        Aggregation source type
 * @param <DSTTYPE>
 *        Aggregation destination type
 */
public interface IAggregatorFactory <SRCTYPE, DSTTYPE> extends IFactory <IAggregator <SRCTYPE, DSTTYPE>>
{
  /* empty */
}
