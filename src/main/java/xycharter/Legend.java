/* (C) Copyright 2009-2013 CNRS (Centre National de la Recherche Scientifique).

Licensed to the CNRS under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The CNRS licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

*/

/* Contributors:

Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 

*/

package xycharter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

public class Legend extends GraphicalElement
{
	private static Font defaultFont = new Font(null, Font.PLAIN, 16);
	private String text = "No legend defined";
	private Font f = defaultFont;
	private Color backgroundColor = null;

	public Legend()
	{
		this("Legend");
	}

	public Legend(String text)
	{
		setText(text);
	}

	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	public void setBackgroundColor(Color b)
	{
		backgroundColor = b;
	}

	/**
	 * Gets the text of the legend.
	 * 
	 * @return String
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Sets the text of the legend.
	 * 
	 * @param text
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * Gets the font of the legend.
	 * 
	 * @return Font
	 */
	public Font getFont()
	{
		return f;
	}

	/**
	 * Sets the font of the legend.
	 * 
	 * @param f
	 */
	public void setFont(Font f)
	{
		if (f == null)
			throw new IllegalArgumentException("null font is not allowed");

		this.f = f;
	}

	public void draw(Graphics2D g)
	{
		if (text == null)
			return;

		if (backgroundColor != null)
		{
			g.setColor(backgroundColor);
			g.fillRect(0, 0, (int) g.getClipBounds().getWidth(),
					(int) g.getClipBounds().getHeight());
		}

		String text = getText();
		Font font = getFont();
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector gv = font.createGlyphVector(frc, text);
		Rectangle2D r = gv.getPixelBounds(null, 0, 0);
		int textWidth = (int) r.getWidth();
		int textHeight = (int) r.getHeight();
		int textX = (int) - textWidth / 2;
		int textY = (int) textHeight / 2;
		g.translate(g.getClipBounds().getWidth() / 2, g.getClipBounds().getHeight() / 2);

		// if the legend is the Y dimension legend, it has to be rotated
		if (getParent() instanceof Dimension && ((Dimension) getParent()).getParent()
				.getYDimension().getLegend() == this)
		{
			g.rotate( - Math.PI / 2);
		}

		g.setColor(getColor());
		g.drawGlyphVector(gv, textX, textY);
		g.translate( - g.getClipBounds().getWidth() / 2,
				- g.getClipBounds().getHeight() / 2);
	}

	public String toString()
	{
		return "Legend";
	}
}
