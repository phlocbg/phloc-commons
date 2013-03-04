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
package com.phloc.commons.graph.utils;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.graph.IGraphNode;
import com.phloc.commons.graph.IGraphRelation;
import com.phloc.commons.graph.IReadonlyGraph;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.XMLHelper;

/**
 * Utility class to export a graph to something else
 * 
 * @author philip
 */
@Immutable
public final class GraphVizUtils
{
  private GraphVizUtils ()
  {}

  /**
   * Get the graph in a simple DOT notation suitable for GraphViz
   * (http://www.graphviz.org). The DOT specs can be found at
   * http://www.graphviz.org/content/dot-language<br>
   * The default file encoding for GraphViz 2.28 is UTF-8!
   * 
   * @param aGraph
   *        The graph to be converted. May not be <code>null</code>.
   * @param sNodeLabelAttr
   *        The name of the attribute to be used for node labels. May be
   *        <code>null</code> to use the node ID as the label.
   * @param sRelationLabelAttr
   *        The name of the attribute to be used for relation labels. May be
   *        <code>null</code> to use no relation label.
   * @return The string representation to be used as input for DOT.
   */
  @Nonnull
  public static String getAsGraphVizDot (@Nonnull final IReadonlyGraph <?> aGraph,
                                         @Nullable final String sNodeLabelAttr,
                                         @Nullable final String sRelationLabelAttr)
  {
    if (aGraph == null)
      throw new NullPointerException ("graph");

    final StringBuilder aSB = new StringBuilder ();
    // It's a directed graph
    aSB.append ("digraph{\n");
    aSB.append ("node[shape=box];");
    for (final IGraphNode <?> aGraphNode : aGraph.getAllNodes ())
    {
      aSB.append (aGraphNode.getID ());
      if (StringHelper.hasText (sNodeLabelAttr))
      {
        final String sLabel = aGraphNode.getAttributeAsString (sNodeLabelAttr);
        aSB.append ("[label=<")
           .append (XMLHelper.getMaskedXMLText (EXMLVersion.XML_10, EXMLIncorrectCharacterHandling.DEFAULT, sLabel))
           .append (">]");
      }
      aSB.append (';');
    }
    aSB.append ('\n');
    for (final IGraphRelation <?> aGraphRelation : aGraph.getAllRelations ())
    {
      aSB.append (aGraphRelation.getFromID ()).append ("->").append (aGraphRelation.getToID ());
      if (StringHelper.hasText (sRelationLabelAttr))
      {
        final String sLabel = aGraphRelation.getAttributeAsString (sRelationLabelAttr);
        aSB.append ("[label=<")
           .append (XMLHelper.getMaskedXMLText (EXMLVersion.XML_10, EXMLIncorrectCharacterHandling.DEFAULT, sLabel))
           .append (">]");
      }
      aSB.append (";\n");
    }
    aSB.append ("overlap=false;\n");
    aSB.append ('}');
    return aSB.toString ();
  }

  /**
   * Invoked the external process "neato" from the GraphViz package. Attention:
   * this spans a sub-process!
   * 
   * @param aGraph
   *        The graph to be converted. May not be <code>null</code>.
   * @param sNodeLabelAttr
   *        The name of the attribute to be used for node labels. May be
   *        <code>null</code> to use the node ID as the label.
   * @param sRelationLabelAttr
   *        The name of the attribute to be used for relation labels. May be
   *        <code>null</code> to use no relation label.
   * @param sFileType
   *        The file type to be generated. E.g. "png" - see neato help for
   *        details. May neither be <code>null</code> nor empty.
   * @return The byte buffer that keeps the converted image. Never
   *         <code>null</code>.
   * @throws IOException
   *         In case some IO error occurs
   * @throws InterruptedException
   *         If the sub-process did not terminate correctly!
   */
  @Nonnull
  public static NonBlockingByteArrayOutputStream getGraphAsImageWithGraphVizNeato (@Nonnull final IReadonlyGraph <?> aGraph,
                                                                                   @Nullable final String sNodeLabelAttr,
                                                                                   @Nullable final String sRelationLabelAttr,
                                                                                   @Nonnull @Nonempty final String sFileType) throws IOException,
                                                                                                                             InterruptedException
  {
    if (StringHelper.hasNoText (sFileType))
      throw new IllegalArgumentException ("Empty file type!");

    final String sDOT = getAsGraphVizDot (aGraph, sNodeLabelAttr, sRelationLabelAttr);
    final ProcessBuilder aPB = new ProcessBuilder ("neato", "-T" + sFileType).redirectErrorStream (false);
    final Process p = aPB.start ();
    // Set neato stdin
    p.getOutputStream ().write (CharsetManager.getAsBytes (sDOT, CCharset.CHARSET_UTF_8_OBJ));
    p.getOutputStream ().close ();
    // Read neato stdout
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    StreamUtils.copyInputStreamToOutputStream (p.getInputStream (), aBAOS);
    p.waitFor ();
    return aBAOS;
  }
}