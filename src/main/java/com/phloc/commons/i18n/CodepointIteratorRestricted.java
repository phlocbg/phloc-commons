package com.phloc.commons.i18n;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Apache Abdera
 */
public class CodepointIteratorRestricted extends DelegatingCodepointIterator
{
  private final ICodepointFilter m_aFilter;
  private final boolean m_bScanningOnly;
  private final boolean m_bInvert;

  protected CodepointIteratorRestricted (@Nonnull final AbstractCodepointIterator aInternal,
                                         @Nonnull final ICodepointFilter aFilter)
  {
    this (aInternal, aFilter, false);
  }

  protected CodepointIteratorRestricted (@Nonnull final AbstractCodepointIterator aInternal,
                                         @Nonnull final ICodepointFilter aFilter,
                                         final boolean bScanningOnly)
  {
    this (aInternal, aFilter, bScanningOnly, false);
  }

  protected CodepointIteratorRestricted (@Nonnull final AbstractCodepointIterator aInternal,
                                         @Nonnull final ICodepointFilter aFilter,
                                         final boolean bScanningOnly,
                                         final boolean bInvert)
  {
    super (aInternal);
    if (aFilter == null)
      throw new NullPointerException ("filter");
    m_aFilter = aFilter;
    m_bScanningOnly = bScanningOnly;
    m_bInvert = bInvert;
  }

  @Override
  public boolean hasNext ()
  {
    final boolean b = super.hasNext ();
    if (m_bScanningOnly)
    {
      try
      {
        final int cp = peek (position ()).getValue ();
        if (b && cp != -1 && _doFilter (cp))
          return false;
      }
      catch (final InvalidCharacterException e)
      {
        return false;
      }
    }
    return b;
  }

  @Override
  public Codepoint next ()
  {
    final Codepoint cp = super.next ();
    final int v = cp.getValue ();
    if (v != -1 && _doFilter (v))
    {
      if (m_bScanningOnly)
      {
        position (position () - 1);
        return null;
      }
      throw new InvalidCharacterException (v);
    }
    return cp;
  }

  private boolean _doFilter (final int cp)
  {
    final boolean bAccept = m_aFilter.accept (cp);
    return m_bInvert ? !bAccept : bAccept;
  }

  @Override
  @Nullable
  public char [] nextChars ()
  {
    final char [] chars = super.nextChars ();
    if (chars != null && chars.length > 0)
    {
      if (chars.length == 1 && _doFilter (chars[0]))
      {
        if (m_bScanningOnly)
        {
          position (position () - 1);
          return null;
        }
        throw new InvalidCharacterException (chars[0]);
      }

      if (chars.length == 2)
      {
        final int cp = Character.toCodePoint (chars[0], chars[1]);
        if (_doFilter (cp))
        {
          if (m_bScanningOnly)
          {
            position (position () - 2);
            return null;
          }
          throw new InvalidCharacterException (cp);
        }
      }
    }
    return chars;
  }
}
