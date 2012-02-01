package com.phloc.commons.url;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasStringRepresentation;

public interface ISimpleURL extends IURLData, IHasStringRepresentation
{
  /**
   * The string representing an empty URL. Must contain at least one character.
   */
  String EMPTY_URL_STRING = "?";

  /**
   * Get the parameter value of the given key.
   * 
   * @param sKey
   *        The key to check. May be <code>null</code>.
   * @return <code>null</code> if no such parameter is present.
   */
  @Nullable
  String getParam (@Nullable String sKey);

  boolean hasKnownProtocol ();

  /**
   * @return The final string representation of this URL not encoding the
   *         request parameters.
   */
  @Nonnull
  String getAsString ();

  /**
   * @return The final string representation of this URL with encoded URL
   *         parameter keys and values. Using the default URL charset as
   *         determined by {@link URLUtils#CHARSET_URL}.
   */
  @Nonnull
  String getAsStringWithEncodedParameters ();

  /**
   * @param sCharset
   *        The charset used for encoding the parameters.
   * @return The final string representation of this URL with encoded URL
   *         parameter keys and values.
   */
  @Nonnull
  String getAsStringWithEncodedParameters (String sCharset);
}
