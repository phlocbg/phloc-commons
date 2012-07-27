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
package com.phloc.commons.typeconvert.rule;

import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.typeconvert.ITypeConverterRuleAnySource;
import com.phloc.commons.typeconvert.ITypeConverterRuleFixedDestination;

/**
 * Abstract type converter than can convert from a base source class to a
 * destination class. Example from Number.class to String.class
 * 
 * @author philip
 */
public abstract class AbstractTypeConverterRuleAnySourceFixedDestination implements
                                                                        ITypeConverterRuleAnySource,
                                                                        ITypeConverterRuleFixedDestination
{
  private final Class <?> m_aDstClass;

  public AbstractTypeConverterRuleAnySourceFixedDestination (@Nonnull final Class <?> aDstClass)
  {
    if (aDstClass == null)
      throw new NullPointerException ("dstClass");
    m_aDstClass = aDstClass;
  }

  public final boolean canConvert (@Nonnull final Class <?> aSrcClass, @Nonnull final Class <?> aDstClass)
  {
    // soure class can be anything
    return m_aDstClass.equals (aDstClass);
  }

  @Nonnull
  public final Class <?> getDestinationClass ()
  {
    return m_aDstClass;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("dstClass", m_aDstClass.getName ()).toString ();
  }
}
