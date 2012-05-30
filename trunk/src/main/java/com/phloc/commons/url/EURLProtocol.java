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
package com.phloc.commons.url;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;

/**
 * Specifies a list of known protocols.<br>
 * Should be extended to the list defined at <a
 * href="http://www.iana.org/assignments/uri-schemes.html">the IANA</a>
 * 
 * @author philip
 */
public enum EURLProtocol
{
  /** embedded data */
  DATA ("data:"),

  /** Local file. */
  FILE ("file:"),

  /** ftp. */
  FTP ("ftp://"),

  /** ftps. */
  FTPS ("ftps://"),

  /** geographic coordinates. */
  GEO ("geo:"),

  /** Gopher. */
  GOPHER ("gopher:"),

  /** http. */
  HTTP ("http://"),

  /** https. */
  HTTPS ("https://"),

  /** LDAP. */
  LDAP ("ldap:"),

  /** Email. */
  MAILTO ("mailto:"),

  /** News. */
  NEWS ("news:"),

  /** POP3. */
  POP ("pop:"),

  /** RSync. */
  RSYNC ("rsync:"),

  /** shttp. */
  SHTTP ("shttp://"),

  /** SIP. */
  SIP ("sip:"),

  /** Tel. */
  TEL ("tel:"),

  /** URN */
  URN ("urn:"),

  /** Web socket */
  WS ("ws://"),

  /** Encrypted web socket */
  WSS ("wss://"),

  // Extended - "unofficial"

  /** MS Media Server. */
  MMS ("mms:"),

  /** scp. */
  SCP ("scp://"),

  /** sftp. */
  SFTP ("sftp://"),

  /** Skype. */
  CALLTO ("callto:"),

  /** JavaScript */
  JAVASCRIPT ("javascript:"),

  /** RTMP */
  RTMP ("rtmp://");

  private final String m_sProtocol;

  private EURLProtocol (@Nonnull @Nonempty final String sProtocol)
  {
    m_sProtocol = sProtocol;
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
  public boolean covers (@Nullable final CharSequence sURL)
  {
    return sURL != null && StringHelper.startsWith (sURL, m_sProtocol);
  }

  @Nullable
  public static EURLProtocol getProtocol (@Nullable final CharSequence sURL)
  {
    if (sURL != null)
      for (final EURLProtocol eProtocol : EURLProtocol.values ())
        if (eProtocol.covers (sURL))
          return eProtocol;
    return null;
  }

  @Nullable
  public static EURLProtocol getProtocol (@Nullable final ISimpleURL aURL)
  {
    return aURL != null ? getProtocol (aURL.getPath ()) : null;
  }

  public static boolean hasKnownProtocol (@Nullable final CharSequence sURL)
  {
    return getProtocol (sURL) != null;
  }

  public static boolean hasKnownProtocol (@Nullable final ISimpleURL aURL)
  {
    return getProtocol (aURL) != null;
  }

  @Nullable
  public static String getWithoutProtocol (@Nullable final String sURL)
  {
    final EURLProtocol eProtocol = getProtocol (sURL);
    return eProtocol == null ? sURL : sURL.substring (eProtocol.getProtocol ().length ());
  }

  @Nullable
  public String ensureProtocol (@Nullable final String sURL)
  {
    if (sURL == null)
      return null;
    return m_sProtocol + getWithoutProtocol (sURL);
  }

  @Nullable
  public String ensureProtocolIfNone (@Nullable final String sURL)
  {
    if (sURL == null || hasKnownProtocol (sURL))
      return sURL;
    return m_sProtocol + sURL;
  }

  public boolean allowsForQueryParameters ()
  {
    return this == HTTP || this == HTTPS || this == MAILTO;
  }
}
