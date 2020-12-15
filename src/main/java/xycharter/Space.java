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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;

import xycharter.Dimension.Orientation;

/**
 * <p>
 * A space is the abstract thing that defines the dimension X and Y. This models
 * the mathematical concept of space.
 * </p>
 * 
 * <p>
 * A space is made of:
 * <ul>
 * <li>2 dimensions (X and Y),
 * <li>an origin point with coordinates (0, 0),
 * <li>a scale that calculates the position on a Java Graphics2D of a logic
 * coordinate.
 * </ul>
 * </p>
 * 
 * <p>
 * The space features also a legend that will be drawn on top of the graphics
 * representation.
 * </p>
 * 
 * @author Luc Hogie.
 */

public class Space extends GraphicalElement {
	private Legend legend = new Legend("Ultimate Plotter");
	private Dimension xDimension;
	private Dimension yDimension;
	private Point2D originPoint = new Point2D.Double(0, 0);
	private Graphics2D figureGraphics;

	/*
	 * the imageObserver is the object that will render the image on its pane. it
	 * can be a SwingPlotter
	 */
	private ImageObserver imageObserver;

	private Color backgroundColor = Color.white;

	public Space() {
		Dimension xDimension = new Dimension(Orientation.X);
		setXDimension(xDimension);

		Dimension yDimension = new Dimension(Orientation.Y);
		setYDimension(yDimension);

		legend.setFont(new Font(null, Font.PLAIN, 20));
		getXDimension().getLowerBoundAxis().setVisible(false);
		getXDimension().getUpperBoundAxis().setVisible(false);
		getYDimension().getLowerBoundAxis().setVisible(false);
		getYDimension().getUpperBoundAxis().setVisible(false);
	}

	/**
	 * Sets the X dimension of the space.
	 * 
	 * @param xDimension
	 */
	public void setXDimension(Dimension xDimension) {
		if (xDimension == null)
			throw new IllegalArgumentException("X dimension cannot be set to null");

		this.xDimension = xDimension;
		xDimension.setParent(this);
	}

	/**
	 * Gets the X dimension of the space.
	 * 
	 * @return Dimension
	 */
	public Dimension getXDimension() {
		return xDimension;
	}

	/**
	 * Sets the Y dimension of the space.
	 * 
	 * @param yDimension
	 */
	public void setYDimension(Dimension yDimension) {
		if (yDimension == null)
			throw new IllegalArgumentException("Y dimension cannot be set to null");

		this.yDimension = yDimension;
		yDimension.setParent(this);
	}

	/**
	 * Gets the X dimension of the space.
	 * 
	 * @return Dimension
	 */
	public Dimension getYDimension() {
		return yDimension;
	}

	/**
	 * Gets the origin point (the point with (0, 0) coordinates) of the space.
	 * Obviously, the real position (on the Graphics2D) of this point is not (0, 0).
	 * 
	 * @return Point2D
	 */
	public Point2D getOriginPoint() {
		return originPoint;
	}

	/**
	 * Sets the range of the space. This is a facility method: this can be done
	 * directly manipulating the dimensions of the space.
	 * 
	 * Warning! This is a convenience method. The Dimension, Graduation and AxisLine
	 * classes allow you to have a better control.
	 * 
	 * @param xmin
	 * @param xmax
	 * @param xstep
	 * @param ymin
	 * @param ymax
	 * @param ystep
	 */
	public void setRange(double xmin, double xmax, double ymin, double ymax) {
		xDimension.setMin(xmin, true);
		xDimension.setMax(xmax, true);
		yDimension.setMin(ymin, true);
		yDimension.setMax(ymax, true);
	}

	/**
	 * Sets the visibility of the grid.
	 * 
	 * Warning! This is a facade method. The grid class feature many methods that
	 * allow a better control.
	 * 
	 * @param gridTracing
	 */
	public void setGridVisible(boolean gridTracing) {
		getXDimension().getGrid().setVisible(gridTracing);
		getYDimension().getGrid().setVisible(gridTracing);
	}

	public void setArrowsVisible(boolean b) {
		getXDimension().getLowerBoundAxis().getLine().getArrow().setVisible(b);
		getXDimension().getOriginAxis().getLine().getArrow().setVisible(b);
		getXDimension().getUpperBoundAxis().getLine().getArrow().setVisible(b);
		getYDimension().getLowerBoundAxis().getLine().getArrow().setVisible(b);
		getYDimension().getOriginAxis().getLine().getArrow().setVisible(b);
		getYDimension().getUpperBoundAxis().getLine().getArrow().setVisible(b);
	}

	public enum MODE {
		MATHS, PHYSICS
	}

	public void setMode(MODE mode) {
		getXDimension().getLegend().setFont(new Font(null, Font.PLAIN, 12));
		getYDimension().getLegend().setFont(new Font(null, Font.PLAIN, 12));

		if (mode == MODE.MATHS) {
			setBackgroundColor(Color.white);
			setColor(Color.black);

			getXDimension().getLowerBoundAxis().setVisible(false);
			getXDimension().getOriginAxis().setVisible(true);
			getXDimension().getUpperBoundAxis().setVisible(false);

			getYDimension().getLowerBoundAxis().setVisible(false);
			getYDimension().getOriginAxis().setVisible(true);
			getYDimension().getUpperBoundAxis().setVisible(false);
		} else if (mode == MODE.PHYSICS) {
			setBackgroundColor(Color.black);
			setColor(Color.white);

			getXDimension().getLowerBoundAxis().setVisible(true);
			getXDimension().getOriginAxis().setVisible(false);
			getXDimension().getUpperBoundAxis().setVisible(true);

			getYDimension().getLowerBoundAxis().setVisible(true);
			getYDimension().getOriginAxis().setVisible(false);
			getYDimension().getUpperBoundAxis().setVisible(true);

			getXDimension().getLowerBoundAxis().getLine().getArrow().setVisible(false);
			getXDimension().getUpperBoundAxis().getLine().getArrow().setVisible(false);
			getYDimension().getLowerBoundAxis().getLine().getArrow().setVisible(false);
			getYDimension().getUpperBoundAxis().getLine().getArrow().setVisible(false);
		}
	}

	public void draw(Graphics2D spaceGraphics, Graphics2D figureGraphics) {
		if (isVisible()) {
			// the first thing to do is to initialize the graduations and draw
			// the grids

			xDimension.getGrid().draw(figureGraphics);
			yDimension.getGrid().draw(figureGraphics);

			// then the rest can be drawn
			xDimension.draw(spaceGraphics, figureGraphics);
			yDimension.draw(spaceGraphics, figureGraphics);
		}
	}

	/**
	 * Returns the imageObserver.
	 * 
	 * @return ImageObserver
	 */
	public ImageObserver getImageObserver() {
		return imageObserver;
	}

	/**
	 * Sets the imageObserver.
	 * 
	 * @param imageObserver The imageObserver to set
	 */
	public void setImageObserver(ImageObserver imageObserver) {
		this.imageObserver = imageObserver;
	}

	public Legend getLegend() {
		return legend;
	}

	public void setLegend(Legend newLegend) {
		if (newLegend == null)
			throw new IllegalArgumentException("the legend cannot be set to null");

		this.legend = newLegend;
		legend.setParent(this);
	}

	/**
	 * Returns the backgroundColor.
	 * 
	 * @return Color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Sets the backgroundColor.
	 * 
	 * @param backgroundColor The backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		if (backgroundColor == null)
			throw new IllegalArgumentException("backgroundColor cannot be set to null");

		this.backgroundColor = backgroundColor;
	}

	public String toString() {
		return "Space";
	}

	public Graphics2D getFigureGraphics() {
		return figureGraphics;
	}

	public void setFigureGraphics(Graphics2D graphics2D) {
		figureGraphics = graphics2D;
	}

	public void setAutoBounds(boolean b) {
		getXDimension().setAutoBounds(b);
		getYDimension().setAutoBounds(b);
	}
}