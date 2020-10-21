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

package xycharter.render;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

import xycharter.Dimension;
import xycharter.Figure;
import xycharter.Space;

/**
 * @author Luc Hogie
 */
public class TextPointRenderer extends FigureRenderer
{
	private Font font = new Font(null, Font.PLAIN, 12);
	private TextProvider p;

	public static interface TextProvider
	{
		String point2text(double x, double y);
	}

	public TextPointRenderer(TextProvider p)
	{
		this.p = p;
	}

	@Override
	public void drawImpl(Figure f, Space space)
	{
		for (int i = 0; i < f.getNbPoints(); ++i)
		{
			Dimension xDimension = space.getXDimension();
			Dimension yDimension = space.getYDimension();
			space.getFigureGraphics().setColor(f.getColor());
			FontRenderContext frc = space.getFigureGraphics().getFontRenderContext();
			double x = f.x(i);
			double y = f.y(i);
			GlyphVector gv = font.createGlyphVector(frc, p.point2text(x, y));
			Rectangle2D r = gv.getLogicalBounds();
			int xx = xDimension.convertToGraphicsCoordonateSystem(x);
			int yy = yDimension.convertToGraphicsCoordonateSystem(y);
			xx = xx - (int) r.getWidth() / 2 + getXShift();
			yy = yy + (int) r.getHeight() / 2 + getYShift();
			space.getFigureGraphics().drawGlyphVector(gv, xx, yy);
		}
	}

	/**
	 * Returns the font.
	 */
	public Font getFont()
	{
		return font;
	}

	/**
	 * Sets the font.
	 */
	public void setFont(Font font)
	{
		if (font == null)
			throw new IllegalArgumentException("font cannot be set to null");

		this.font = font;
	}

	@Override
	public String getPublicName()
	{
		return "text";
	}

}
