package com.phloc.commons.url;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base interface representing the basic elements of a URL from a high level
 * perspective.
 * 
 * @author philip
 */
public interface IURLData extends Serializable
{
  /**
   * @return The path part of the URL (everything before the "?" and the "#")
   */
  @Nonnull
  String getPath ();

  /**
   * @return A map of all query string parameters. May be <code>null</code>.
   */
  @Nullable
  Map <String, String> directGetParams ();

  /**
   * @return The name of the anchor (everything after the "#") or
   *         <code>null</code> if none is defined.
   */
  @Nullable
  String getAnchor ();
}
