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
package com.phloc.jaxb22.plugin;

import java.io.Serializable;
import java.util.List;

import org.xml.sax.ErrorHandler;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.math.graph.IDirectedGraphNode;
import com.phloc.math.graph.simple.SimpleDirectedGraph;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.Outline;

/**
 * Make all classes implement special interfaces that need to be passed as
 * arguments. A typical example is "java.io.Serializable"
 * 
 * @author Philip Helger
 */
@IsSPIImplementation
public class PluginImplements extends Plugin
{
  private static final String OPT = "Xphloc-implements";
  private List <String> m_aInterfaces;

  @Override
  public String getOptionName ()
  {
    return OPT;
  }

  @Override
  public String getUsage ()
  {
    return "  -" + OPT + " interfaceName[,interfaceName] :  implement 1-n interfaces in a class";
  }

  @Override
  public int parseArgument (final Options opt, final String [] args, final int i) throws BadCommandLineException
  {
    if (args[i].equals ("-" + OPT))
    {
      final String sClassNames = opt.requireArgument ("-" + OPT, args, i + 1);
      m_aInterfaces = RegExHelper.getSplitToList (sClassNames, "[,;]+");
      if (m_aInterfaces.isEmpty ())
        throw new BadCommandLineException ("No interface names provided. They must be seprated by commad (,) or semicolon (;)");
      return 2;
    }
    return 0;
  }

  @Override
  public List <String> getCustomizationURIs ()
  {
    return ContainerHelper.newUnmodifiableList (CJAXB22.NSURI_PHLOC);
  }

  @Override
  public boolean run (final Outline aOutline, final Options aOpts, final ErrorHandler aErrorHandler)
  {
    final JCodeModel aCodeModel = aOutline.getCodeModel ();

    // Build the graph with all classes and there hierarchy
    final SimpleDirectedGraph aSG = new SimpleDirectedGraph ();
    // Create all nodes
    for (final ClassOutline aClassOutline : aOutline.getClasses ())
      aSG.createNode (aClassOutline.implClass.fullName ()).setAttribute ("value", aClassOutline.implClass);
    // Connect them
    for (final ClassOutline aClassOutline : aOutline.getClasses ())
    {
      // Check if there is a super-class node present (not present e.g. for
      // Object.class)
      final IDirectedGraphNode aParentNode = aSG.getNodeOfID (aClassOutline.implClass._extends ().fullName ());
      if (aParentNode != null)
        aSG.createRelation (aParentNode, aSG.getNodeOfID (aClassOutline.implClass.fullName ()));
    }

    for (final String sInterface : m_aInterfaces)
    {
      final String sCleanInterfaceName = sInterface.trim ();
      final JClass aInterface = aCodeModel.ref (sCleanInterfaceName);

      // Implement interfaces only in all base classes, because sub-classes have
      // them already!
      for (final IDirectedGraphNode aNode : aSG.getAllStartNodes ())
        ((JDefinedClass) aNode.getCastedAttribute ("value"))._implements (aInterface);

      // Enums are automatically serializable
      if (!sCleanInterfaceName.equals (Serializable.class.getName ()))
        for (final EnumOutline aEnumOutline : aOutline.getEnums ())
          aEnumOutline.clazz._implements (aInterface);
    }
    return true;
  }
}
