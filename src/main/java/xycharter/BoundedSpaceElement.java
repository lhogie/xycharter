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

/**
 * <p>
 * The space object is made of (and itself is) bounded by numeric values.
 * </p>
 * 
 * <p>
 * The mathematical concept of space does not talk about an interval but here we
 * have to render the graphical representation of the space and so it is no
 * possible to consider the whole space: we want to display only the interval
 * required by the user.
 * </p>
 * 
 * <p>
 * A bounded graphical element is a graphical element plus the conception of
 * interval. This is used only for rendering. This class only brings the concept
 * of interval used for painting.
 * </p>
 * 
 * <p>
 * Knowing that the interval may be automatically deduced from the user data or
 * even from the parent bounded graphical elements, we assume that a bounded
 * graphical element can be <i>auto bounded</i>.
 * </p>
 * 
 * <p>
 * Note: the bounds values are inclusive (ex: [-1, 1])
 * </p>
 * 
 * @author Luc Hogie
 */

public class BoundedSpaceElement extends GraphicalElement {
	private double min = - 1, max = 1;
	private boolean minimumIsAutomatic = true;
	private boolean maximumIsAutomatic = true;

	public boolean isMaximumAutomatic() {
		return maximumIsAutomatic;
	}

	public void setMaximumIsAutomatic(boolean b) {
		this.maximumIsAutomatic = b;
	}

	public boolean isMinimumAutomatic() {
		return minimumIsAutomatic;
	}

	public void setMinimumIsAutomatic(boolean b) {
		this.minimumIsAutomatic = b;
	}

	/**
	 * @return the minimum value for the bounds. This cannot be lower than the
	 *         maximum value.
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @return the maximum value for the bounds. This cannot be greater than the
	 *         minimum value.
	 */
	public double getMax() {
		return max;
	}

	public void setMin(double min, boolean permanent) {
		this.minimumIsAutomatic = ! permanent;

		if (min < max) {
			this.min = min;
		}
		else {
			this.min = min;
			this.max = min + 1;
		}
	}

	public void setMax(double max, boolean permanent) {
		this.maximumIsAutomatic = ! permanent;

		if (max > min) {
			this.max = max;
		}
		else {
			this.max = max;
			this.min = max - 1;
		}
	}

	/**
	 * @return if the graphical element is visible at the given position.
	 */
	public boolean isVisibleAt(double position) {
		return isVisible() && getMin() <= position && position <= getMax();
	}
}