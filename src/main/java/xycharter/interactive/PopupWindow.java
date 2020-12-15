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

package xycharter.interactive;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;

import xycharter.Figure;
import xycharter.FigureFactory;
import xycharter.SwingPlotter;
import xycharter.render.ConnectedLineFigureRenderer;

public class PopupWindow {
	public static void main(String[] args) {
		List<Double> xList = new Vector<Double>();
		xList.add(4d);
		xList.add(1d);
		xList.add(3d);

		List<Double> yList = new Vector<Double>();
		yList.add(1d);
		yList.add(4d);
		yList.add(3d);

		popupWindows(xList, yList);
	}

	public static void popupWindows(List<Double> x, List<Double> y) {
		popupWindows(x, y, "", "", "");
	}

	public static void popupWindows(List<Double> x, List<Double> y, String title, String xLegend, String yLegend) {
		JFrame frame = new JFrame();
		Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		double ratio = 0.5;
		frame.setSize((int) (ss.width * ratio), (int) (ss.height * ratio));
		frame.setLocation(ss.width / 2 - frame.getSize().width / 2, ss.height / 2 - frame.getSize().height / 2);

		SwingPlotter plotter = new SwingPlotter();
		Figure figure = FigureFactory.createFigure(x, y);
		figure.addRenderer(new ConnectedLineFigureRenderer());
		plotter.getPlot().addFigure(figure);
		plotter.getPlot().getSpace().getLegend().setText(title);
		plotter.getPlot().getSpace().getXDimension().getLegend().setText(xLegend);
		plotter.getPlot().getSpace().getYDimension().getLegend().setText(yLegend);
		frame.getContentPane().setLayout(new GridLayout(1, 1));
		frame.getContentPane().add(plotter);
		frame.setVisible(true);
	}
}
