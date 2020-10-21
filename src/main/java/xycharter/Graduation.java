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
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graduation extends GraphicalElement {
	private Map<Double, GlyphVector> glyphVectors = new HashMap<Double, GlyphVector>();

	private double step = 1;

	private int dedicatedPixelCount = 0;

	private GraduationStepProperties stepProperties = new GraduationStepProperties();

	// distance in pixel between two subsequent steps
	private int stepPixelInterval = 40;

	private int pixelCountBetweenAxisLineAndText = 5;

	private Font font = new Font(null, Font.PLAIN, 9);

	@Override
	public Axis getParent() {
		return (Axis) super.getParent();
	}

	/**
	 * Gets the number of pixels of the area where the graduation labels are
	 * drawn.
	 * 
	 * @return int
	 */
	public int getDedicatedPixelCount() {
		if (isVisible()) {
			return dedicatedPixelCount;
		}
		else {
			return 5;
		}
	}

	/**
	 * Sets the number of pixels of the area where the graduation labels are
	 * drawn.
	 * 
	 * @param dedicatedPixelCount
	 */
	public void setDedicatedPixelCount(int dedicatedPixelCount) {
		if (dedicatedPixelCount < 0)
			throw new IllegalArgumentException("dedicated pixel count < 0");

		this.dedicatedPixelCount = dedicatedPixelCount;
	}

	/**
	 * Gets the step used to display the graduation labels.
	 * 
	 * @return double
	 */
	public double getStep() {
		return this.step;
	}

	private void setStep(double step) {
		if (step <= 0)
			throw new IllegalArgumentException("step: " + step);

		this.step = step;
	}

	/**
	 * Returns the stepInPixel.
	 * 
	 * @return int
	 */
	public int getStepPixelInterval() {
		return stepPixelInterval;
	}

	/**
	 * Sets the stepInPixel. This interval may not be strictly respected: it is
	 * used for automatic stepping, it helps the system to calculate the best
	 * step. Setting this property means that you want the plotter to use a
	 * stepping of, nearly, <code>n</code> pixels.
	 * 
	 * @param stepPixelInterval
	 *            The stepPixelInterval to set
	 */
	public void setStepPixelInterval(int stepPixelInterval) {
		if (stepPixelInterval <= 0)
			throw new IllegalArgumentException("step pixel interval must be > 0");

		this.stepPixelInterval = stepPixelInterval;
	}

	/**
	 * Returns the pixelCountBetweenAxisLineAndText.
	 * 
	 * @return int
	 */
	public int getPixelCountBetweenAxisLineAndText() {
		return pixelCountBetweenAxisLineAndText;
	}

	/**
	 * Sets the pixelCountBetweenAxisLineAndText.
	 * 
	 * @param pixelCountBetweenAxisLineAndText
	 *            The pixelCountBetweenAxisLineAndText to set
	 */
	public void setPixelCountBetweenAxisLineAndText(
			int pixelCountBetweenAxisLineAndText) {
		this.pixelCountBetweenAxisLineAndText = pixelCountBetweenAxisLineAndText;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		if (font == null)
			throw new IllegalArgumentException("graduation font cannot be set to null");

		this.font = font;
	}

	/**
	 * Returns the stepProperties.
	 * 
	 * @return GraduationStepProperties
	 */
	public GraduationStepProperties getStepProperties() {
		return stepProperties;
	}

	/**
	 * Sets the stepProperties.
	 * 
	 * @param stepProperties
	 *            The stepProperties to set
	 */
	public void setStepProperties(GraduationStepProperties stepProperties) {
		if (stepProperties == null)
			throw new NullPointerException("stepProperties cannot be set to null");

		this.stepProperties = stepProperties;
	}

	List<Double> shownSteps;

	public void update(double size, Graphics2D spaceGraphics) {
		if (size < 0)
			throw new IllegalArgumentException();

		Dimension dimension = getParent().getParent();

		int unnormalizedStepCount = (int) (size / getStepPixelInterval());

		if (unnormalizedStepCount > 0) {
			double range = dimension.getMax() - dimension.getMin();
			setStep(normalizeStep(range / unnormalizedStepCount));
		}
		else {
			setStep(1);
		}

		shownSteps = new ArrayList<>(unnormalizedStepCount);
		double max = dimension.getMax();

		for (double step = 0; step <= max; step += getStep()) {
			shownSteps.add(step);
		}

		double min = dimension.getMin();

		for (double step = - getStep(); step >= min; step -= getStep()) {
			shownSteps.add(step);
		}

		createGlyphVectors(spaceGraphics, spaceGraphics.getFontRenderContext());
	}

	private void createGlyphVectors(Graphics2D spaceGraphics, FontRenderContext frc) {
		glyphVectors = new HashMap<>();
		this.dedicatedPixelCount = 0;

		for (double step : shownSteps) {
			String text = stepProperties.getTextAt(step);

			if (text == null) {
				text = "";
			}

			// text = simplify(text);
			GlyphVector gv = font.createGlyphVector(frc, text);
			glyphVectors.put(step, gv);
			java.awt.Dimension dimension = gv.getPixelBounds(frc, 0, 0).getSize();
			int size = getParent().getParent().isX() ? dimension.height : dimension.width;

			if (size > this.dedicatedPixelCount) {
				this.dedicatedPixelCount = size;
			}
		}

		this.dedicatedPixelCount += getPixelCountBetweenAxisLineAndText();
	}

	protected void draw(Graphics2D spaceGraphics, Graphics2D figureGraphics) {
		if (isVisible()) {
			// if the origin axis is too near to the primary axis, the
			// graduation steps
			// of the origin axis are not displayed : this prevents an ugly
			// effect that text
			// drawn over another
			if ( ! graduationStepsMustBeHidden(figureGraphics)) {
				Dimension dimension = getParent().getParent();
				double max = dimension.getMax();
				double min = dimension.getMin();

				for (double i = 0; i <= max; i += getStep()) {
					// i = lucci.math.MathsUtilities.round(i, 0);

					if (i >= dimension.getMin()) {
						if (dimension.isX()) {
							drawHorizontalAxisGraduationStep(spaceGraphics, i);
						}
						else {
							drawVerticalAxisGraduationStep(spaceGraphics, i);
						}
					}
				}

				for (double i = - getStep(); i >= min; i -= getStep()) {
					// i = lucci.math.MathsUtilities.round(i, 0);

					if (i <= dimension.getMax()) {
						if (dimension.isX()) {
							drawHorizontalAxisGraduationStep(spaceGraphics, i);
						}
						else {
							drawVerticalAxisGraduationStep(spaceGraphics, i);
						}
					}
				}
			}
		}
	}

	// private DecimalFormat numberFormatter = new DecimalFormat("##0.#####E0");

	private void drawHorizontalAxisGraduationStep(Graphics2D spaceGraphics,
			double value) {
		Space space = (Space) getParent().getParent().getParent();
		Dimension xDimension = space.getXDimension();
		Dimension yDimension = space.getYDimension();

		int x = xDimension.convertToGraphicsCoordonateSystem(value)
				+ yDimension.getLowerBoundAxis().getGraduation().getDedicatedPixelCount();
		int y = getHorizontalAxisY(spaceGraphics);

		if (stepProperties.getLineLengthAt(value) > 0) {
			setGraphicsColor(spaceGraphics, stepProperties.getLineColorAt(value));

			if (((Axis) getParent()).getPosition() == Axis.UPPER_BOUND) {
				spaceGraphics.drawLine(x, y, x,
						y + stepProperties.getLineLengthAt(value));
			}
			else {
				spaceGraphics.drawLine(x, y, x,
						y - stepProperties.getLineLengthAt(value));
			}
		}

		// GlyphVector gv =
		// this.glyphVectors.get(numberFormatter.format(value));
		GlyphVector gv = this.glyphVectors.get(value);

		if (gv != null) {
			Rectangle2D r = gv.getPixelBounds(null, 0, 0);
			int textWidth = (int) r.getWidth();
			int textHeight = (int) r.getHeight();
			setGraphicsColor(spaceGraphics, stepProperties.getTextColorAt(value));
			int shift = getPixelCountBetweenAxisLineAndText();

			// only the upper bound X axis graduation is drawn on the top of the
			// axis line
			if (((Axis) getParent()).getPosition() == Axis.UPPER_BOUND) {
				spaceGraphics.drawGlyphVector(gv, x - textWidth / 2, y - shift);
			}
			else {
				int xshift = 0;

				if (((Axis) getParent()).getPosition() == Axis.ORIGIN) {
					if (value == 0) {
						if (space.getYDimension().getOriginAxis().getLine().isVisible()) {
							xshift = - textWidth / 2 - 5;
						}
					}
					else if (value == ((Dimension) getParent().getParent()).getMin()) {
						if (space.getYDimension().getLowerBoundAxis().getLine()
								.isVisible()) {
							xshift = textWidth / 2 + 1;
						}
					}
					else if (value == ((Dimension) getParent().getParent()).getMax()) {
						if (space.getYDimension().getUpperBoundAxis().getLine()
								.isVisible()) {
							xshift = - textWidth / 2 - 2;
						}
					}
				}

				spaceGraphics.drawGlyphVector(gv, x - textWidth / 2 + xshift,
						y + textHeight + shift);
			}
		}
	}

	private int getHorizontalAxisY(Graphics2D graphics) {
		int position = ((Axis) getParent()).getPosition();

		if (position == Axis.LOWER_BOUND) {
			return (int) graphics.getClipBounds().getHeight() - 1
					- getDedicatedPixelCount();
		}
		else if (position == Axis.UPPER_BOUND) {
			return getDedicatedPixelCount();
		}
		else {
			Dimension dimension = (Dimension) getParent().getParent();
			return dimension.getUpperBoundAxis().getGraduation().getDedicatedPixelCount()
					+ dimension.getSiblingDimension()
							.convertToGraphicsCoordonateSystem(0);
		}
	}

	private void drawVerticalAxisGraduationStep(Graphics2D graphics, double value) {
		Space space = (Space) getParent().getParent().getParent();
		Dimension xDimension = space.getXDimension();
		Dimension yDimension = space.getYDimension();
		int x = getVerticalAxisX(graphics);
		int y = yDimension.convertToGraphicsCoordonateSystem(value)
				+ xDimension.getUpperBoundAxis().getGraduation().getDedicatedPixelCount();

		if (stepProperties.getLineLengthAt(value) > 0) {
			setGraphicsColor(graphics, stepProperties.getLineColorAt(value));

			if (((Axis) getParent()).getPosition() == Axis.UPPER_BOUND) {
				graphics.drawLine(x, y, x - stepProperties.getLineLengthAt(value), y);
			}
			else {
				graphics.drawLine(x, y, x + stepProperties.getLineLengthAt(value), y);
			}
		}

		GlyphVector gv = this.glyphVectors.get(value);

		if (gv != null) {
			int shift = getPixelCountBetweenAxisLineAndText();
			Rectangle2D r = gv.getPixelBounds(null, 0, 0);
			int textWidth = (int) r.getWidth();
			int textHeight = (int) r.getHeight();
			setGraphicsColor(graphics, stepProperties.getTextColorAt(value));

			if (((Axis) getParent()).getPosition() == Axis.UPPER_BOUND) {
				graphics.drawGlyphVector(gv, x + shift, y + textHeight / 2);
			}
			else {
				int yshift = 0;

				if (((Axis) getParent()).getPosition() == Axis.ORIGIN) {
					if (value == 0) {
						if (xDimension.getOriginAxis().getLine().isVisibleAt(0)) {
							if (xDimension.getOriginAxis().getGraduation().isVisible()) {
								// if the text of the two dimension is the same
								// at the (0, 0) point
								if (space.getXDimension().getOriginAxis().getGraduation()
										.getStepProperties().getTextAt(0f)
										.equals(getStepProperties().getTextAt(0f))) {
									return;
								}
								else {
									yshift = - 5;
								}
							}
						}
					}
					else if (value == yDimension.getMin()) {
						if (space.getXDimension().getLowerBoundAxis().getLine()
								.isVisible()) {
							yshift = - textHeight / 2 - 1;
						}
					}
					else if (value == yDimension.getMax()) {
						if (space.getXDimension().getLowerBoundAxis().getLine()
								.isVisible()) {
							yshift = textHeight / 2 + 3;
						}
					}
				}

				graphics.drawGlyphVector(gv, x - textWidth - shift,
						y + textHeight / 2 + yshift);
			}
		}
	}

	/**
	 * This method will be invoked only the Y dimension.
	 */
	private int getVerticalAxisX(Graphics2D graphics) {
		int position = ((Axis) getParent()).getPosition();

		if (position == Axis.LOWER_BOUND) {
			return getDedicatedPixelCount();
		}
		else if (position == Axis.UPPER_BOUND) {
			return (int) graphics.getClipBounds().getWidth() - 1
					- getDedicatedPixelCount();
		}
		else {
			Dimension dimension = (Dimension) getParent().getParent();
			return ((Dimension) getParent().getParent()).getLowerBoundAxis()
					.getGraduation().getDedicatedPixelCount()
					+ dimension.getSiblingDimension()
							.convertToGraphicsCoordonateSystem(0);
		}
	}

	private boolean graduationStepsMustBeHidden(Graphics2D graphics) {
		Axis axis = (Axis) getParent();
		Dimension dimension = (Dimension) axis.getParent();

		if (axis.getPosition() == Axis.ORIGIN) {
			if (dimension.getLowerBoundAxis().getGraduation().isVisible()
					|| dimension.getUpperBoundAxis().getGraduation().isVisible()) {
				// if the origin is included in the dimension
				if (dimension.getMin() < 0 && 0 < dimension.getMax()) {
					// calculates the number of pixels used by the negative area
					// of the sibbling dimension
					Dimension sibblingDimension = dimension.getSiblingDimension();
					int size = sibblingDimension.getGraphicsSize(graphics);
					double negativeArrayRate = - sibblingDimension.getMin()
							/ (sibblingDimension.getMax() - sibblingDimension.getMin());
					size = (int) ((float) size * negativeArrayRate);

					Graduation lowerBoundAxisGraduation = dimension.getLowerBoundAxis()
							.getGraduation();

					if (lowerBoundAxisGraduation.isVisible()) {
						// if less than 50 pixels are dedicated to the negative
						// area of the
						// dimension, it's better the graduation step not to be
						// displayed
						return size < 50;
					}
					else {
						// the lower bound axis graduation is not visible, so
						// the origin bound axis graduation HAS to be shown
						// BUT if the text may be cutted by the limit of the
						// graphics, it's better that it's invisible
						return size < 1.5 * getPixelCountBetweenAxisLineAndText()
								+ getFont().getSize();
					}
				}
				else {
					return true;
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	private String simplify(String text) {
		if (text.endsWith("000000000000")) {
			text = text.substring(text.length() - 12) + 'T';
		}
		else if (text.endsWith("000000000")) {
			text = text.substring(text.length() - 9) + 'G';
		}
		else if (text.endsWith("000000")) {
			text = text.substring(text.length() - 6) + 'M';
		}
		else if (text.endsWith("000")) {
			text = text.substring(text.length() - 3) + 'K';
		}

		return text;
	}

	private static double normalizeStep(double step) {
		if (step == 0)
			throw new IllegalArgumentException("" + step);

		double intlog = Math.floor(Math.log(step) / Math.log(10));
		step = step * Math.pow(10, - intlog);
		step = Math.round(step);

		switch ((int) step) {
		case 0:
			step = 1;
			break;
		case 1:
			step = 1;
			break;
		case 2:
			step = 2;
			break;
		case 3:
			step = 2.5;
			break;
		case 4:
			step = 5;
			break;
		case 5:
			step = 5;
			break;
		case 6:
			step = 5;
			break;
		case 7:
			step = 5;
			break;
		case 8:
			step = 10;
			break;
		case 9:
			step = 10;
			break;
		}

		return step * Math.pow(10, intlog);
	}

	private void setGraphicsColor(Graphics2D graphics, Color color) {
		if (color == null) {
			color = getColor();
		}

		graphics.setColor(color);
	}

	@Override
	public String toString() {
		return "Graduation";
	}
}
