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

package xycharter.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JTabbedPane;

import toools.gui.Utilities;
import toools.thread.AtomicDouble;
import toools.thread.Threads;
import xycharter.Figure;
import xycharter.GraduationStepProperties;
import xycharter.Space;
import xycharter.SwingPlotter;
import xycharter.render.CirclePointRenderer;
import xycharter.render.ClosedNaturalCubicSplineFigureRenderer;
import xycharter.render.ConnectedLineFigureRenderer;
import xycharter.render.FigureRenderer;
import xycharter.render.HistogramPointRenderer;
import xycharter.render.PixelBasedFigureRenderer;

public class Demo {
	public static void main(String[] args) throws Throwable {
		System.out.println("starting");
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("default", createDefaultModel());
		tabbedPane.addTab("maths", createMathematicalModel());
		tabbedPane.addTab("stats", createStatisticalModel());
		tabbedPane.addTab("autobounds", createAutobounds());
		tabbedPane.addTab("move", createMovingModel());
		tabbedPane.addTab("phy", createPhysicalModel());
		tabbedPane.addTab("grad", createSpecialGraduationStepsModel());
		tabbedPane.addTab("colors", createRadioModel());
		tabbedPane.addTab("legend", createLegendModel());
		tabbedPane.addTab("time-serie", createTimedModel());

		Utilities.displayInJFrame(tabbedPane, "Demo");
	}

	private static Component createTimedModel() {
		SwingPlotter plotter = new SwingPlotter();
		Figure figure = new Figure();
		plotter.getPlot().addFigure(figure);

		AtomicInteger t = new AtomicInteger();
		Threads.newThread_loop_periodic(100, () -> true, () -> {
			int x = t.getAndIncrement();
			figure.addPoint(x, Math.cos(Math.cos(x)));
		});

		plotter.refreshEveryMs(100, () -> true);
		figure.addRenderer(new ConnectedLineFigureRenderer());
		return plotter;
	}

	private static SwingPlotter createStatisticalModel() {
		// creates the histogram data
		// each point stands for a bar
		// the x is the index, the y is the value
		Figure figure = new Figure();
		figure.addPoint(1, 2);
		figure.addPoint(2, 4);
		figure.addPoint(3, 9);
		figure.addPoint(4, 11);
		figure.addPoint(5, 13);
		figure.addPoint(6, 18);
		figure.addPoint(7, 24);
		figure.rendererList.add(new HistogramPointRenderer((x,
				y) -> new Color(255 / 24 * (int) y, 0, 255 / 24 * (24 - (int) y), 128)));

		// creates the Swing component that will paint the figure
		SwingPlotter plotter = new SwingPlotter();

		// link the figure to the component
		plotter.getPlot().addFigure(figure);

		Space space = plotter.getPlot().getSpace();

		// sets the background color
		space.setBackgroundColor(new Color(12, 115, 10, 50));

		// define the bounds
		space.getXDimension().setBounds(0, 8);
		space.getYDimension().setBounds(0, 30);
		space.getYDimension().getOriginAxis().setVisible(false);
		space.getYDimension().getUpperBoundAxis().setVisible(false);
		space.getYDimension().getGrid().setColor(Color.black);

		space.getYDimension().getLegend().setText("Degrees");

		space.getXDimension().getOriginAxis().setVisible(false);
		space.getXDimension().getUpperBoundAxis().setVisible(false);
		space.getXDimension().getLowerBoundAxis().getGraduation().setVisible(false);
		space.getXDimension().getLowerBoundAxis().getLine().getArrow().setVisible(false);
		space.getXDimension().getLegend().setVisible(false);
		space.getLegend().setText("Average temperature");
		return plotter;
	}

	private static SwingPlotter createMovingModel() {
		final Figure figure = new Figure();
		figure.addPoint(1, 1);
		figure.addPoint(1, 1);
		figure.addPoint(1, 1);

		FigureRenderer bspl = new ClosedNaturalCubicSplineFigureRenderer();
		figure.setColor(Color.black);
		figure.addRenderer(bspl);

		SwingPlotter plotter = new SwingPlotter();

		// not using the image buffer make the display a lot faster, obviously
		plotter.setImageBufferedUsed(false);
		AtomicDouble angle = new AtomicDouble();
		int freq = 30;
		int periodMS = 1000 / freq;

		Threads.newThread_loop_periodic(periodMS, () -> true, () -> {
			double t = angle.get();

			for (int i = 0; i < 3; ++i) {
				figure.movePoint(i, Math.cos(i * t), Math.sin(2 * (i + 1) * t));
			}

			angle.set(t + Math.PI / 100);
		});


		plotter.getPlot().addFigure(figure);
		Space space = plotter.getPlot().getSpace();
		space.setBackgroundColor(Color.orange);

		space.getLegend().setText("Moving closed cubic splines");
		space.getLegend().setFont(new Font(null, Font.ITALIC, 14));
		space.getLegend().setColor(Color.black);
		space.setAutoBounds(false);
		space.setColor(Color.gray);

		space.setVisible(false);
		space.getYDimension().setBounds( - 1.2, 1.2);
		space.getXDimension().setBounds( - 1.2, 1.2);

		plotter.refreshEveryMs(periodMS, () -> true);

		return plotter;
	}

	private static SwingPlotter createAutobounds() {

		ParametricFunction f1 = x -> new Point(Math.cos(x), Math.sin(x));
		ParametricFunction f2 = x -> new Point(Math.cos(2 * x), Math.sin(2 * x));

		SwingPlotter plotter = new SwingPlotter();
		Figure figure = new Figure();
		figure.addPoint(0, 0);
		figure.addPoint(0, 0);
		figure.rendererList.add(new ConnectedLineFigureRenderer());
		figure.rendererList.add(new CirclePointRenderer(i -> 5));
		plotter.getPlot().addFigure(figure);

		// not using the image buffer make the display a lot faster, obviously
		plotter.setImageBufferedUsed(false);
		int freq = 30;
		int periodMS = 1000 / freq;
		AtomicDouble x = new AtomicDouble();

		Threads.newThread_loop_periodic(periodMS, () -> true, () -> {
			Point p = f1.f(x.get());
			figure.movePoint(0, p.x, p.y);
			Point p2 = f2.f(x.get());
			figure.movePoint(1, p2.x, p2.y);
			x.set(x.get() + 0.01);
		});

		plotter.refreshEveryMs(periodMS, () -> true);

		return plotter;
	}

	private static SwingPlotter createMathematicalModel() {
		ParametricFunction function = t -> new Point(t * Math.cos(t), Math.sin(t));
		Figure figure = function.toFigure(0, 2 * Math.PI, Math.PI / 100);
		figure.addRenderer(new ConnectedLineFigureRenderer());

		SwingPlotter plotter = new SwingPlotter();
		plotter.getPlot().addFigure(figure);

		Space space = plotter.getPlot().getSpace();
		space.getLegend().setText("x = t cos(t) ; y = sin(t)");

		return plotter;
	}

	private static SwingPlotter createPhysicalModel() {
		ParametricFunction f1 = d -> new Point(d, d * (d / 2 - 1) * (d / 4 - 2) + d + 2);
		Figure fig1 = f1.toFigure( - 3, 5, 0.5);
		fig1.setColor(Color.green);

		ParametricFunction f2 = d -> {
			Point p = f1.f(d);
			p.x = p.x + 1;
			p.y = - p.y / 2 - p.x;
			return p;
		};

		Figure fig2 = f2.toFigure( - 3, 5, 0.5);
		fig2.setColor(Color.orange);

		ParametricFunction af1 = d -> {
			Point p = f1.f(d);
			p.y += Math.random() * 3 - 1.5f;
			return p;
		};

		Figure aff1 = af1.toFigure( - 3, 5, 0.5);
		aff1.setColor(Color.blue);
		aff1.addRenderer(new CirclePointRenderer(i -> 5));


		SwingPlotter plotter = new SwingPlotter();
		plotter.getPlot().addFigure(fig1);
		plotter.getPlot().addFigure(fig2);
		plotter.getPlot().addFigure(aff1);
		Space space = plotter.getPlot().getSpace();
		space.setMode(Space.MODE.PHYSICS);
		space.getLegend().setVisible(false);
		space.getXDimension().getLegend().setText("Speed");
		space.getXDimension().getLegend().setFont(new Font(null, Font.PLAIN, 12));
		space.getYDimension().getLegend().setFont(new Font(null, Font.PLAIN, 12));
		space.getYDimension().getLegend().setText("Acceleration");

		return plotter;
	}

	private static SwingPlotter createSpecialGraduationStepsModel() {
		ParametricFunction function = t -> new Point(t, Math.cos(t) * t);
		Figure figure = function.toFigure( - 5, 10, 0.1);
		figure.addRenderer(new ConnectedLineFigureRenderer());

		SwingPlotter plotter = new SwingPlotter();
		plotter.getPlot().addFigure(figure);
		Space space = plotter.getPlot().getSpace();
		space.getLegend().setText("Special graduation");
		space.getXDimension().getLegend().setVisible(false);
		space.getYDimension().getLegend().setVisible(false);
		space.getXDimension().getLowerBoundAxis().setVisible(false);
		space.getXDimension().getUpperBoundAxis().setVisible(false);
		space.getYDimension().getLowerBoundAxis().setVisible(false);
		space.getYDimension().getUpperBoundAxis().setVisible(false);

		space.getXDimension().getOriginAxis().getGraduation()
				.setStepProperties(new GraduationStepProperties() {
					@Override
					public String getTextAt(double step) {
						if (step < 0) {
							return null;
						}
						else if ((int) step == step) {
							return super.getTextAt(step);
						}
						else {
							return null;
						}
					}

					@Override
					public int getLineLengthAt(double step) {
						if ((int) step == step) {
							return 4;
						}
						else if ((int) (step * 2) == step * 2) {
							return 3;
						}

						else {
							return 1;
						}
					}

					@Override
					public Color getTextColorAt(double step) {
						if (2 < step && step < 7) {
							return Color.red;
						}
						else {
							return null;
						}
					}
				});

		space.getXDimension().getGrid().setVisible(false);
		return plotter;
	}

	private static SwingPlotter createDefaultModel() {
		SwingPlotter plotter = new SwingPlotter();

		Space space = plotter.getPlot().getSpace();
		// space.setRange(-5f, 3f, 1f, -4f, 6f, 0.5f);
		return plotter;
	}

	private static SwingPlotter createRadioModel() {
		SwingPlotter plotter = new SwingPlotter();
		final Figure figure = new Figure();
		figure.addPoint(4, 2);
		plotter.getPlot().addFigure(figure);
		plotter.getPlot().getSpace().getXDimension().getOriginAxis().setVisible(false);
		plotter.getPlot().getSpace().getYDimension().getOriginAxis().setVisible(false);

		plotter.getPlot().getSpace().getXDimension().setMinimumIsAutomatic(false);
		plotter.getPlot().getSpace().getXDimension().setMaximumIsAutomatic(false);
		plotter.getPlot().getSpace().getYDimension().setMinimumIsAutomatic(false);
		plotter.getPlot().getSpace().getYDimension().setMaximumIsAutomatic(false);
		figure.addRenderer(new PixelBasedFigureRenderer());
		plotter.getPlot().getSpace().getXDimension().setBounds( - 10, 10);
		plotter.getPlot().getSpace().getYDimension().setBounds( - 10, 10);

		return plotter;
	}

	private static SwingPlotter createLegendModel() {
		SwingPlotter plotter = new SwingPlotter();

		{
			ParametricFunction function = d -> new Point(d, Math.cos(d));
			Figure figure = function.toFigure(0, 10, 0.1);
			figure.setName("cos(x)");
			FigureRenderer figureRenderer = new ConnectedLineFigureRenderer();
			figure.setColor(Color.blue);
			figure.addRenderer(figureRenderer);
			plotter.getPlot().addFigure(figure);
		}

		{
			ParametricFunction function = d -> new Point(d, Math.cos(2 * d + 1) * 2);
			Figure figure = function.toFigure( - 5, 5, 0.1);
			figure.setName("cos(2x + 1) * 2");
			FigureRenderer figureRenderer = new ConnectedLineFigureRenderer();
			figure.setColor(Color.red);
			figure.addRenderer(figureRenderer);
			plotter.getPlot().addFigure(figure);
		}

		{
			ParametricFunction function = d -> new Point(d, (int) Math.abs(d));
			Figure figure = function.toFigure( - 5, 5, 0.1);
			figure.setName("E(|x|)");
			FigureRenderer figureRenderer = new ConnectedLineFigureRenderer();
			figure.setColor(Color.green);
			figure.addRenderer(figureRenderer);
			plotter.getPlot().addFigure(figure);
		}

		Space space = plotter.getPlot().getSpace();
		space.getXDimension().getLowerBoundAxis().setVisible(false);
		space.getXDimension().getUpperBoundAxis().setVisible(false);
		space.getYDimension().getLowerBoundAxis().setVisible(false);
		space.getYDimension().getUpperBoundAxis().setVisible(false);
		space.getLegend().setVisible(false);
		space.getXDimension().getLegend().setText("X");
		space.getXDimension().getLegend().setFont(new Font(null, Font.PLAIN, 12));
		space.getYDimension().getLegend().setFont(new Font(null, Font.PLAIN, 12));
		space.getYDimension().getLegend().setText("Y");

		return plotter;
	}

}