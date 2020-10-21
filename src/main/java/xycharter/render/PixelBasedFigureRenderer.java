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

/*
 * Created on Dec 14, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package xycharter.render;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

import xycharter.Figure;
import xycharter.Space;

/**
 * @author luc.hogie
 */
public class PixelBasedFigureRenderer extends FigureRenderer
{
	private int xstep = 1, ystep = 1;

	@Override
	protected void drawImpl(Figure f, Space space)
	{
		List points = new Vector();

		for (int i = 0; i < f.getNbPoints(); ++i)
		{
			int x = space.getXDimension().convertToGraphicsCoordonateSystem(f.x(i));
			int y = space.getYDimension().convertToGraphicsCoordonateSystem(f.y(i));
			points.add(new int[] { x, y });
		}

		Dimension dimension = space.getFigureGraphics().getClip().getBounds().getSize();

		for (int x = 0; x < dimension.width; x += xstep)
		{
			for (int y = 0; y < dimension.height; y += ystep)
			{
				double temperature = getTemperature(x, y, points);
				space.getFigureGraphics().setColor(getColor(f.getColor(), temperature));
				space.getFigureGraphics().fillRect(x, y, xstep, ystep);
			}
		}
	}

	protected Color getColor(Color color, double temperature)
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue(),
				(int) (255 * temperature));
	}

	protected double getTemperature(int x, int y, List points)
	{
		double distance = getDistance(x, y, points);
		double f = 1f / (2f + (float) Math.cos(distance / 20f));
		return f;
	}

	private int getDistance(int x, int y, List points)
	{
		int minDistance = 1000;

		for (int i = 0; i < points.size(); ++i)
		{
			int[] ps = (int[]) points.get(i);
			int dx = Math.abs(x - ps[0]);
			int dy = Math.abs(y - ps[1]);
			int d = (int) Math.sqrt(dx * dx + dy * dy);

			if (d < minDistance)
				minDistance = d;
		}

		// System.out.println(distanceSum);
		return minDistance;
		// return 3;
	}

	@Override
	public String getPublicName()
	{
		return "pixel-based";
	}

}
