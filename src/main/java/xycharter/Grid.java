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
import java.awt.Graphics2D;

public class Grid extends GraphicalElement {
	private Graduation referenceGraduation;
	private double density = 0.25f;

	public Grid() {
		setColor(Color.lightGray);
		setVisible(true);
	}

	@Override
	public Dimension getParent() {
		return (Dimension) super.getParent();
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double d) {
		if (d < 0 || d > 1)
			throw new IllegalArgumentException("density must be in the [0, 1] interval");

		this.density = d;
	}

	protected void draw(Graphics2D graphics) {
		if (isVisible()) {
			Dimension dimension = (Dimension) getParent();
			Graduation graduation = getReferenceGraduation();
			double step = graduation.getStep();
			graphics.setColor(getColor());
			int gridStep = (int) (1 / getDensity());
			double min = dimension.getMin();
			double max = dimension.getMax();

			if (dimension.isX()) {
				int gh = (int) graphics.getClipBounds().getHeight();

				for (double i = 0; i <= max; i += step) {
					if (i > min) {
						int gx = dimension.convertToGraphicsCoordonateSystem(i);

						for (int gy = 0; gy < gh; gy += gridStep) {
							graphics.drawLine(gx, gy, gx, gy);
						}
					}
				}

				for (double i = - step; i >= min; i -= step) {
					if (i < max) {
						int gx = dimension.convertToGraphicsCoordonateSystem(i);

						for (int gy = 0; gy < gh; gy += gridStep) {
							graphics.drawLine(gx, gy, gx, gy);
						}
					}
				}
			}
			else {

				int gw = (int) graphics.getClipBounds().getWidth();

				for (double i = 0; i <= max; i += step) {
					// i = lucci.math.MathsUtilities.round(i, 0);

					if (i > min) {
						int gy = dimension.convertToGraphicsCoordonateSystem(i);

						for (int gx = 0; gx < gw; gx += gridStep) {
							graphics.drawLine(gx, gy, gx, gy);
						}
					}
				}

				for (double i = - step; i >= min; i -= step) {
					// i = lucci.math.MathsUtilities.round(i, 0);

					if (i < max) {
						int gy = dimension.convertToGraphicsCoordonateSystem(i);

						for (int gx = 0; gx < gw; gx += gridStep) {
							graphics.drawLine(gx, gy, gx, gy);
						}
					}
				}
			}
		}
	}

	/**
	 * Returns the referenceGraduation.
	 * 
	 * @return Graduation
	 */
	public Graduation getReferenceGraduation() {
		if (referenceGraduation == null) {
			Dimension dimension = (Dimension) getParent();
			return dimension.getOriginAxis().getGraduation();
		}
		else {
			return referenceGraduation;
		}
	}

	/**
	 * Sets the referenceGraduation.
	 * 
	 * @param referenceGraduation
	 *            The referenceGraduation to set
	 */
	public void setReferenceGraduation(Graduation referenceGraduation) {
		this.referenceGraduation = referenceGraduation;
	}

	@Override
	public String toString() {
		return "Grid";
	}
}
