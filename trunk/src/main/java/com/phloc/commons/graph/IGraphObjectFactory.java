package com.phloc.commons.graph;

/**
 * Combined factory interface that encapsulates node and relation factory
 * methods.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        Graph node value type
 */
public interface IGraphObjectFactory <VALUETYPE> extends
                                                 IGraphNodeFactory <VALUETYPE>,
                                                 IGraphRelationFactory <VALUETYPE>
{
  /* empty */
}
