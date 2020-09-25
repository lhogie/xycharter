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

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import oscilloscup.Figure.Extremi;
import toools.gui.Utilities;

/**
 * The Graphics2DPlotter is the component that paints the user's Graphics2D. The
 * paint process is not implemented in a Swing component because the user may
 * want to get a graphical representation of its 2D data without showing it on a
 * user interface widget: for example the user may want to export the graphical
 * representation to an image file or event directly print it.
 * 
 * @author Luc Hogie
 */
public class Plot {
	private final Map<String, Figure> figures = new HashMap<>();
	private Space space = new Space();
	private FigureLegend figureLegend = new FigureLegend();

	public int figureGraphicsX = 0;
	public int figureGraphicsY = 0;

	/**
	 * Returns the space that is used to layout the data representation.
	 * 
	 * @return Space
	 */
	public Space getSpace() {
		return space;
	}

	/**
	 * Sets she space that is used to layout the data representation.
	 * 
	 * @param space The space to set
	 */
	public void setSpace(Space space) {
		if (space == null)
			throw new IllegalArgumentException("Space cannot be set to null");

		this.space = space;
	}

	/**
	 * Paints the space and the figure on the given graphics.
	 */
	public void draw(Graphics2D g) {
		figureGraphicsX = figureGraphicsY = 0;
		g.setColor(space.getBackgroundColor());
		g.fillRect(0, 0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight());
		drawEverything(g);
	}

	private void drawEverything(Graphics2D g) {
		Legend mainLegend = space.getLegend();

		if (!mainLegend.isVisible()) {
			drawAxisLegendsAndAxisAndPoints(g);
		} else {
			int legendGraphicsX = 0;
			int legendGraphicsY = 0;
			int legendGraphicsW = (int) g.getClipBounds().getWidth();
			int legendGraphicsH = mainLegend.getFont().getSize() * 2;

			mainLegend.draw((Graphics2D) g.create(legendGraphicsX, legendGraphicsY, legendGraphicsW, legendGraphicsH));

			int curveAndAxisLegendsGraphicsX = 0;
			int curveAndAxisLegendsGraphicsY = legendGraphicsH;
			int curveAndAxisLegendsGraphicsW = legendGraphicsW;
			int curveAndAxisLegendsGraphicsH = (int) (g.getClipBounds().getHeight() - legendGraphicsH);
			drawAxisLegendsAndAxisAndPoints((Graphics2D) g.create(curveAndAxisLegendsGraphicsX,
					curveAndAxisLegendsGraphicsY, curveAndAxisLegendsGraphicsW, curveAndAxisLegendsGraphicsH));

			figureGraphicsY += curveAndAxisLegendsGraphicsY;
		}

	}

	private void drawAxisLegendsAndAxisAndPoints(Graphics2D g) {
		Dimension xDimension = space.getXDimension();
		Dimension yDimension = space.getYDimension();
		Legend xLegend = xDimension.getLegend();
		Legend yLegend = yDimension.getLegend();

		if (xLegend.isVisible() && yLegend.isVisible()) {
			int curveGraphicsX = yLegend.getFont().getSize() * 2;
			int curveGraphicsY = 0;
			int curveGraphicsW = (int) (g.getClipBounds().getWidth() - curveGraphicsX);
			int curveGraphicsH = (int) g.getClipBounds().getHeight() - xLegend.getFont().getSize() * 2;
			Graphics2D spaceGraphics = (Graphics2D) g.create(curveGraphicsX, curveGraphicsY, curveGraphicsW,
					curveGraphicsH);
			drawSpaceAndFigure(spaceGraphics);

			int xLegendGraphicsX = curveGraphicsX;
			int xLegendGraphicsY = curveGraphicsH;
			int xLegendGraphicsW = curveGraphicsW;
			int xLegendGraphicsH = (int) (g.getClipBounds().getHeight() - curveGraphicsH);
			Graphics2D xLegendGraphics = (Graphics2D) g.create(xLegendGraphicsX, xLegendGraphicsY, xLegendGraphicsW,
					xLegendGraphicsH);
			xLegend.draw(xLegendGraphics);

			int yLegendGraphicsX = 0;
			int yLegendGraphicsY = 0;
			int yLegendGraphicsW = curveGraphicsX;
			int yLegendGraphicsH = curveGraphicsH;
			Graphics2D yLegendGraphics = (Graphics2D) g.create(yLegendGraphicsX, yLegendGraphicsY, yLegendGraphicsW,
					yLegendGraphicsH);
			yLegend.draw(yLegendGraphics);

			figureGraphicsX += curveGraphicsX;
			figureGraphicsY += curveGraphicsY;
		} else if (xLegend.isVisible()) {
			int curveGraphicsX = 0;
			int curveGraphicsY = 0;
			int curveGraphicsW = (int) (g.getClipBounds().getWidth() - curveGraphicsX);
			int curveGraphicsH = (int) g.getClipBounds().getHeight() - xLegend.getFont().getSize() * 2;
			Graphics2D spaceGraphics = (Graphics2D) g.create(curveGraphicsX, curveGraphicsY, curveGraphicsW,
					curveGraphicsH);
			drawSpaceAndFigure(spaceGraphics);

			int xLegendGraphicsX = curveGraphicsX;
			int xLegendGraphicsY = curveGraphicsH;
			int xLegendGraphicsW = curveGraphicsW;
			int xLegendGraphicsH = (int) (g.getClipBounds().getHeight() - curveGraphicsH);
			Graphics2D xLegendGraphics = (Graphics2D) g.create(xLegendGraphicsX, xLegendGraphicsY, xLegendGraphicsW,
					xLegendGraphicsH);
			xLegend.draw(xLegendGraphics);

			figureGraphicsX += curveGraphicsX;
			figureGraphicsY += curveGraphicsY;
		} else if (yLegend.isVisible()) {
			int curveGraphicsX = yLegend.getFont().getSize() * 2;
			int curveGraphicsY = 0;
			int curveGraphicsW = (int) (g.getClipBounds().getWidth() - curveGraphicsX);
			int curveGraphicsH = (int) g.getClipBounds().getHeight();
			Graphics2D spaceGraphics = (Graphics2D) g.create(curveGraphicsX, curveGraphicsY, curveGraphicsW,
					curveGraphicsH);
			drawSpaceAndFigure(spaceGraphics);

			int yLegendGraphicsX = 0;
			int yLegendGraphicsY = 0;
			int yLegendGraphicsW = curveGraphicsX;
			int yLegendGraphicsH = curveGraphicsH;
			Graphics2D yLegendGraphics = (Graphics2D) g.create(yLegendGraphicsX, yLegendGraphicsY, yLegendGraphicsW,
					yLegendGraphicsH);
			yLegend.draw(yLegendGraphics);

			figureGraphicsX += curveGraphicsX;
			figureGraphicsY += curveGraphicsY;
		} else {
			drawSpaceAndFigure(g);
		}
	}

	private void drawSpaceAndFigure(Graphics2D spaceGraphics) {
		Dimension xDimension = getSpace().getXDimension();
		Dimension yDimension = getSpace().getYDimension();

		Extremi ex = new Extremi();
		figures.values().forEach(f -> f.updateExtrems(ex));
		xDimension.updateBounds(ex);
		yDimension.updateBounds(ex);

		// the size required to set the size of the X graduation is known using
		// the
		// size of the font used. This size is used to calculate the Y size of
		// the
		// figure graphics, then the Y shown steps, then the size required to
		// show the
		// Y graduation
		double ySize = spaceGraphics.getClipBounds().getHeight()
				- space.getXDimension().getLowerBoundAxis().getGraduation().getFont().getSize()
				- space.getXDimension().getLowerBoundAxis().getGraduation().getPixelCountBetweenAxisLineAndText()
				- space.getXDimension().getUpperBoundAxis().getGraduation().getFont().getSize()
				- space.getXDimension().getUpperBoundAxis().getGraduation().getPixelCountBetweenAxisLineAndText();

		double xSize = spaceGraphics.getClipBounds().getWidth()
				- space.getYDimension().getLowerBoundAxis().getGraduation().getDedicatedPixelCount()
				- space.getYDimension().getLowerBoundAxis().getGraduation().getPixelCountBetweenAxisLineAndText()
				- space.getYDimension().getUpperBoundAxis().getGraduation().getDedicatedPixelCount()
				- space.getYDimension().getUpperBoundAxis().getGraduation().getPixelCountBetweenAxisLineAndText();

		if (xSize > 0 && ySize > 0) {
			space.getYDimension().getLowerBoundAxis().getGraduation().update(ySize, spaceGraphics);
			space.getYDimension().getOriginAxis().getGraduation().update(ySize, spaceGraphics);
			space.getYDimension().getUpperBoundAxis().getGraduation().update(ySize, spaceGraphics);

			space.getXDimension().getLowerBoundAxis().getGraduation().update(xSize, spaceGraphics);
			space.getXDimension().getOriginAxis().getGraduation().update(xSize, spaceGraphics);
			space.getXDimension().getUpperBoundAxis().getGraduation().update(xSize, spaceGraphics);

			int xp = space.getXDimension().getLowerBoundAxis().getGraduation().getDedicatedPixelCount();
			int yp = space.getYDimension().getLowerBoundAxis().getGraduation().getDedicatedPixelCount();
			int xs = space.getXDimension().getUpperBoundAxis().getGraduation().getDedicatedPixelCount();
			int ys = space.getYDimension().getUpperBoundAxis().getGraduation().getDedicatedPixelCount();
			int curveGraphicsX = yp;
			int curveGraphicsY = xs;
			int curveGraphicsW = (int) spaceGraphics.getClipBounds().getWidth() - yp - ys;
			int curveGraphicsH = (int) spaceGraphics.getClipBounds().getHeight() - xp - xs;

			figureGraphicsX += curveGraphicsX;
			figureGraphicsY += curveGraphicsY;

			if (curveGraphicsW > 0 && curveGraphicsH > 0) {
				Graphics2D figureGraphics = (Graphics2D) spaceGraphics.create(curveGraphicsX, curveGraphicsY,
						curveGraphicsW, curveGraphicsH);
				space.setFigureGraphics(figureGraphics);
				updateFigureRightBeforePainting();

				double xRange = xDimension.getMax() - xDimension.getMin();
				double yRange = yDimension.getMax() - yDimension.getMin();
				space.getOriginPoint().setLocation(
						(int) (figureGraphics.getClipBounds().getWidth() * -xDimension.getMin() / xRange),
						(int) (figureGraphics.getClipBounds().getHeight() * yDimension.getMax() / yRange));

				space.draw(spaceGraphics, figureGraphics);

				for (Figure f : figures.values()) {
					f.draw(space);
					figureLegend.draw(figureGraphics, space, this);
				}

				// the figure graphics shouldn't be set to null because it will
				// be
				// required for calculating
				// the logical location of user mouse pointing on the curve,
				// which
				// aim at enabling
				// user selections
				// space.setFigureGraphics(null);
			}
		}

	}

	/**
	 * The user should redefine this method if he wants the figure (and/or its
	 * renderers) to be defined just before repainting.
	 */
	public void updateFigureRightBeforePainting() {
	}

	public FigureLegend getFigureLegend() {
		return figureLegend;
	}

	public void display() {
		SwingPlotter plotter = new SwingPlotter();
		plotter.setPlot(this);
		Utilities.displayInJFrame(plotter, "xytracer");
	}

	public void addFigure(Figure figure) {
		figures.put(figure.name, figure);
	}

	public int getNbFigures() {
		return figures.size();
	}

	public Collection<Figure> figures() {
		return figures.values();
	}

	public void removeAllFigures() {
		figures.clear();
		
	}
}
