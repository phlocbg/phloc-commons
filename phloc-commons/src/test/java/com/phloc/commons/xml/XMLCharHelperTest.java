package com.phloc.commons.xml;

import static org.junit.Assert.assertFalse;

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
    assertFalse (XMLCharHelper.isInvalidXMLAttributeValueChar (EXMLVersion.XML_10, '<'));
    assertFalse (XMLCharHelper.isInvalidXMLAttributeValueChar (EXMLVersion.XML_11, '<'));
  }

  @Test
  public void testText ()
  {
    assertFalse (XMLCharHelper.isInvalidXMLTextChar (EXMLVersion.XML_10, '<'));
    assertFalse (XMLCharHelper.isInvalidXMLTextChar (EXMLVersion.XML_11, '<'));
  }
}
