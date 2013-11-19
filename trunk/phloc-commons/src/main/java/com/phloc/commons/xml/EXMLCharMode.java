package com.phloc.commons.xml;

/**
 * Defines the different characters modes that are relevant to determine invalid
 * characters as well characters to be masked.
 * 
 * @author Philip Helger
 */
public enum EXMLCharMode
{
  ELEMENT_NAME,
  ATTRIBUTE_NAME,
  ATTRIBUTE_VALUE,
  TEXT,
  CDATA;
}
