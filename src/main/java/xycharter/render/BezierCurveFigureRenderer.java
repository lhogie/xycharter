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

public class BezierCurveFigureRenderer extends TimLambertsCodeBasedInterpolatingFigureRenderer {
	public void drawImpl(Figure f, Space space) {
		init(f, space);

		Polygon pol = new Polygon();
		java.awt.Point q = p(0, 0);
		pol.addPoint(q.x, q.y);

		for (int i = 0; i < pts.npoints - 3; i += 3) {
			int STEPS = getStepCount();

			for (int j = 1; j <= STEPS; j++) {
				q = p(i, j / (float) STEPS);
				pol.addPoint(q.x, q.y);
			}
		}

		space.getFigureGraphics().drawPolyline(pol.xpoints, pol.ypoints, pol.npoints);
	}

	static float b(int i, float t) {
		switch (i) {
		case 0:
			return (1 - t) * (1 - t) * (1 - t);
		case 1:
			return 3 * t * (1 - t) * (1 - t);
		case 2:
			return 3 * t * t * (1 - t);
		case 3:
			return t * t * t;
		}

		return 0; // we only get here if an invalid i is specified
	}

	java.awt.Point p(int i, float t) {
		float px = 0;
		float py = 0;

		for (int j = 0; j <= 3; j++) {
			px += b(j, t) * pts.xpoints[i + j];
			py += b(j, t) * pts.ypoints[i + j];
		}

		return new java.awt.Point((int) Math.round(px), (int) Math.round(py));
	}

	@Override
	public String getPublicName() {
		return "Bezier";
	}

}