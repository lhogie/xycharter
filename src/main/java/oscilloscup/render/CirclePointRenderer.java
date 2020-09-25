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

package oscilloscup.render;

import java.awt.Color;

import oscilloscup.Dimension;
import oscilloscup.Figure;
import oscilloscup.Space;

/**
 * @author Luc Hogie
 */
public class CirclePointRenderer extends FigureRenderer
{
	private Color fillColor = Color.white;
	private boolean useGradient = false;
	private P p;

	public static interface P
	{
		int radius(int pointIndex);
	}
	
	public CirclePointRenderer(P p)
	{
		this.p = p;
	}
	
	@Override
	public void drawImpl(Figure f, Space space)
	{
		Dimension xDimension = space.getXDimension();
		Dimension yDimension = space.getYDimension();

		for (int pi = 0; pi < f.getNbPoints(); ++pi)
		{
			int radius = p.radius(pi);

			if (radius == 0)
				continue;
			
			int x = xDimension.convertToGraphicsCoordonateSystem(f.x(pi));
			int y = yDimension.convertToGraphicsCoordonateSystem(f.y(pi));

			if (radius == 1)
			{
				space.getFigureGraphics().drawLine(x, y, x, y);
			}
			else
			{
				Color color = f.getColor();

				if (useGradient)
				{
					for (int i = 1; i <= radius; ++i)
					{
						int hs = i / 2;
						Color c = new Color(color.getRed(), color.getGreen(),
								color.getBlue(), 255 - i * 255 / radius);
						space.getFigureGraphics().setColor(c);
						space.getFigureGraphics().drawOval(x - hs, y - hs, i, i);
					}
				}
				else
				{
					if (fillColor != null)
					{
						space.getFigureGraphics().setColor(fillColor);
						space.getFigureGraphics().fillOval(x - radius, y - radius,
								2 * radius, 2 * radius);
					}

					space.getFigureGraphics().setColor(color);
					space.getFigureGraphics().drawOval(x - radius, y - radius, 2 * radius,
							2 * radius);
				}
			}
		}

	}

	/**
	 * Returns the fillColor.
	 */
	public Color getFillColor()
	{
		return fillColor;
	}

	/**
	 * Returns the useGradient.
	 */
	public boolean isUseGradient()
	{
		return useGradient;
	}

	/**
	 * Sets the fillColor.
	 */
	public void setFillColor(Color fillColor)
	{
		this.fillColor = fillColor;
	}

	/**
	 * Sets the useGradient.
	 */
	public void setUseGradient(boolean useGradient)
	{
		this.useGradient = useGradient;
	}

	@Override
	public String getPublicName()
	{
		return "circle";
	}

}
