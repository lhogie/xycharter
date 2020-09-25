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

package oscilloscup.multiscup;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import oscilloscup.Figure;
import oscilloscup.SwingPlotter;
import oscilloscup.render.FigureRenderer;
import toools.thread.Threads;

public abstract class Multiscope<E> extends JPanel {
	private Palette palette = Palette.defaultPalette;
	private List<E> rows;
	private final List<Property<E>> displayedProperties;

	private final Map<E, Map<Property<E>, Figure>> row_figures = new HashMap<>();
	private final Map<Property<E>, PanePlotter> property_plotter = new HashMap<>();
	private final Map<String, PanePlotter> unit_plotter = new HashMap<>();

	private final SPTable<E> table;

	private final PlotterSetPanel propertiesPlottersPanel = new PlotterSetPanel();
	private final PlotterSetPanel unitsPlottersPanel = new PlotterSetPanel();
	private RefreshThread refreshThread;

	private int refreshPeriodMs = -1;

	public Multiscope(Class<E> c) {
		this(Property.findAllProperties(c));
	}

	public Multiscope(List<Property<E>> props) {
		super();

		if (props == null)
			throw new NullPointerException("null property list");

		if (props.contains(null))
			throw new NullPointerException("I found a null property in the property list");

		this.displayedProperties = new ArrayList<>(props);

		table = new SPTable<E>(props, e -> updateVisiblePlotPanels());
		table.setColorPalette(palette);

		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, table.createScrollPane());

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Properties", propertiesPlottersPanel);
		tabbedPane.addTab("Units", unitsPlottersPanel);
		tabbedPane.addChangeListener(e -> updateVisiblePlotPanels());

		add(BorderLayout.CENTER, tabbedPane);
	}

	public int getRefreshPeriodMs() {
		return refreshPeriodMs;
	}

	public void setRefreshPeriodMs(int rp) {
		if (rp != refreshPeriodMs) {
			this.refreshPeriodMs = rp;

			// ensureRefreshThreadIsStopped
			if (refreshThread != null) {
				refreshThread.shouldStop = true;
				refreshThread = null;
			}

			// if the refresh period is valid
			if (refreshPeriodMs >= 0) {
				// start refresh tread
				this.refreshThread = new RefreshThread();
				Threads.sleepMs(refreshPeriodMs);
				this.refreshThread.start();
			}
		}
	}

	public void setRows(E... rows) {
		setRows(Arrays.asList(rows));
	}

	public void setRows(List<E> rows) {
		this.rows = new ArrayList<>(rows);
		table.setModel(rows);

		for (int rowindex = 0; rowindex < rows.size(); ++rowindex) {
			E row = rows.get(rowindex);

			// set the row labels in the table
			table.getModel().setValueAt(getRowNameFor(row), rowindex, 0);

			// fills the table with the row itself. The renderer will extract
			// the
			// appropriate value for each cell
			for (int propertyIndex = 0; propertyIndex < displayedProperties.size(); ++propertyIndex) {
				table.getModel().setValueAt(row, rowindex, propertyIndex + 1);
			}
		}

		for (Property<E> p : displayedProperties) {
			PanePlotter plotter = new PanePlotter();
			/*
			 * if (p.getMinYDisplayed() != null) {
			 * plotter.getPlot().getSpace().getYDimension().setMin(p. getMinYDisplayed(),
			 * true); }
			 * 
			 * if (p.getMaxYDisplayed() != null) {
			 * plotter.getPlot().getSpace().getYDimension().setMax(p. getMaxYDisplayed(),
			 * true); }
			 */
			plotter.getPlot().getSpace().getXDimension().getLegend()
					.setText("time (" + p.getClock().getTimeUnit() + ")");
			plotter.getPlot().getSpace().getYDimension().getLegend().setText(p.getHumanReadableNameAndUnit());
			plotter.getPlot().getSpace().getLegend()
					.setText(plotter.getPlot().getSpace().getYDimension().getLegend().getText());
			property_plotter.put(p, plotter);
		}

		for (Set<Property<E>> set : Property.findPropertyByUnitAndClock(displayedProperties)) {
			Property<E> aProperty = set.iterator().next();
			String unit = aProperty.getUnit();
			Clock clock = aProperty.getClock();

			PanePlotter plotter = new PanePlotter();
			plotter.getPlot().getSpace().getLegend().setText(null);
			plotter.getPlot().getSpace().getYDimension().getLegend().setText(unit);
			plotter.getPlot().getSpace().getXDimension().getLegend()
					.setText("time " + (clock.getTimeUnit() == null ? "" : "(" + clock.getTimeUnit() + ")"));
			unit_plotter.put(unit, plotter);
		}

		for (E row : rows) {
			row_figures.put(row, new HashMap<Property<E>, Figure>());

			for (Property<E> p : displayedProperties) {
				Figure columnFigure = new Figure();

				columnFigure.addRenderer(p.createRenderer());
				// columnFigure.addRenderer(new ConnectedLineFigureRenderer());
				row_figures.get(row).put(p, columnFigure);
			}
		}
	}

	protected abstract String getRowNameFor(E row);

	synchronized public void newStep() {
		for (E row : rows) {
			Map<Property<E>, Figure> prop_fig = row_figures.get(row);

			for (Property<E> property : displayedProperties) {
				Figure f = prop_fig.get(property);
				Double v = property.getPoint(row);
				double x = property.getClock().getTime();

				if (v != null) {
					if (f.getNbPoints() == 0) {
						f.addPoint(x, v);
					} else {
						double lastPoint = f.x(f.getNbPoints() - 1);

						if (v != lastPoint) {
							f.addPoint(x, v);

							f.retainsOnlyLastPoints(getNbPointsInSlidingWindow(row, property));
						}
					}
				}
			}
		}

		table.repaint();

		if (propertiesPlottersPanel.isVisible()) {
			for (SwingPlotter p : property_plotter.values()) {
				p.repaint();
			}
		}

		if (unitsPlottersPanel.isVisible()) {
			for (SwingPlotter p : unit_plotter.values()) {
				p.repaint();
			}
		}
	}

	protected int getNbPointsInSlidingWindow(E row, Property<E> p) {
		return 20;
	}

	private class RefreshThread extends Thread {
		public boolean shouldStop;

		@Override
		public void run() {
			setName("Multiscope refresh");

			while (!shouldStop) {
				newStep();
				Threads.sleepMs(refreshPeriodMs);
			}
		}
	}

	public void updateVisiblePlotPanels() {
		if (propertiesPlottersPanel.isVisible()) {
			propertiesPlottersPanel.setPlotters(getPropertiesPlottersToShow());
			propertiesPlottersPanel.doLayout();
		}

		if (unitsPlottersPanel.isVisible()) {
			unitsPlottersPanel.setPlotters(getUnitsPlottersToShow());
			unitsPlottersPanel.doLayout();
		}
	}

	private Set<SwingPlotter> getPropertiesPlottersToShow() {
		Set<SwingPlotter> plottersToShow = new HashSet<>();

		for (int columnindex : table.getSelectedColumns()) {
			if (columnindex > 0) {
				Property<E> property = displayedProperties.get(columnindex - 1);
				SwingPlotter plotter = property_plotter.get(property);
				plottersToShow.add(plotter);
				plotter.getPlot().removeAllFigures();

				for (int rowindex : table.getSelectedRows()) {
					E row = rows.get(rowindex);
					Map<Property<E>, Figure> property_figure = row_figures.get(row);
					Figure propertyFigure = property_figure.get(property);
					propertyFigure.setName(getRowNameFor(row));

					FigureRenderer sr = getSpecificRenderer(row, property);

					if (sr != null) {
						propertyFigure.rendererList.clear();
						propertyFigure.addRenderer(sr);
					}

					propertyFigure
							.setColor(palette.getColor(plotter.getPlot().getNbFigures() % palette.getNumberOfColors()));
					plotter.getPlot().addFigure(propertyFigure);
				}
			}
		}
		return plottersToShow;

	}

	protected abstract FigureRenderer getSpecificRenderer(E row, Property<E> property);

	private Set<SwingPlotter> getUnitsPlottersToShow() {
		Set<SwingPlotter> plottersToShow = new HashSet<>();

		for (PanePlotter plotter : unit_plotter.values()) {
			plotter.getPlot().removeAllFigures();
		}

		for (int columnindex : table.getSelectedColumns()) {
			if (columnindex > 0) {
				Property<E> property = displayedProperties.get(columnindex - 1);

				SwingPlotter plotter = unit_plotter.get(property.getUnit());
				plottersToShow.add(plotter);

				for (int r : table.getSelectedRows()) {
					E n = rows.get(r);
					Map<Property<E>, Figure> property_figure = row_figures.get(n);
					Figure f = property_figure.get(property);
					f.setColor(palette.getColor(plotter.getPlot().getNbFigures() % palette.getNumberOfColors()));
					f.setName(getRowNameFor(n) + ":" + property.getHumanReadableName());
					plotter.getPlot().addFigure(f);
				}
			}
		}

		return plottersToShow;
	}

	public List<E> getRows() {
		return rows;
	}
}
