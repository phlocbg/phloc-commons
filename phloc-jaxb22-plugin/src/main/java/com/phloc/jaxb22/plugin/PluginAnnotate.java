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
package com.phloc.jaxb22.plugin;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xml.sax.ErrorHandler;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

@IsSPIImplementation
public class PluginAnnotate extends Plugin
{
  private static final String OPT = "Xphloc-annotate";

  @Override
  public String getOptionName ()
  {
    return OPT;
  }

  @Override
  public String getUsage ()
  {
    return "  -" + OPT + " :  add Nullable/Nonnull annotations to getters and setters";
  }

  @Override
  public List <String> getCustomizationURIs ()
  {
    return ContainerHelper.newUnmodifiableList (CJAXB22.NSURI_PHLOC);
  }

  @Override
  public boolean run (final Outline aOutline, final Options aOpts, final ErrorHandler aErrorHandler)
  {
    // For all classes
    for (final ClassOutline aClassOutline : aOutline.getClasses ())
    {
      final JDefinedClass jClass = aClassOutline.implClass;
      for (final JMethod aMethod : ContainerHelper.newList (jClass.methods ()))
      {
        final List <JVar> aParams = aMethod.params ();
        if (aMethod.name ().startsWith ("get") && aParams.isEmpty ())
        {
          final JType aReturnType = aMethod.type ();
          // Find e.g. List<ItemListType> getItemList()
          if (aReturnType.name ().startsWith ("List<"))
          {
            aMethod.annotate (Nonnull.class);
            aMethod.annotate (ReturnsMutableObject.class).param ("reason", "JAXB implementation style");
          }
          else
            if (!aReturnType.isPrimitive ())
              aMethod.annotate (Nullable.class);
        }
        else
          if (aMethod.name ().startsWith ("set") && aParams.size () == 1)
          {
            final JVar aParam = aParams.get (0);
            if (!aParam.type ().isPrimitive ())
              aParam.annotate (Nullable.class);
          }
      }
    }
    return true;
  }
}
