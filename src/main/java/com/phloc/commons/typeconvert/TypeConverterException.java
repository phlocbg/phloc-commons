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
package com.phloc.commons.typeconvert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.NoTranslationRequired;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.text.impl.TextFormatter;

/**
 * Exceptions of this type are only thrown from the {@link TypeConverter} class
 * if type conversion fails.
 * 
 * @author philip
 */
public final class TypeConverterException extends RuntimeException
{
  @NoTranslationRequired (value = "because whenever an exception is thrown, no locale is present!")
  public static enum EReason
  {
    CONVERSION_FAILED ("Conversion from type {0} to type {1} failed!"),
    NO_CONVERTER_FOUND ("No converter found to convert an object of type {0} to type {1}");

    private String m_sMsg;

    private EReason (@Nonnull @Nonempty final String sMsg)
    {
      m_sMsg = sMsg;
    }

    @Nonnull
    @Nonempty
    public String getMessage (@Nonnull final Class <?> aSrcClass, @Nonnull final Class <?> aDstClass)
    {
      return TextFormatter.getFormattedText (m_sMsg, aSrcClass.getName (), aDstClass.getName ());
    }
  }

  private final Class <?> m_aSrcClass;
  private final Class <?> m_aDstClass;
  private final EReason m_eReason;

  /**
   * Constructor without a cause exception.
   * 
   * @param aSrcClass
   *        The conversion source class. May not be <code>null</code>.
   * @param aDstClass
   *        The conversion destination class. May not be <code>null</code>.
   * @param eReason
   *        The reason code why the transformation failed. May not be
   *        <code>null</code>.
   */
  public TypeConverterException (@Nonnull final Class <?> aSrcClass,
                                 @Nonnull final Class <?> aDstClass,
                                 @Nonnull final EReason eReason)
  {
    this (aSrcClass, aDstClass, eReason, null);
  }

  /**
   * Constructor with a cause exception.
   * 
   * @param aSrcClass
   *        The conversion source class. May not be <code>null</code>.
   * @param aDstClass
   *        The conversion destination class. May not be <code>null</code>.
   * @param eReason
   *        The reason code why the transformation failed. May not be
   *        <code>null</code>.
   * @param aCause
   *        A causing exception. May be <code>null</code>.
   */
  public TypeConverterException (@Nonnull final Class <?> aSrcClass,
                                 @Nonnull final Class <?> aDstClass,
                                 @Nonnull final EReason eReason,
                                 @Nullable final Throwable aCause)
  {
    super (eReason.getMessage (aSrcClass, aDstClass), aCause);
    m_aSrcClass = aSrcClass;
    m_aDstClass = aDstClass;
    m_eReason = eReason;
  }

  /**
   * @return The conversion source class. Never <code>null</code>.
   */
  @Nonnull
  public Class <?> getSrcClass ()
  {
    return m_aSrcClass;
  }

  /**
   * @return The conversion destination class. Never <code>null</code>.
   */
  @Nonnull
  public Class <?> getDstClass ()
  {
    return m_aDstClass;
  }

  /**
   * @return The conversion failing reason. Never <code>null</code>.
   */
  @Nonnull
  public EReason getReason ()
  {
    return m_eReason;
  }
}
