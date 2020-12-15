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

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import xycharter.FilePlotter;
import xycharter.SwingPlotter;

/**
 * The SwingPlotter is a Swing component on which a graphical representation
 * will be plotted.
 * 
 * The SwingPlotter may be or may be not buffered. When it is, the full refresh
 * is slow but you can fastly move the widget, make it visible/unvisible. It
 * will be extremely fastly repainted. If the buffer is disabled, the graphical
 * representation is not stored and so it has to be recalculated for each
 * repaint process. This is useful when the content of the data change often and
 * the component has to be updated (eg: animations).
 * 
 * It has the ability to be periodically repainted: this allow the user to
 * program an animation. If the user want the widget to be repainted according
 * to a strict period, he has to define the <i>task</i>. The SwingPlotter
 * calculates the duration of the task and waits only the remaining time
 * (remaining = period - task duration) before repating the component. The
 * constrainst is that the task must execute faster than one period. If the user
 * do not want a strict period, he can do whatever on the figure and call
 * SwingPlotter.repaint(0) at any time.
 * 
 * @author Luc Hogie
 */

public class InteractiveSwingPlotter extends SwingPlotter {
	private JPopupMenu popup;
	private JMenuItem repaintMenuItem = new JMenuItem("Repaint");
	private JMenuItem saveMenuItem = new JMenuItem("Save as...");
	private JMenuItem controlPanelMenuItem = new JMenuItem("Control panel");

	private Selector selectionMode = new PointSelector(this);

	public InteractiveSwingPlotter() {
		popup = new JPopupMenu();
		popup.setInvoker(this);
		popup.add(repaintMenuItem);
		popup.add(saveMenuItem);
		popup.addSeparator();
		popup.add(controlPanelMenuItem);

		repaintMenuItem.addActionListener(e -> repaint(0));

		addMouseMotionListener(selectionMode);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (popup.isVisible()) {
					popup.setVisible(false);
				} else {
					// right button
					if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) {
						Point point = e.getPoint();
						point.x -= getPlot().figureGraphicsX;
						point.y -= getPlot().figureGraphicsY;
						double x = getPlot().getSpace().getXDimension()
								.convertToDimensionCoordonateSystem((int) point.getX());
						double y = getPlot().getSpace().getYDimension()
								.convertToDimensionCoordonateSystem((int) point.getY());
						selectionMode.click(x, y);
					}
					// left button only
					else if ((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0
							&& (e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == 0) {
						Point p = e.getPoint();
						SwingUtilities.convertPointToScreen(p, InteractiveSwingPlotter.this);
						popup.setLocation(p);
						popup.setVisible(true);
					}
				}
			}
		});

		saveMenuItem.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			FileFilter filter = new FileFilter() {
				@Override
				public boolean accept(File file) {
					if (file.isDirectory()) {
						return true;
					} else {
						String name = file.getName();
						int pos = name.lastIndexOf('.');

						if (pos <= 0) {
							return false;
						} else {
							String ext = name.substring(pos + 1).toLowerCase();
							return ext.equals("jpg") || ext.equals("png") || ext.equals("svg") || ext.equals("eps");
						}
					}
				}

				@Override
				public String getDescription() {
					return "JPEG (.jpg), PNG (.png), SVG (.svg), PostScript (.eps)";
				}
			};

			chooser.setFileFilter(filter);
			int returnVal = chooser.showSaveDialog(InteractiveSwingPlotter.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();

				try {
					java.awt.Dimension size = getSize();
					FilePlotter.plotFile(plot, file, size.width, size.height);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(InteractiveSwingPlotter.this, "I/O error while writing file", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (Exception ex) {
					if (ex.getMessage() == null || ex.getMessage().length() == 0) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(InteractiveSwingPlotter.this, ex.getClass().getName(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(InteractiveSwingPlotter.this, ex.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		controlPanelMenuItem.addActionListener(e -> new ControlFrame(InteractiveSwingPlotter.this));

	}

	public void paint(Graphics g) {
		super.paint(g);
		selectionMode.paint(g);
	}

	public Selector getSelectionMode() {
		return selectionMode;
	}

	public void setSelectionMode(Selector i) {
		selectionMode = i;
	}

}
