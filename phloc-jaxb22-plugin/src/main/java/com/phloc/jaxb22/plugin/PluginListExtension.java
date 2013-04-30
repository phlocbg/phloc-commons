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

import javax.annotation.Nonnegative;

import org.xml.sax.ErrorHandler;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.collections.ContainerHelper;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

@IsSPIImplementation
public class PluginListExtension extends Plugin
{
  private static final String OPT = "Xphloc-list-extension";

  @Override
  public String getOptionName ()
  {
    return OPT;
  }

  @Override
  public String getUsage ()
  {
    return "  -" + OPT + " :  add additional methods for list types";
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
        if (aMethod.name ().startsWith ("get") && aMethod.params ().isEmpty ())
        {
          final JType aReturnType = aMethod.type ();
          // Find e.g. List<ItemListType> getItemList()
          if (aReturnType.name ().startsWith ("List<"))
          {
            {
              final JMethod mHasEntries = jClass.method (JMod.PUBLIC,
                                                         aOutline.getCodeModel ().BOOLEAN,
                                                         "has" + aMethod.name ().substring (3) + "Entries");
              mHasEntries.body ()._return (JOp.not (JExpr.invoke (aMethod).invoke ("isEmpty")));
              mHasEntries.javadoc ()
                         .addReturn ()
                         .add ("<code>true</code> if at least one item is contained, <code>false</code> otherwise.");
            }

            {
              final JMethod mHasNoEntries = jClass.method (JMod.PUBLIC,
                                                           aOutline.getCodeModel ().BOOLEAN,
                                                           "hasNo" + aMethod.name ().substring (3) + "Entries");
              mHasNoEntries.body ()._return (JExpr.invoke (aMethod).invoke ("isEmpty"));
              mHasNoEntries.javadoc ()
                           .addReturn ()
                           .add ("<code>true</code> if no item is contained, <code>false</code> otherwise.");
            }

            {
              final JMethod mCount = jClass.method (JMod.PUBLIC, aOutline.getCodeModel ().INT, aMethod.name () +
                                                                                               "Count");
              mCount.annotate (Nonnegative.class);
              mCount.body ()._return (JExpr.invoke (aMethod).invoke ("size"));
              mCount.javadoc ().addReturn ().add ("The number of contained elements. Always &ge; 0.");
            }

            {
              final JMethod mAtIndex = jClass.method (JMod.PUBLIC,
                                                      ((JClass) aReturnType).getTypeParameters ().get (0),
                                                      "get" + aMethod.name ().substring (3) + "AtIndex");
              final JVar aParam = mAtIndex.param (JMod.FINAL, aOutline.getCodeModel ().INT, "index");
              aParam.annotate (Nonnegative.class);
              mAtIndex.body ()._return (JExpr.invoke (aMethod).invoke ("get").arg (aParam));
              mAtIndex.javadoc ().addParam (aParam).add ("The index to retrieve");
              mAtIndex.javadoc ()
                      .addReturn ()
                      .add ("<code>true</code> if at least one item is contained, <code>false</code> otherwise.");
              mAtIndex.javadoc ().addThrows (ArrayIndexOutOfBoundsException.class).add ("if the index is invalid!");
            }
          }
        }
    }
    return true;
  }
}
