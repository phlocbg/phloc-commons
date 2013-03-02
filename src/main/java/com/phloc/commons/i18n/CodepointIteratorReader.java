package com.phloc.commons.i18n;

import java.io.Reader;

import javax.annotation.Nonnull;
import javax.annotation.WillClose;

import com.phloc.commons.io.streams.StreamUtils;

/**
 * @author Apache Abdera
 */
public class CodepointIteratorReader extends CodepointIteratorCharArray
{
  public CodepointIteratorReader (@Nonnull @WillClose final Reader aReader)
  {
    super (StreamUtils.getAllCharacters (aReader));
  }
}
