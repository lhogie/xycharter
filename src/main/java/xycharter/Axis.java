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

package xycharter;

import java.awt.Graphics2D;

/**
 *
 * <p>
 * There are 3 axis in a dimension.
 * <ul>
 * <li>The lower bound axis,
 * <li>the upper bound axis,
 * <li>the origin axis.
 * </ul>
 * </p>
 * 
 * <p>
 * Because the orgin axis is not always visible (e.g. if the data is defined in
 * a strictly positive or stricty negative interval) we have the need to show
 * axis that are always visible and do not depend on the location of the input
 * data. The lower bound and upper bound axis are drawn respectively on the
 * lower and upper bounds of the parent dimensions.
 * </p>
 * 
 * <p>
 * An axis has an axis line that may or may not be drawn and a graduation.
 * </p>
 * 
 * <p>
 * On a dimension, each axis has its own line and graduation, which allow a very
 * powerful customization.
 * </p>
 *
 * @author Luc Hogie
 */
public class Axis extends GraphicalElement {
	private AxisLine line;
	private Graduation graduation;

	public final static int LOWER_BOUND = 0;
	public final static int ORIGIN = 1;
	public final static int UPPER_BOUND = 2;

	public Axis() {
		setGraduation(new Graduation());
		setLine(new AxisLine());
	}

	@Override
	public Dimension getParent() {
		return (Dimension) super.getParent();
	}

	/**
	 * Returns the axis line for this axis.
	 */
	public AxisLine getLine() {
		return line;
	}

	/**
	 * Sets the axis line for this axis.
	 */
	public void setLine(AxisLine line) {
		if (line == null)
			throw new IllegalArgumentException("axis line cannot be set to null");

		line.setParent(this);
		this.line = line;
	}

	/**
	 * Gets the graduation of the axis.
	 */
	public Graduation getGraduation() {
		return graduation;
	}

	/**
	 * Sets the graduation of the axis.
	 */
	public void setGraduation(Graduation graduation) {
		if (graduation == null)
			throw new IllegalArgumentException("axis graduation cannot be set to null");

		this.graduation = graduation;
		graduation.setParent(this);
	}

	/**
	 * Returns the position of the axis in its parent dimension.
	 */
	public int getPosition() {
		Dimension dimension = (Dimension) getParent();

		if (this == dimension.getLowerBoundAxis()) {
			return LOWER_BOUND;
		}
		else if (this == dimension.getOriginAxis()) {
			return ORIGIN;
		}
		else if (this == dimension.getUpperBoundAxis()) {
			return UPPER_BOUND;
		}
		else {
			throw new IllegalStateException("the axis does not belong to a dimension");
		}
	}

	protected void draw(Graphics2D spaceGraphics, Graphics2D figureGraphics) {
		if (isVisible()) {
			getGraduation().draw(spaceGraphics, figureGraphics);
			getLine().draw(spaceGraphics, figureGraphics);
		}
	}

	protected Axis[] getSiblingAxis() {
		Dimension dimension = (Dimension) getParent();

		if (getPosition() == LOWER_BOUND) {
			return new Axis[] { dimension.getOriginAxis(),
					dimension.getUpperBoundAxis() };
		}
		else if (getPosition() == ORIGIN) {
			return new Axis[] { dimension.getLowerBoundAxis(),
					dimension.getUpperBoundAxis() };
		}
		else {
			return new Axis[] { dimension.getLowerBoundAxis(),
					dimension.getOriginAxis() };
		}
	}

	@Override
	public String toString() {
		if (getPosition() == LOWER_BOUND) {
			return "lower bound axis";
		}
		else if (getPosition() == UPPER_BOUND) {
			return "upper bound axis";
		}
		else if (getPosition() == ORIGIN) {
			return "origin axis";
		}
		else {
			return "unbound axis";
		}
	}
}
