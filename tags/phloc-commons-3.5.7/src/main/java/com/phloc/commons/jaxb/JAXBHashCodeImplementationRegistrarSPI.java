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
package com.phloc.commons.jaxb;

import javax.annotation.Nonnull;
import javax.xml.bind.JAXBElement;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.hash.IHashCodeImplementation;
import com.phloc.commons.hash.IHashCodeImplementationRegistrarSPI;
import com.phloc.commons.hash.IHashCodeImplementationRegistry;

@IsSPIImplementation
public final class JAXBHashCodeImplementationRegistrarSPI implements IHashCodeImplementationRegistrarSPI
{
  public void registerHashCodeImplementations (@Nonnull final IHashCodeImplementationRegistry aRegistry)
  {
    // JAXBElement does not implement hashCode!
    aRegistry.registerHashCodeImplementation (JAXBElement.class, new IHashCodeImplementation ()
    {
      public int getHashCode (final Object aObj)
      {
        final JAXBElement <?> aRealObj = (JAXBElement <?>) aObj;
        return new HashCodeGenerator (aRealObj.getClass ()).append (aRealObj.getDeclaredType ())
                                                           .append (aRealObj.getName ())
                                                           .append (aRealObj.getScope ())
                                                           .append (aRealObj.isNil ())
                                                           .append (aRealObj.getValue ())
                                                           .getHashCode ();
      }
    });
  }
}
