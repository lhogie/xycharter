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

import java.awt.Color;

import xycharter.Dimension;
import xycharter.Figure;
import xycharter.Space;

/**
 * @author Luc Hogie
 */

public class HistogramPointRenderer extends FigureRenderer
{
	private double barWidth = 1;
	private P p;

	public interface P
	{
		Color f(double x, double y);
	}

	public HistogramPointRenderer(P p)
	{
		this.p = p;
	}

	@Override
	public void drawImpl(Figure f, Space space)
	{

		Dimension xDimension = space.getXDimension();
		Dimension yDimension = space.getYDimension();

		for (int i = 0; i < f.getNbPoints(); ++i)
		{
			double x = f.x(i);
			double y = f.y(i);
			int x1 = xDimension.convertToGraphicsCoordonateSystem(x - getBarWidth() / 2);
			int y1 = yDimension.convertToGraphicsCoordonateSystem(y);

			int x2 = xDimension
					.convertToGraphicsCoordonateSystem(x + getBarWidth() / 2);
			int y2 = yDimension.convertToGraphicsCoordonateSystem(0);
			space.getFigureGraphics().setColor(p.f(x, y));
			space.getFigureGraphics().fillRect(x1, y1, x2 - x1, y2 - y1);
			space.getFigureGraphics().setColor(java.awt.Color.black);
			space.getFigureGraphics().drawRect(x1, y1, x2 - x1, y2 - y1);
		}
	}

	public double getBarWidth()
	{
		return barWidth;
	}

	public void setBarWidth(double barWidth)
	{
		this.barWidth = barWidth;
	}

	@Override
	public String getPublicName()
	{
		return "bar chart";
	}

}