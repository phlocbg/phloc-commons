/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.commons.actor.experimental;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * A GUI panel that shows an image as its contents.
 * 
 * @author BFEIGENB
 */
public class ImageView extends JPanel
{
  // TODO: move to new package

  public ImageView ()
  {
    setLayout (null);
  }

  public ImageView (final BufferedImage image)
  {
    this ();
    this.m_aImage = image;
  }

  protected BufferedImage m_aImage;

  public BufferedImage getImage ()
  {
    return m_aImage;
  }

  public void setImage (final BufferedImage image)
  {
    this.m_aImage = image;
    repaint ();
  }

  @Override
  protected void paintComponent (final Graphics g)
  {
    super.paintComponent (g);
    // System.out.printf("paintComponent: %s%n", this);
    final Graphics2D g2d = (Graphics2D) g;
    if (m_aImage != null)
    {
      g2d.drawImage (m_aImage, 0, 0, getWidth (), getHeight (), null);
    }
    else
    {
      g2d.setColor (Color.LIGHT_GRAY.brighter ());
      g2d.fillRect (0, 0, getWidth (), getHeight ());
      g2d.setColor (Color.RED);
      g2d.drawString ("No image yet!", getWidth () / 10, getHeight () / 2);
    }
  }
}
