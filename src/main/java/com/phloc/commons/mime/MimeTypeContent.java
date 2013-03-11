package com.phloc.commons.mime;

import java.util.Arrays;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

public class MimeTypeContent
{
  private final byte [] m_aContentBytes;
  private final IMimeType m_aMimeType;

  public MimeTypeContent (@Nonnull final byte [] aContentBytes, @Nonnull final IMimeType aMimeType)
  {
    if (ArrayHelper.isEmpty (aContentBytes))
      throw new IllegalArgumentException ("no ContentBytes provided");
    if (aMimeType == null)
      throw new NullPointerException ("mimeType");
    m_aContentBytes = ArrayHelper.getCopy (aContentBytes);
    m_aMimeType = aMimeType;
  }

  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public byte [] getContentBytes ()
  {
    return ArrayHelper.getCopy (m_aContentBytes);
  }

  @Nonnull
  public IMimeType getMimeType ()
  {
    return m_aMimeType;
  }

  private static boolean _match (@Nonnull final byte [] aBytes, final int nOffset, @Nonnull final byte [] aCmpBytes)
  {
    final int nEnd = aCmpBytes.length;
    for (int i = 0; i < nEnd; ++i)
      if (aBytes[nOffset + i] != aCmpBytes[i])
        return false;
    return true;
  }

  public boolean matchesBeginning (@Nonnull final byte [] aBytes)
  {
    return aBytes.length > m_aContentBytes.length && _match (aBytes, 0, m_aContentBytes);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final MimeTypeContent rhs = (MimeTypeContent) o;
    return Arrays.equals (m_aContentBytes, rhs.m_aContentBytes) && m_aMimeType.equals (rhs.m_aMimeType);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aContentBytes).append (m_aMimeType).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("contentBytes", Arrays.toString (m_aContentBytes))
                                       .append ("mimeType", m_aMimeType)
                                       .toString ();
  }
}
