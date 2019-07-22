package com.phloc.commons.lang;

import javax.annotation.Nullable;

/**
 * A stack trace customizer can be used to to perform modifications to the stack
 * trace String generated from a throwable
 *
 * @author Boris Gregorcic
 */
public interface IStackTraceCustomizer
{
  /**
   * Processes the localized message of a stack trace.
   *
   * @param sMessage
   *        The message to process, may be <code>null</code>
   * @return The processed message, may be <code>null</code>. By default, the
   *         message passed will just be returned unchanged.
   */
  @Nullable
  default String customizeMessage (@Nullable final String sMessage)
  {
    return sMessage;
  }
}
