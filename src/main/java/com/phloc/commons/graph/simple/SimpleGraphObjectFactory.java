package com.phloc.commons.graph.simple;

import javax.annotation.Nonnull;

import com.phloc.commons.graph.IGraphNode;
import com.phloc.commons.graph.IGraphObjectFactory;
import com.phloc.commons.graph.IGraphRelation;
import com.phloc.commons.graph.impl.GraphNode;
import com.phloc.commons.graph.impl.GraphRelation;

public class SimpleGraphObjectFactory <VALUETYPE> implements IGraphObjectFactory <VALUETYPE>
{
  @Nonnull
  public IGraphNode <VALUETYPE> createNode (final VALUETYPE aValue)
  {
    return new GraphNode <VALUETYPE> (null, aValue);
  }

  @Nonnull
  public IGraphNode <VALUETYPE> createNode (final String sID, final VALUETYPE aValue)
  {
    return new GraphNode <VALUETYPE> (sID, aValue);
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> createRelation (final IGraphNode <VALUETYPE> aFrom, final IGraphNode <VALUETYPE> aTo)
  {
    return new GraphRelation <VALUETYPE> (aFrom, aTo);
  }

  @Nonnull
  public IGraphRelation <VALUETYPE> createRelation (final String sID,
                                                    final IGraphNode <VALUETYPE> aFrom,
                                                    final IGraphNode <VALUETYPE> aTo)
  {
    return new GraphRelation <VALUETYPE> (sID, aFrom, aTo);
  }
}
