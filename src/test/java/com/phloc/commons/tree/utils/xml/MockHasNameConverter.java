package com.phloc.commons.tree.utils.xml;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.utils.MicroUtils;
import com.phloc.commons.name.MockHasName;

public final class MockHasNameConverter implements IConverterTreeXML <MockHasName>
{
  public void appendDataValue (final IMicroElement eDataElement, final MockHasName aAnyName)
  {
    eDataElement.appendElement ("name").appendText (aAnyName.getName ());
  }

  public MockHasName getAsDataValue (final IMicroElement eDataElement)
  {
    return new MockHasName (MicroUtils.getChildTextContent (eDataElement, "name"));
  }
}