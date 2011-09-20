package com.phloc.commons.graph;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Factory interface for creating graph relations.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        The value type of the graph nodes.
 */
public interface IGraphRelationFactory <VALUETYPE>
{
  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param aFrom
   *        The from node. May not be <code>null</code>.
   * @param aTo
   *        The to node. May not be <code>null</code>.
   * @return The created graph relation and never <code>null</code>.
   */
  @Nonnull
  IGraphRelation <VALUETYPE> createRelation (@Nonnull IGraphNode <VALUETYPE> aFrom, @Nonnull IGraphNode <VALUETYPE> aTo);

  /**
   * Shortcut factory method to spare using the generics parameter manually.
   * 
   * @param sID
   *        The ID of the relation to be created. May be <code>null</code>. If
   *        <code>null</code> than a new unique ID is created.
   * @param aFrom
   *        The from node. May not be <code>null</code>.
   * @param aTo
   *        The to node. May not be <code>null</code>.
   * @return The created graph relation and never <code>null</code>.
   */
  @Nonnull
  IGraphRelation <VALUETYPE> createRelation (@Nullable String sID,
                                             @Nonnull IGraphNode <VALUETYPE> aFrom,
                                             @Nonnull IGraphNode <VALUETYPE> aTo);
}
