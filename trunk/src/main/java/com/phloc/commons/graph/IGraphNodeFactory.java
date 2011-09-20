package com.phloc.commons.graph;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Factory interface for creating graph nodes
 * 
 * @author philip
 * @param <VALUETYPE>
 *        Graph node value type
 */
public interface IGraphNodeFactory <VALUETYPE>
{
  /**
   * Create a new graph node with a new ID.
   * 
   * @param aValue
   *        The value to be assigned to the graph node. May be <code>null</code>
   *        .
   * @return The created graph node. May not be <code>null</code>.
   */
  @Nonnull
  IGraphNode <VALUETYPE> createNode (@Nullable VALUETYPE aValue);

  /**
   * Create a new graph node with a known ID.
   * 
   * @param sID
   *        The ID of the graph node. If it is <code>null</code> a new ID is
   *        automatically created.
   * @param aValue
   *        The value to be assigned to the graph node. May be <code>null</code>
   *        .
   * @return The created graph node. May not be <code>null</code>.
   */
  @Nonnull
  IGraphNode <VALUETYPE> createNode (@Nullable String sID, @Nullable VALUETYPE aValue);
}
