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

package oscilloscup;

/**
 * @author Luc Hogie
 * @version 1.0
 */
import java.awt.Graphics;
import java.awt.Polygon;

import oscilloscup.Dimension.Orientation;

/**
 *
 * <p>
 * This models the arrow that is drawn on the upper bound of the parent axis
 * line.
 * </p>
 * 
 * <p>
 * It is possible to set the length and the width of this arrow.
 * </p>
 * 
 * @author Luc Hogie
 */
public class Arrow extends GraphicalElement {
	private int width = 8;
	private int length = 12;

	@Override
	public AxisLine getParent() {
		return (AxisLine) super.getParent();
	}

	protected void draw(Graphics spaceGraphics) {
		if (isVisible()) {
			AxisLine axisLine = getParent();
			Axis axis = axisLine.getParent();
			Dimension dimension = axis.getParent();
			Dimension siblingDimension = dimension.getSiblingDimension();

			if (axis.getPosition() == Axis.ORIGIN
					&& (dimension.getMin() >= 0 || dimension.getMax() <= 0)) {
				// if the graphics is too small, only origin axis's arrows
				// should be drawn
			}
			else {
				if (dimension.isX()) {
					// the arrow is drawn at the top of the axis line
					int x = dimension.convertToGraphicsCoordonateSystem(axisLine.getMax())
							+ siblingDimension.getLowerBoundAxis().getGraduation()
									.getDedicatedPixelCount();

					// assuming we're drawing the arrow for the upper bound axis
					int y = dimension.getUpperBoundAxis().getGraduation()
							.getDedicatedPixelCount();

					// if this is the arrow of the origin axis, we have to
					// change the y
					if (axis.getPosition() == Axis.ORIGIN) {
						y += siblingDimension.convertToGraphicsCoordonateSystem(0);
					}
					else if (axis.getPosition() == Axis.LOWER_BOUND) {
						y += siblingDimension.getGraphicsSize(spaceGraphics);
					}

					Polygon polygon = new Polygon();
					polygon.addPoint(x, y);
					polygon.addPoint(x - getLength(), y - getWidth() / 2);
					polygon.addPoint(x - getLength(), y + getWidth() / 2);
					spaceGraphics.setColor(getColor());
					spaceGraphics.drawPolygon(polygon);
					spaceGraphics.fillPolygon(polygon);
				}
				else {
					int x = dimension.getLowerBoundAxis().getGraduation()
							.getDedicatedPixelCount();
					int y = dimension.convertToGraphicsCoordonateSystem(axisLine.getMax())
							+ siblingDimension.getUpperBoundAxis().getGraduation()
									.getDedicatedPixelCount();

					if (axis.getPosition() == Axis.ORIGIN) {
						x += siblingDimension.convertToGraphicsCoordonateSystem(0);
					}
					else if (axis.getPosition() == Axis.UPPER_BOUND) {
						x += siblingDimension.getGraphicsSize(spaceGraphics);
					}

					Polygon polygon = new Polygon();
					polygon.addPoint(x, y);
					polygon.addPoint(x - getWidth() / 2, y + getLength());
					polygon.addPoint(x + getWidth() / 2, y + getLength());
					spaceGraphics.setColor(getColor());
					spaceGraphics.drawPolygon(polygon);
					spaceGraphics.fillPolygon(polygon);
				}
			}
		}
	}

	/**
	 * Returns the length of the arrow.
	 * 
	 * @return int
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns the width of the arrow.
	 * 
	 * @return int
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the length of the arrow.
	 * 
	 * @param length
	 *            The length to set
	 */
	public void setLength(int length) {
		if (length <= 0)
			throw new IllegalArgumentException("length must be > 0");

		this.length = length;
	}

	/**
	 * Sets the width of the arrow.
	 * 
	 * @param width
	 *            The width to set
	 */
	public void setWidth(int width) {
		if (width <= 0)
			throw new IllegalArgumentException("width must be > 0");

		this.width = width;
	}

	public String toString() {
		return "Arrow";
	}
}