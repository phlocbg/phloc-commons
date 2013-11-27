package com.phloc.commons.xml.serialize;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link XMLCharHelper}.
 * 
 * @author Philip Helger
 */
public final class XMLCharHelperTest
{
  @Test
  public void testAttributeValue ()
  {
    assertFalse (XMLCharHelper.isInvalidXMLAttributeValueChar (EXMLSerializeVersion.XML_10, '<'));
    assertFalse (XMLCharHelper.isInvalidXMLAttributeValueChar (EXMLSerializeVersion.XML_11, '<'));
    assertFalse (XMLCharHelper.isInvalidXMLAttributeValueChar (EXMLSerializeVersion.HTML, '<'));
    assertFalse (XMLCharHelper.isInvalidXMLAttributeValueChar (EXMLSerializeVersion.XHTML, '<'));

    assertTrue (XMLCharHelper.isInvalidXMLAttributeValueChar (EXMLSerializeVersion.XML_10, '\u0001'));
    assertTrue (XMLCharHelper.isInvalidXMLAttributeValueChar (EXMLSerializeVersion.XML_11, '\u007f'));
    assertTrue (XMLCharHelper.isInvalidXMLAttributeValueChar (EXMLSerializeVersion.HTML, '\u007f'));
    assertTrue (XMLCharHelper.isInvalidXMLAttributeValueChar (EXMLSerializeVersion.XHTML, '\u007f'));
  }

  @Test
  public void testText ()
  {
    assertFalse (XMLCharHelper.isInvalidXMLTextChar (EXMLSerializeVersion.XML_10, '<'));
    assertFalse (XMLCharHelper.isInvalidXMLTextChar (EXMLSerializeVersion.XML_11, '<'));
    assertFalse (XMLCharHelper.isInvalidXMLTextChar (EXMLSerializeVersion.HTML, '<'));
    assertFalse (XMLCharHelper.isInvalidXMLTextChar (EXMLSerializeVersion.XHTML, '<'));
  }
}
