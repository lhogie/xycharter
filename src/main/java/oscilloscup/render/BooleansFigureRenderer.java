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

import oscilloscup.Dimension;
import oscilloscup.Figure;
import oscilloscup.Space;

/**
 * @author Luc Hogie
 * 
 *         This renderer draws a line between each point and its subsequent
 *         point. This is a very common process used to draw curves.
 */

public class BooleansFigureRenderer extends FigureRenderer
{
	@Override
	public void drawImpl(Figure f, Space space)
	{
		int pointCount = f.getNbPoints();
		int step = 1;


		for (int i = step; i < pointCount; i += step)
		{
			Dimension xDimension = space.getXDimension();
			Dimension yDimension = space.getYDimension();

			int px = xDimension.convertToGraphicsCoordonateSystem(f.x(i));
			int py = yDimension.convertToGraphicsCoordonateSystem(f.y(i));

			int ppx = xDimension.convertToGraphicsCoordonateSystem(f.x(i - 1));
			int ppy = yDimension.convertToGraphicsCoordonateSystem(f.y(i - 1));

			space.getFigureGraphics().setColor(f.getColor());
			space.getFigureGraphics().setStroke(getStroke());
			space.getFigureGraphics().drawLine(ppx, ppy, px, ppy);
		}
	}

	@Override
	public String getPublicName()
	{
		return "connected line";
	}


}
