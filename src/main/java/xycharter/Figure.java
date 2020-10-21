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
import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import xycharter.render.ConnectedLineFigureRenderer;
import xycharter.render.FigureRenderer;

/**
 * <p>
 * A figure is a container. Basically a figure in a sequence of points. A figure
 * can model what you want. For example if you have to display a curve (with
 * data that come for a multiple measurement of a physical event), you can
 * create a figure and add it each point to it. If you want each point to be
 * connected to the previous and next points, you have to set a
 * ConnectedPointFigureRenderer to the figure.
 * </p>
 * 
 * <p>
 * A figure also contains children figure: this is a recursive data structure.
 * This allow you to build a well organized data structure and display complex
 * stuff. For example, if you have to display the map of your house, you can
 * create a houseAndGarden figure that will contain garden and a house figure.
 * The house figure may contain a kitchen figure, a livingRoom figure, a
 * parentsRoom figure, a childrenRoom figure and a garage figure... Each figure
 * deleguates its painting to a dedicated (or shared, depends on what you want)
 * renderer.
 * </p>
 * 
 * <p>
 * The deep of the tree is not limited.
 * </p>
 * 
 * 
 * @author Luc Hogie
 */
public class Figure extends GraphicalElement {
	private DoubleList x = new DoubleArrayList();
	private DoubleList y = new DoubleArrayList();

	public String name;

	public final List<FigureRenderer> rendererList = new ArrayList<>();
	public final List<FigureListener> listeners = new ArrayList<>();

	public Figure() {
		setColor(Color.blue);
	}

	public void draw(Space space) {
		rendererList.forEach(r -> r.draw(this, space));
	}

	/**
	 * Gets the number of point in this figure.
	 */
	public int getNbPoints() {
		return x.size();
	}

	public double x(int index) {
		return x.getDouble(index);
	}

	public double y(int index) {
		return y.getDouble(index);
	}

	public static class Point {
		public double x, y;
	}

	public void xy(Point p, int index) {
		p.x = x(index);
		p.y = y(index);
	}

	/**
	 * Insert the given point at the given position.
	 */
	public void insertPoint(double x, double y, int position) {
		this.x.add(position, x);
		this.y.add(position, y);
		listeners.forEach(l -> l.pointInserted(this, position));
	}

	/**
	 * Appends the given point to the figure.
	 */
	public void addPoint(double x, double y) {
		insertPoint(x, y, getNbPoints());
	}

	/**
	 * Removes the point at the given position.
	 */
	public void removePointAt(int i) {
		double xx = x.removeDouble(i);
		double yy = y.removeDouble(i);
		listeners.forEach(l -> l.pointRemoved(this, xx, yy, i));
	}

	/**
	 * Remove all the points that figure contain.
	 */
	public void removeAllPoints() {
		while (getNbPoints() > 0) {
			removePointAt(getNbPoints() - 1);
		}
	}

	/**
	 * Gets the extremums of the figure.
	 * 
	 * @return Extremi
	 */
	public Extremi computeExtremums() {
		Extremi extrems = new Extremi();
		updateExtrems(extrems);
		return extrems;
	}

	public void updateExtrems(Extremi extrems) {
		int nbPoints = getNbPoints();

		for (int i = 0; i < nbPoints; ++i) {
			double x = this.x.getDouble(i);
			double y = this.y.getDouble(i);

			if (extrems.nbPoints == 0) {
				extrems.minX = extrems.maxX = x;
				extrems.minY = extrems.maxY = y;
			} else {
				extrems.minX = Math.min(extrems.minX, x);
				extrems.minY = Math.min(extrems.minY, y);
				extrems.maxX = Math.max(extrems.maxX, x);
				extrems.maxY = Math.max(extrems.maxY, y);
			}

			++extrems.nbPoints;
		}
	}

	@Override
	public Figure clone() {
		Figure clone = new Figure();
		clone.x.addAll(x);
		clone.y.addAll(y);
		clone.rendererList.addAll(new ArrayList<>(rendererList));
		clone.listeners.addAll(new ArrayList<>(listeners));
		return clone;
	}

	public void translate(double xs, double ys) {
		for (int i = 0; i < x.size(); ++i) {
			x.set(i, x.getDouble(i) + xs);
			y.set(i, y.getDouble(i) + ys);
		}
	}

	@Override
	public String toString() {
		return "figure " + name + " (" + getNbPoints() + " point(s))";
	}

	public static class Extremi {

		public double minX, minY, maxX, maxY;
		public int nbPoints = 0;

		@Override
		public String toString() {
			return "Extremi [minX=" + minX + ", minY=" + minY + ", maxX=" + maxX + ", maxY=" + maxY + "]";
		}

		public Object clone() {
			Extremi clone = new Extremi();
			clone.minX = minX;
			clone.minY = minY;
			clone.maxX = maxX;
			clone.maxY = maxY;
			return clone;
		}
	}

	public void retainsOnlyLastPoints(int n) {
		while (getNbPoints() > n) {
			removePointAt(0);
		}
	}

	public void addRenderer(FigureRenderer r) {
		rendererList.add(r);
	}

	public static String toStringCartesian(double x, double y) {
		return "(" + x + ", " + y + ")";
	}

	public String toStringPolar(int i) {
		return "(" + getDistance(i, 0, 0) + ", " + getAngle(i) + "°)";
	}

	public void rotate(int i, double angle, double xref, double yref) {
		double dx = x(i) - xref;
		double dy = y(i) - yref;
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		movePoint(i, dx * cos - dy * sin + xref, dx * sin + dy * cos + yref);
	}

	public void movePoint(int i, double x, double y) {
		this.x.set(i, x);
		this.y.set(i, y);
		listeners.forEach(l -> l.pointMoved(this, i));
	}

	public double getDistance(int i, double refx, double refy) {
		double dx = x(i) - refx;
		double dy = y(i) - refy;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public double getDistanceToOrigin(int pi) {
		return Math.sqrt(x(pi) * x(pi) + y(pi) * y(pi));
	}

	public double getAngle(int pi) {
		return Math.atan2(y(pi), x(pi));
	}

	public void setDistanceToOrigin(int i, double newd) {
		if (newd < 0)
			throw new IllegalArgumentException("distance can't be negative: " + newd);

		double ratio = newd / getDistance(i, 0, 0);
		movePoint(i, x(i) * ratio, y(i) * ratio);
	}

	public void setAngle(int i, double newAngle) {
		rotate(i, newAngle - getAngle(i), 0, 0);
	}

	public int getClosestPoint(double x, double y) {
		int closestPoint = -1;
		double distance = Integer.MAX_VALUE;

		for (int i = 0; i < getNbPoints(); ++i) {
			double d = getDistance(i, x, y);

			if (d < distance) {
				distance = d;
				closestPoint = i;
			}
		}

		return closestPoint;
	}

	public void setName(String s) {
		this.name = name;
	}

	public void display() {
		Plot p = new Plot();
		p.addFigure(this);
		p.display();
	}

	public void sort() {
		double[] xx = x.toDoubleArray();
		double[] yy = y.toDoubleArray();
		DoubleArrays.quickSort(xx, yy);
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
	}

	public static Figure fromFunction(Double2DoubleFunction function, double start, double end, double step) {
		Figure f = new Figure();

		for (double t = start; t < end; t += step) {
			f.addPoint(t, function.get(t));
		}

		return f;
	}

}
