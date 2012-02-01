package com.phloc.commons.url;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;

/**
 * Test class for class {@link URLUtils}.
 * 
 * @author philip
 */
public final class URLUtilsTest
{
  @Test
  public void testGetCleanURL ()
  {
    assertEquals ("aeoeue", URLUtils.getCleanURLPartWithoutUmlauts ("äöü"));
    assertEquals ("AeoeUe", URLUtils.getCleanURLPartWithoutUmlauts ("ÄöÜ"));
    assertEquals ("Ae-Uesz", URLUtils.getCleanURLPartWithoutUmlauts ("Ä Üß"));
    assertEquals ("Weisze-Waeste", URLUtils.getCleanURLPartWithoutUmlauts ("Weiße Wäste"));
    assertEquals ("hallo", URLUtils.getCleanURLPartWithoutUmlauts ("hállò"));
    assertEquals ("ffi", URLUtils.getCleanURLPartWithoutUmlauts ("\uFB03"));
    assertEquals ("ffl", URLUtils.getCleanURLPartWithoutUmlauts ("\uFB04"));
    assertEquals ("hallo;jsessionid=1234", URLUtils.getCleanURLPartWithoutUmlauts ("hállò;jsessionid=1234"));
  }

  @Test
  public void testUrlEncodeDecode ()
  {
    String sDec = "hallo welt";
    String sEnc = URLUtils.urlEncode (sDec);
    assertEquals ("hallo+welt", sEnc);
    assertEquals (sDec, URLUtils.urlDecode (sEnc));

    // default: UTF-8
    sDec = "äöü";
    sEnc = URLUtils.urlEncode (sDec);
    assertEquals ("%C3%A4%C3%B6%C3%BC", sEnc);
    assertEquals (sDec, URLUtils.urlDecode (sEnc));

    sDec = "äöü";
    sEnc = URLUtils.urlEncode (sDec, CCharset.CHARSET_ISO_8859_1);
    assertEquals ("%E4%F6%FC", sEnc);
    assertEquals (sDec, URLUtils.urlDecode (sEnc, CCharset.CHARSET_ISO_8859_1));
  }
}
