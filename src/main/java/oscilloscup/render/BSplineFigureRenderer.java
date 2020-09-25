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

import java.awt.Polygon;

import oscilloscup.Figure;
import oscilloscup.Space;

public class BSplineFigureRenderer extends TimLambertsCodeBasedInterpolatingFigureRenderer
{
	@Override
	public void drawImpl(Figure f, Space space)
	{
		init(f, space);
		Polygon pol = new Polygon();
		java.awt.Point q = p(2, 0);
		pol.addPoint(q.x, q.y);

		for (int i = 2; i < pts.npoints - 1; i++)
		{
			float STEPS = getStepCount();

			for (int j = 1; j <= STEPS; j++)
			{
				q = p(i, j / (float) STEPS);
				pol.addPoint(q.x, q.y);
			}
		}

		space.getFigureGraphics().drawPolyline(pol.xpoints, pol.ypoints, pol.npoints);
	}

	float b(int i, float t)
	{
		switch (i)
		{
		case - 2:
			return ((( - t + 3) * t - 3) * t + 1) / 6;
		case - 1:
			return (((3 * t - 6) * t) * t + 4) / 6;
		case 0:
			return ((( - 3 * t + 3) * t + 3) * t + 1) / 6;
		case 1:
			return (t * t * t) / 6;
		}

		return 0; // we only get here if an invalid i is specified
	}

	java.awt.Point p(int i, float t)
	{
		float px = 0;
		float py = 0;

		for (int j = - 2; j <= 1; j++)
		{
			px += b(j, t) * pts.xpoints[i + j];
			py += b(j, t) * pts.ypoints[i + j];
		}

		return new java.awt.Point((int) Math.round(px), (int) Math.round(py));
	}

	@Override
	public String getPublicName()
	{
		return "B-splines";
	}


}