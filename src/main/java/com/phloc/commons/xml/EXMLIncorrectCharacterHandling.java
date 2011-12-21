package com.phloc.commons.xml;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;

/**
 * Define what to do, when an invalid character is to be serialized to XML.
 * 
 * @author philip
 */
public enum EXMLIncorrectCharacterHandling
{
  /**
   * Throw an exception
   */
  THROW_EXCEPTION (true, false)
  {
    @Override
    public void notifyOnInvalidXMLCharacter (@Nonnull @Nonempty final String s)
    {
      throw new IllegalArgumentException ("XML content contains invalid character data: '" + s + "'");
    }
  },

  /**
   * Write the invalid character to the file. This will result in a file that
   * cannot be read with the Java XML parser.<br>
   * This is the fastest option
   */
  WRITE_TO_FILE_NO_LOG (false, false)
  {
    @Override
    public void notifyOnInvalidXMLCharacter (@Nonnull @Nonempty final String s)
    {
      // Do nothing
    }
  },

  /**
   * Write the invalid character to the file. This will result in a file that
   * cannot be read with the Java XML parser.<br>
   * This is the second fastest option
   */
  WRITE_TO_FILE_LOG_WARNING (false, false)
  {
    @Override
    public void notifyOnInvalidXMLCharacter (@Nonnull @Nonempty final String s)
    {
      // Do nothing
    }
  },

  /**
   * Do not write the invalid character to XML and do not log anything. This
   * means silently ignore the problem.
   */
  DO_NOT_WRITE_NO_LOG (true, true)
  {
    @Override
    public void notifyOnInvalidXMLCharacter (@Nonnull @Nonempty final String s)
    {
      s_aLogger.warn ("XML content contains invalid character data: '" + s + "'");
    }
  },

  /**
   * Do not write the invalid character to XML but log a warning.
   */
  DO_NOT_WRITE_LOG_WARNING (true, true)
  {
    @Override
    public void notifyOnInvalidXMLCharacter (@Nonnull @Nonempty final String s)
    {
      s_aLogger.warn ("XML content contains invalid character data: '" + s + "'");
    }
  };

  /**
   * The default setting as it was in previous versions of phloc-commons
   */
  public static final EXMLIncorrectCharacterHandling DEFAULT = EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG;
  private static final Logger s_aLogger = LoggerFactory.getLogger (EXMLIncorrectCharacterHandling.class);

  private boolean m_bIsTestRequired;
  private boolean m_bReplaceWithNothing;

  private EXMLIncorrectCharacterHandling (final boolean bIsTestRequired, final boolean bReplaceWithNothing)
  {
    m_bIsTestRequired = bIsTestRequired;
    m_bReplaceWithNothing = bReplaceWithNothing;
  }

  /**
   * @return <code>true</code> if this handling type requires a check for
   *         invalid characters.
   */
  public boolean isTestRequired ()
  {
    return m_bIsTestRequired;
  }

  /**
   * @return <code>true</code> if all invalid characters should be replaced with
   *         nothing, meaning that they are simply ignored on writing.
   */
  public boolean isReplaceWithNothing ()
  {
    return m_bReplaceWithNothing;
  }

  /**
   * Called in case XML data contains an invalid character
   * 
   * @param s
   *        The XML string where the error occurs.
   */
  public abstract void notifyOnInvalidXMLCharacter (@Nonnull @Nonempty String s);
}
