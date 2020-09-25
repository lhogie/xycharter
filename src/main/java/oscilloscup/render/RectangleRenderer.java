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

public class RectangleRenderer extends FigureRenderer
{
	private boolean filled = false;

	public boolean isFilled()
	{
		return filled;
	}

	public void setFilled(boolean filled)
	{
		this.filled = filled;
	}

	public void drawImpl(Figure f, Space space)
	{
		Dimension xDimension = space.getXDimension();
		Dimension yDimension = space.getYDimension();

		int p1x = xDimension.convertToGraphicsCoordonateSystem(f.x(0));
		int p1y = yDimension.convertToGraphicsCoordonateSystem(f.y(0));

		int p2x = xDimension.convertToGraphicsCoordonateSystem(f.x(1));
		int p2y = yDimension.convertToGraphicsCoordonateSystem(f.x(1));

		space.getFigureGraphics().setColor(f.getColor());
		space.getFigureGraphics().setStroke(getStroke());

		if (isFilled())
		{
			space.getFigureGraphics().fillRect(p1x, p1y, p2x, p2y);
		}
		else
		{
			space.getFigureGraphics().drawRect(p1x, p1y, p2x, p2y);
		}
	}

	public String getPublicName()
	{
		return "rectangle";
	}

}