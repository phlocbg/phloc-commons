package com.phloc.commons.color;

import java.awt.Color;

import com.phloc.commons.mutable.MutableFloat;

public class HSLColor
{
  private final MutableFloat m_aHue;
  private final MutableFloat m_aSaturation;
  private final MutableFloat m_aLightness;
  private final MutableFloat m_aAlpha;

  /**
   * Create a HSLColor object using an RGB Color object.
   *
   * @param aRGB
   *        the RGB Color object
   */
  public HSLColor (final Color aRGB)
  {
    this.m_aHue = new MutableFloat (0);
    this.m_aSaturation = new MutableFloat (0);
    this.m_aLightness = new MutableFloat (0);
    fromRGB (aRGB);
    this.m_aAlpha = new MutableFloat (aRGB.getAlpha () / 255.0f);
  }

  public HSLColor (final HSLColor aHSL)
  {
    this (aHSL.getHue (), aHSL.getSaturation (), aHSL.getLightness (), aHSL.getAlpha ());
  }

  /**
   * Create a HSLColor object using individual HSL values and a default alpha
   * value of 1.0.
   *
   * @param h
   *        is the Hue value in degrees between 0 - 360
   * @param s
   *        is the Saturation percentage between 0 - 100
   * @param l
   *        is the Lightness percentage between 0 - 100
   */
  public HSLColor (final float h, final float s, final float l)
  {
    this (h, s, l, 1.0f);
  }

  /**
   * Create a HSLColor object using individual HSL values.
   *
   * @param h
   *        the Hue value in degrees between 0 - 359
   * @param s
   *        the Saturation percentage between 0 - 100
   * @param l
   *        the Lightness percentage between 0 - 100
   * @param alpha
   *        the alpha value between 0 - 1
   */
  public HSLColor (final float h, final float s, final float l, final float alpha)
  {
    this.m_aHue = new MutableFloat (h);
    this.m_aSaturation = new MutableFloat (s);
    this.m_aLightness = new MutableFloat (l);
    this.m_aAlpha = new MutableFloat (alpha);
  }

  public HSLColor setHue (final float nHue)
  {
    if (nHue < 0 || nHue > 360)
    {
      throw new IllegalArgumentException ("Hue must be in the range of [0..360]");
    }
    this.m_aHue.set (nHue);
    return this;
  }

  public HSLColor addHue (final float nHue)
  {
    this.m_aHue.inc (nHue);
    while (this.m_aHue.floatValue () >= 360)
    {
      this.m_aHue.dec (360);
    }
    while (this.m_aHue.floatValue () < 0)
    {
      this.m_aHue.inc (360);
    }
    return this;
  }

  public HSLColor setLightness (final float nLightness)
  {
    if (nLightness < 0 || nLightness > 100)
    {
      throw new IllegalArgumentException ("Lightness must be in the range of [0..100]");
    }
    this.m_aLightness.set (nLightness);
    return this;
  }

  public HSLColor addLightness (final float nLightness)
  {
    this.m_aLightness.inc (nLightness);
    if (this.m_aLightness.floatValue () > 100)
    {
      this.m_aLightness.set (100);
    }
    if (this.m_aLightness.floatValue () < 0)
    {
      this.m_aLightness.set (0);
    }
    return this;
  }

  public HSLColor setSaturation (final float nSaturation)
  {
    if (nSaturation < 0 || nSaturation > 100)
    {
      throw new IllegalArgumentException ("Saturation must be in the range of [0..100]");
    }
    this.m_aSaturation.set (nSaturation);
    return this;
  }

  public HSLColor addSaturation (final float nSaturation)
  {
    this.m_aSaturation.inc (nSaturation);
    if (this.m_aSaturation.floatValue () > 100)
    {
      this.m_aSaturation.set (100);
    }
    if (this.m_aSaturation.floatValue () < 0)
    {
      this.m_aSaturation.set (0);
    }
    return this;
  }

  /**
   * Get the Alpha value.
   *
   * @return the Alpha value.
   */
  public float getAlpha ()
  {
    return this.m_aAlpha.floatValue ();
  }

  public HSLColor getComplementary ()
  {
    return new HSLColor (this).addHue (180);
  }

  /**
   * Get the Hue value.
   *
   * @return the Hue value.
   */
  public float getHue ()
  {
    return this.m_aHue.floatValue ();
  }

  /**
   * Get the Lightness value.
   *
   * @return the Lightness value.
   */
  public float getLightness ()
  {
    return this.m_aLightness.floatValue ();
  }

  /**
   * Get the RGB Color object represented by this HDLColor.
   *
   * @return the RGB Color object.
   */
  public Color getRGB ()
  {
    return toRGB (getHue (), getSaturation (), getLightness (), getAlpha ());
  }

  /**
   * Get the Saturation value.
   *
   * @return the Saturation value.
   */
  public float getSaturation ()
  {
    return this.m_aSaturation.floatValue ();
  }

  public static double getLuma (final Color aRGB)
  {
    return Math.sqrt (0.299 * Math.pow (aRGB.getRed (), 2) +
                      0.587 * Math.pow (aRGB.getGreen (), 2) +
                      0.114 * Math.pow (aRGB.getBlue (), 2));
  }

  public double getLuma ()
  {
    return getLuma (getRGB ());
  }

  @Override
  public String toString ()
  {
    return "HSLColor[h=" +
           this.m_aHue.floatValue () +
           ",s=" +
           this.m_aSaturation.floatValue () +
           ",l=" +
           this.m_aLightness.floatValue () +
           ",alpha=" +
           this.m_aAlpha.floatValue () +
           "]";
  }

  private void fromRGB (final Color color)
  {
    // Get RGB values in the range 0 - 1

    final float [] rgb = color.getRGBColorComponents (null);
    final float r = rgb[0];
    final float g = rgb[1];
    final float b = rgb[2];

    // Minimum and Maximum RGB values are used in the HSL calculations

    final float min = Math.min (r, Math.min (g, b));
    final float max = Math.max (r, Math.max (g, b));

    // Calculate the Hue

    float h = 0;

    if (max == min)
    {
      h = 0;
    }
    else
      if (max == r)
      {
        h = ((60 * (g - b) / (max - min)) + 360) % 360;
      }
      else
        if (max == g)
        {
          h = (60 * (b - r) / (max - min)) + 120;
        }
        else
          if (max == b)
          {
            h = (60 * (r - g) / (max - min)) + 240;
          }

    // Calculate the Lightness

    final float l = (max + min) / 2;

    // Calculate the Saturation

    float s = 0;

    if (max == min)
      s = 0;
    else
      if (l <= .5f)
        s = (max - min) / (max + min);
      else
        s = (max - min) / (2 - max - min);

    this.m_aHue.set (h);
    this.m_aSaturation.set (s * 100);
    this.m_aLightness.set (l * 100);
  }

  /**
   * Convert HSL values to a RGB Color with a default alpha value of 1.
   *
   * @param h
   *        Hue is specified as degrees in the range 0 - 360.
   * @param s
   *        Saturation is specified as a percentage in the range 1 - 100.
   * @param l
   *        Lightness is specified as a percentage in the range 1 - 100.
   * @return the RGB Color object
   */
  public static Color toRGB (final float h, final float s, final float l)
  {
    return toRGB (h, s, l, 1.0f);
  }

  /**
   * Convert HSL values to a RGB Color.
   *
   * @param h
   *        Hue is specified as degrees in the range 0 - 360.
   * @param s
   *        Saturation is specified as a percentage in the range 1 - 100.
   * @param l
   *        Lightness is specified as a percentage in the range 1 - 100.
   * @param alpha
   *        the alpha value between 0 - 1
   * @return the RGB Color object
   */
  public static Color toRGB (float h, float s, float l, final float alpha)
  {
    if (s < 0.0f || s > 100.0f)
    {
      final String message = "Color parameter outside of expected range - Saturation";
      throw new IllegalArgumentException (message);
    }

    if (l < 0.0f || l > 100.0f)
    {
      final String message = "Color parameter outside of expected range - Lightness";
      throw new IllegalArgumentException (message);
    }

    if (alpha < 0.0f || alpha > 1.0f)
    {
      final String message = "Color parameter outside of expected range - Alpha";
      throw new IllegalArgumentException (message);
    }

    // Formula needs all values between 0 - 1.

    h = h % 360.0f;
    h /= 360f;
    s /= 100f;
    l /= 100f;

    float q = 0;

    if (l < 0.5)
      q = l * (1 + s);
    else
      q = (l + s) - (s * l);

    final float p = 2 * l - q;

    float r = Math.max (0, getHueAsRGB (p, q, h + (1.0f / 3.0f)));
    float g = Math.max (0, getHueAsRGB (p, q, h));
    float b = Math.max (0, getHueAsRGB (p, q, h - (1.0f / 3.0f)));

    r = Math.min (r, 1.0f);
    g = Math.min (g, 1.0f);
    b = Math.min (b, 1.0f);

    return new Color (r, g, b, alpha);
  }

  private static float getHueAsRGB (final float p, final float q, float h)
  {
    if (h < 0)
      h += 1;

    if (h > 1)
      h -= 1;

    if (6 * h < 1)
    {
      return p + ((q - p) * 6 * h);
    }

    if (2 * h < 1)
    {
      return q;
    }

    if (3 * h < 2)
    {
      return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
    }

    return p;
  }
}
