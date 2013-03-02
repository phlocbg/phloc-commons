package com.phloc.commons.i18n;

import javax.annotation.Nonnull;

/**
 * @author Apache Abdera
 */
public class CodepointIteratorCharSequence extends AbstractCodepointIterator
{
  private final CharSequence m_aBuffer;

  public CodepointIteratorCharSequence (@Nonnull final CharSequence buffer)
  {
    this (buffer, 0, buffer.length ());
  }

  public CodepointIteratorCharSequence (@Nonnull final CharSequence aBuffer, final int nOfs, final int nLen)
  {
    m_aBuffer = aBuffer;
    m_nPosition = nOfs;
    m_nLimit = Math.min (aBuffer.length () - nOfs, nLen);
  }

  @Override
  protected char get ()
  {
    return m_aBuffer.charAt (m_nPosition++);
  }

  @Override
  protected char get (final int index)
  {
    return m_aBuffer.charAt (index);
  }
}
