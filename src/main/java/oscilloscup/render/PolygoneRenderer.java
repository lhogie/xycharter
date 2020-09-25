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
import oscilloscup.Figure.Point;
import oscilloscup.Space;

/**
 * @author Luc Hogie
 */
public class PolygoneRenderer extends FigureRenderer {
	private int radius = 10;
	private int numberOfEdges = 3;
	private Color fillColor = Color.white;

	@Override
	public void drawImpl(Figure f, Space space) {
		for (int i = 0; i < f.getNbPoints(); ++i) {
			Dimension xDimension = space.getXDimension();
			Dimension yDimension = space.getYDimension();

			int x = xDimension.convertToGraphicsCoordonateSystem(f.x(i));
			int y = yDimension.convertToGraphicsCoordonateSystem(f.y(i));

			if (radius == 0) {
				// draw nothing
			}
			if (radius == 1) {
				space.getFigureGraphics().drawLine(x, y, x, y);
			}
			else {
				Color color = f.getColor();
				int[] xs = new int[numberOfEdges];
				int[] ys = new int[numberOfEdges];
				
				findCoordinates(x, y, this.radius, 2 * Math.PI / numberOfEdges, xs, ys);

				if (fillColor != null) {
					space.getFigureGraphics().setColor(fillColor);
					space.getFigureGraphics().fillPolygon(xs, ys, numberOfEdges);
				}

				space.getFigureGraphics().setColor(color);
				space.getFigureGraphics().drawPolygon(xs, ys, numberOfEdges);
			}
		}
	}

	private void findCoordinates(double originx, double originy, int radius,
			double angle, int[] xs, int[] ys) {
		Point rotatingPoint = new Point();
		rotatingPoint.x = originx + radius;
		rotatingPoint.y = originy;

		for (int i = 0; i < xs.length; ++i) {
			xs[i] = (int) rotatingPoint.x;
			ys[i] = (int) rotatingPoint.y;
			rotate(rotatingPoint, angle, originx, originy);
		}
	}

	public static void rotate(oscilloscup.Figure.Point p, double angle, double origX,
			double origY) {
		double dx = p.x - origX;
		double dy = p.y - origY;
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		p.x = dx * cos - dy * sin + origX;
		p.y = dx * sin + dy * cos + origY;
	}

	/**
	 * Returns the size.
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Sets the size.
	 */
	public void setRadius(int size) {
		if (size < 0)
			throw new IllegalArgumentException("size must be >= 0");

		this.radius = size;
	}

	/**
	 * Returns the fillColor.
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Sets the fillColor.
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	@Override
	public String getPublicName() {
		return "polygon";
	}

}
