package com.phloc.commons.url;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A simple implementation of the {@link IURLProtocol} interface.
 * 
 * @author philip
 */
public class URLProtocol implements IURLProtocol
{
  private final String m_sProtocol;
  private final boolean m_bAllowsForQueryParameters;

  public URLProtocol (@Nonnull @Nonempty final String sProtocol, final boolean bAllowsForQueryParameters)
  {
    m_sProtocol = sProtocol;
    m_bAllowsForQueryParameters = bAllowsForQueryParameters;
  }

  /**
   * @return The underlying text representation of the protocol.
   */
  @Nonnull
  @Nonempty
  public String getProtocol ()
  {
    return m_sProtocol;
  }

  /**
   * Tells if the passed String (URL) belongs to this protocol.
   * 
   * @param sURL
   *        The URL to check. May be <code>null</code>.
   * @return <code>true</code> if the passed URL starts with this protocol
   */
  public boolean isUsedInURL (@Nullable final CharSequence sURL)
  {
    return sURL != null && StringHelper.startsWith (sURL, m_sProtocol);
  }

  @Nullable
  public String getWithProtocol (@Nullable final String sURL)
  {
    if (sURL == null)
      return sURL;
    return m_sProtocol + sURL;
  }

  @Nullable
  public String getWithProtocolIfNone (@Nullable final String sURL)
  {
    if (sURL == null || URLProtocolRegistry.hasKnownProtocol (sURL))
      return sURL;
    return m_sProtocol + sURL;
  }

  public boolean allowsForQueryParameters ()
  {
    return m_bAllowsForQueryParameters;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final URLProtocol rhs = (URLProtocol) o;
    return m_sProtocol.equals (rhs.m_sProtocol) && m_bAllowsForQueryParameters == rhs.m_bAllowsForQueryParameters;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sProtocol).append (m_bAllowsForQueryParameters).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("protocol", m_sProtocol)
                                       .append ("queryParams", m_bAllowsForQueryParameters)
                                       .toString ();
  }
}
