/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.xml.serialize;

import javax.annotation.Nonnull;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.factory.IFactory;

public final class SAXReaderFactory implements IFactory <org.xml.sax.XMLReader>
{
  @Nonnull
  public org.xml.sax.XMLReader create ()
  {
    return createXMLReader ();
  }

  @Nonnull
  public static org.xml.sax.XMLReader createXMLReader ()
  {
    try
    {
      org.xml.sax.XMLReader ret;
      if (true)
        ret = XMLReaderFactoryPhloc.createXMLReader ();
      else
      {
        // This fails with Xerces on the classpath
        ret = SAXParserFactory.newInstance ().newSAXParser ().getXMLReader ();
      }
      return ret;
    }
    catch (final ParserConfigurationException ex)
    {
      throw new InitializationException ("Failed to instantiate XML reader!", ex);
    }
    catch (final SAXException ex)
    {
      throw new InitializationException ("Failed to instantiate XML reader!", ex);
    }
  }
}
