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
package com.phloc.jaxb21.plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMods;
import com.sun.codemodel.JVar;

@Immutable
public final class CJAXB21
{
  public static final String NSURI_PHLOC = "http://www.phloc.com/ns/jaxb21/plugin";

  private CJAXB21 ()
  {}

  /**
   * Hack for JAXB 2.1 as in the codemodel, the params are not yet accessible :(
   * Therefore reflection is used to access them.
   * 
   * @param aMethod
   *        Method to get the params from
   * @return A copy of the params
   */
  @Nonnull
  @ReturnsMutableCopy
  public static List <JVar> getMethodParams (@Nonnull final JMethod aMethod)
  {
    try
    {
      final Field aField = aMethod.getClass ().getField ("params");
      final List <JVar> aParams = (List <JVar>) aField.get (aMethod);
      return ContainerHelper.newList (aParams);
    }
    catch (final Exception ex)
    {
      throw new IllegalArgumentException ("Failed to get method params!", ex);
    }
  }

  public static void updateMods (final JMods aMods, final int nFlag, final boolean bSet)
  {
    try
    {
      final Method aSetFlag = aMods.getClass ().getMethod ("setFlag", int.class, boolean.class);
      aSetFlag.invoke (aMods, Integer.valueOf (nFlag), Boolean.valueOf (bSet));
    }
    catch (final Exception ex)
    {
      throw new IllegalArgumentException ("Failed to set JMods flags!", ex);
    }
  }
}
