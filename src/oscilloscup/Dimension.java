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
import java.awt.Rectangle;

/**
 * <p>
 * This call models the mathematic concept of dimension: a 2-dimensional space
 * is constituted of 2 dimensions.
 * </p>
 * 
 * <p>
 * A dimension is itselft bounded because it may be graphically represented on a
 * different interval that its parent space.
 * </p>
 * 
 * @author Luc Hogie
 */
public class Dimension extends BoundedSpaceElement {
	public enum Orientation {
		X, Y
	}

	private Legend legend;
	private Axis lowerBoundAxis;
	private Axis upperBoundAxis;
	private Axis originAxis;
	private double borderRatio = 0.1;
	protected Grid grid;
	public final Orientation o;

	public Dimension(Orientation o) {
		this.o = o;
		setLowerBoundAxis(new Axis());
		setUpperBoundAxis(new Axis());
		setOriginAxis(new Axis());
		setLegend(new Legend());
		getLegend().setText(o.name());
		setGrid(new Grid());
	}

	public Space getParentSpace() {
		return (Space) getParent();
	}

	/**
	 * Gets the grid of the graduation.
	 * 
	 * @return Grid
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * Sets the grid of the graduation.
	 * 
	 * @param newGrid
	 */
	public void setGrid(Grid newGrid) {
		if (newGrid == null)
			throw new IllegalArgumentException("the grid cannot be set to null");

		this.grid = newGrid;
		this.grid.setParent(this);
	}

	/**
	 * Gets the lower bounds axis of the dimension
	 * 
	 * @return Axis
	 */
	public Axis getLowerBoundAxis() {
		return lowerBoundAxis;
	}

	/**
	 * Sets the lower bounds axis of the dimension.
	 * 
	 * @param axis
	 */
	public void setLowerBoundAxis(Axis axis) {
		if (axis == null)
			throw new IllegalArgumentException("axis cannot be set to null");

		this.lowerBoundAxis = axis;
		axis.setParent(this);
	}

	/**
	 * Gets the upper bounds axis of the dimension
	 * 
	 * @return Axis
	 */
	public Axis getUpperBoundAxis() {
		return upperBoundAxis;
	}

	/**
	 * Sets the upper bounds axis of the dimension.
	 * 
	 * @param axis
	 */
	public void setUpperBoundAxis(Axis axis) {
		if (axis == null)
			throw new IllegalArgumentException("axis cannot be set to null");

		this.upperBoundAxis = axis;
		axis.setParent(this);
	}

	/**
	 * Gets the origin axis of the dimension
	 * 
	 * @return Axis
	 */
	public Axis getOriginAxis() {
		return originAxis;
	}

	/**
	 * Sets the origin axis of the dimension.
	 * 
	 * @param axis
	 */
	public void setOriginAxis(Axis axis) {
		if (axis == null)
			throw new IllegalArgumentException("axis cannot be set to null");

		this.originAxis = axis;
		axis.setParent(this);
	}

	/**
	 * Returns the border.
	 * 
	 * @return float
	 */
	public double getBorder() {
		return borderRatio;
	}

	/**
	 * Sets the border.
	 * 
	 * @param border
	 *            The border to set
	 */
	public void setBorder(double border) {
		this.borderRatio = border;
	}

	/**
	 * Gets the legend of the dimension. This explains what is the dimension
	 * used for.
	 * 
	 * @return Legend
	 */
	public Legend getLegend() {
		return legend;
	}

	/**
	 * Sets the legend of the dimension. This explains what is the dimension
	 * used for.
	 * 
	 * @param newLegend
	 */
	public void setLegend(Legend newLegend) {
		if (newLegend == null)
			throw new IllegalArgumentException("axis legend cannot be set to null");

		this.legend = newLegend;
		legend.setParent(this);
	}

	protected void draw(Graphics2D spaceGraphics, Graphics2D figureGraphics) {
		if (isVisible()) {
			getLowerBoundAxis().draw(spaceGraphics, figureGraphics);
			getOriginAxis().draw(spaceGraphics, figureGraphics);
			getUpperBoundAxis().draw(spaceGraphics, figureGraphics);
		}
	}

	public void updateBounds(Figure.Extremi extremi) {
		double min = getMin(), max = getMax();
		// System.out.println(isMinimumAutomatic());
		if (isMinimumAutomatic() && extremi.nbPoints > 0) {
			min = isX() ? extremi.minX : extremi.minY;
		}

		if (isMaximumAutomatic() && extremi.nbPoints > 0) {
			max = isX() ? extremi.maxX : extremi.maxY;
		}

		if (max < min) {
			double tmp = max;
			max = min;
			min = tmp;
		}
		else if (min == max) {
			--min;
			++max;
		}

		double range = max - min;

		if (isMinimumAutomatic())
			setMin(min - range * borderRatio, false);

		if (isMaximumAutomatic())
			setMax(max + range * borderRatio, false);
	}

	/**
	 * Computes the number of pixels used by the dimension on the given
	 * graphics.
	 */
	public int getGraphicsSize(Graphics graphics) {
		Rectangle bounds = graphics.getClipBounds();

		if (isX()) {
			return (int) bounds.getWidth();
		}
		else {
			return (int) bounds.getHeight();
		}
	}

	@Override
	public Space getParent() {
		return (Space) super.getParent();
	}

	public boolean isX() {
		return o == Orientation.X;
	}

	protected Dimension getSiblingDimension() {
		return isX() ? getParent().getYDimension() : getParent().getXDimension();
	}

	public int convertToGraphicsCoordonateSystem(double value) {
		Space space = getParent();
		int graphicsSize = getGraphicsSize(space.getFigureGraphics());

		if (value == getMin()) {
			return isX() ? 0 : graphicsSize - 1;
		}

		if (value == getMax()) {
			return isX() ? graphicsSize - 1 : 0;
		}

		double range = getMax() - getMin();
		double factor = graphicsSize / range;
		int valueOnGraphics = (int) (isX()
				? factor * value + space.getOriginPoint().getX()
				: - factor * value + space.getOriginPoint().getY());

		return valueOnGraphics;
	}

	public double convertToDimensionCoordonateSystem(int a) {
		double aa = a;
		Space space = getParent();
		double graphicsSize = getGraphicsSize(space.getFigureGraphics());

		double range = getMax() - getMin();
		double factor = graphicsSize / range;
		double valueOnDimension = (int) (isX()
				? (aa - space.getOriginPoint().getX()) / factor
				: (aa - space.getOriginPoint().getY()) / - factor);

		return valueOnDimension;
	}

	public double getLogicalIntervalRepresentedByOnePixel() {
		double logicalRange = getMax() - getMin();
		double graphicsSize = getGraphicsSize(getParent().getFigureGraphics());
		return logicalRange / graphicsSize;
	}

	public String toString() {
		String orientation = isX() ? "horizontal" : "vertical";
		return orientation + " dimension";
	}

	public void setAutoBounds(boolean b) {
		setMaximumIsAutomatic(b);
		setMinimumIsAutomatic(b);
	}

	public void setBounds(double min, double max) {
		setMin(min, true);
		setMax(max, true);
	}
}
