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

import java.awt.Polygon;

import xycharter.Figure;
import xycharter.Space;

/**
 * @author Luc Hogie
 */
public class PolygonFigureRenderer extends FigureRenderer
{
	private boolean filled = true;

	@Override
	public void drawImpl(Figure f, Space space)
	{
		int pointCount = f.getNbPoints();
		Polygon polygon = new Polygon();

		for (int i = 0; i < pointCount; ++i)
		{
			int px = space.getXDimension().convertToGraphicsCoordonateSystem(f.x(i));
			int py = space.getYDimension().convertToGraphicsCoordonateSystem(f.y(i));
			polygon.addPoint(px, py);
		}

		if (filled)
		{
			space.getFigureGraphics().fillPolygon(polygon);
		}
		else
		{
			space.getFigureGraphics().drawPolygon(polygon);
		}
	}

	/**
	 * Returns the filled.
	 * 
	 * @return boolean
	 */
	public boolean isFilled()
	{
		return filled;
	}

	/**
	 * Sets the filled.
	 * 
	 * @param filled
	 *            The filled to set
	 */
	public void setFilled(boolean filled)
	{
		this.filled = filled;
	}

	public String getPublicName()
	{
		return "polygone";
	}

}
