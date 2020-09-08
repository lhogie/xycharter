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

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * <p>
 * The axis line belong to an axis. The bounds of the line may differ from the
 * bounds of the parent axis.
 * </p>
 *
 * <p>
 * On the upper bound of the line is drawn an arrow.
 * </p>
 *
 * @author Luc Hogie
 */
public class AxisLine extends BoundedSpaceElement {
	private Arrow arrow;

	public AxisLine() {
		setArrow(new Arrow());
	}

	@Override
	public Axis getParent() {
		return (Axis) super.getParent();
	}

	/**
	 * Returns the arrow.
	 */
	public Arrow getArrow() {
		return arrow;
	}

	/**
	 * Sets the arrow.
	 */
	public void setArrow(Arrow arrow) {
		if (arrow == null)
			throw new IllegalArgumentException("arrow cannot be set to null");

		arrow.setParent(this);
		this.arrow = arrow;
	}

	protected void draw(Graphics spaceGraphics, Graphics2D graphics) {
		if (isVisible()) {
			Axis axis = (Axis) getParent();
			Dimension dimension = (Dimension) axis.getParent();

			if (isMinimumAutomatic())
				setMin(dimension.getMin(), false);

			if (isMaximumAutomatic())
				setMax(dimension.getMax(), false);

			graphics.setColor(getColor());
			int position = axis.getPosition();
			int v1 = dimension.convertToGraphicsCoordonateSystem(getMin());
			int v2 = dimension.convertToGraphicsCoordonateSystem(getMax());

			if (axis.getParent().isX()) {
				if (position == Axis.LOWER_BOUND) {
					int y = (int) graphics.getClipBounds().getHeight() - 1;
					graphics.drawLine(v1, y, v2, y);
				}
				else if (position == Axis.UPPER_BOUND) {
					graphics.drawLine(v1, 0, v2, 0);
				}
				else {
					int y = dimension.getSiblingDimension()
							.convertToGraphicsCoordonateSystem(0);
					graphics.drawLine(v1, y, v2, y);
				}
			}
			else {
				if (position == Axis.LOWER_BOUND) {
					graphics.drawLine(0, v1, 0, v2);
				}
				else if (position == Axis.UPPER_BOUND) {
					int x = (int) graphics.getClipBounds().getWidth() - 1;
					graphics.drawLine(x, v1, x, v2);
				}
				else {
					int x = dimension.getSiblingDimension()
							.convertToGraphicsCoordonateSystem(0);
					graphics.drawLine(x, v1, x, v2);
				}
			}

			// if (Math.abs(v2 - v1) >= getArrow().getLength() * 2)
			getArrow().draw(spaceGraphics);
		}
	}

	@Override
	public String toString() {
		return "Line";
	}
}
