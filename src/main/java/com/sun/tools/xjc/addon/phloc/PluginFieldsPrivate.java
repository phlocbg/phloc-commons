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
package com.sun.tools.xjc.addon.phloc;

import java.util.List;

import javax.annotation.Nonnull;

import org.xml.sax.ErrorHandler;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.collections.ContainerHelper;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

/**
 * Plugin that makes all fields private instead of the default "protected"
 * 
 * @author Philip Helger
 */
@IsSPIImplementation
public class PluginFieldsPrivate extends Plugin
{
  private static final String OPT = "Xphloc-fields-private";

  @Override
  public String getOptionName ()
  {
    return OPT;
  }

  @Override
  public String getUsage ()
  {
    return "  -" + OPT + "    :  mark all fields as private";
  }

  @Override
  public List <String> getCustomizationURIs ()
  {
    return ContainerHelper.newUnmodifiableList (CJAXB21.NSURI_PHLOC);
  }

  @Override
  public boolean run (@Nonnull final Outline aOutline,
                      @Nonnull final Options aOpts,
                      @Nonnull final ErrorHandler aErrorHandler)
  {
    for (final ClassOutline aClassOutline : aOutline.getClasses ())
    {
      final JDefinedClass jClass = aClassOutline.implClass;
      for (final JFieldVar aFieldVar : jClass.fields ().values ())
      {
        CJAXB21.updateMods (aFieldVar.mods (), JMod.PUBLIC, false);
        CJAXB21.updateMods (aFieldVar.mods (), JMod.PROTECTED, false);
        CJAXB21.updateMods (aFieldVar.mods (), JMod.PRIVATE, true);
      }
    }
    return true;
  }
}
