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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;

import javax.swing.JComponent;

import toools.thread.Threads;

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

public class SwingPlotter extends JComponent
{
	// the image used as buffer for fast repaints
	private boolean redrawNeeded = true;
	private boolean imageBufferedUsed = false;
	private Image imageCache;

	protected Plot plot = new Plot();

	private final List<SwingPlotterListener> listeners = new ArrayList<SwingPlotterListener>();

	public SwingPlotter()
	{
		plot.getSpace().setImageObserver(this);
		plot.getSpace().setBackgroundColor(Color.white);
		setDoubleBuffered(false);
		super.setBorder(null);
	}

	@Override
	public Dimension getPreferredSize()
	{
		Space s = getPlot().getSpace();
		int h = s.getLegend().getFont().getSize()
				+ s.getXDimension().getLegend().getFont().getSize() + 800;
		int w = s.getYDimension().getLegend().getFont().getSize() + 800;
		return new Dimension(w, h);	}

	public Plot getPlot()
	{
		return plot;
	}

	public void setGraphics2DPlotter(Plot p)
	{
		if (p == null)
			throw new IllegalArgumentException("painter set to null");

		this.plot = p;
	}

	@Override
	public void setForeground(Color fg)
	{
		plot.getSpace().setColor(fg);
	}

	@Override
	public Color getForeground()
	{
		return plot.getSpace().getColor();
	}

	@Override
	public void setBackground(Color bg)
	{
		plot.getSpace().setBackgroundColor(bg);
	}

	@Override
	public Color getBackground()
	{
		return plot.getSpace().getBackgroundColor();
	}

	@Override
	synchronized public void paint(Graphics g)
	{
		java.awt.Dimension size = getSize();

		if (g.getClipBounds().getSize().equals(size))
		{
			listeners.forEach(l -> l.paintStarting(this));

			if (isImageBufferedUsed())
			{
				if (isUpdateNeeded())
				{
					setUpdateNeeded(false);
					imageCache = createImage(size.width, size.height);
					Graphics imageGraphics = imageCache.getGraphics();
					imageGraphics.setClip(0, 0, size.width, size.height);
					drawOnGraphics(imageGraphics);
				}

				g.drawImage(imageCache, 0, 0, Color.white, this);
			}
			else
			{
				drawOnGraphics(g);
			}

			listeners.forEach(l -> l.paintStarting(this));
		}
		else
		{
			repaint(0);
		}
	}

	private void drawOnGraphics(Graphics g)
	{
		plot.draw((Graphics2D) g);
	}

	public boolean isUpdateNeeded()
	{
		if (redrawNeeded)
		{
			return true;
		}
		else
		{
			// the first time, the image does not exist
			if (imageCache == null)
			{
				return true;
			}
			else
			{
				// if the size of the component has changed
				if (imageCache.getHeight(this) != getSize().height
						|| imageCache.getWidth(this) != getSize().width)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
	}

	/**
	 * Sets if the component has to be updated. The update is needed if the data
	 * has changed.
	 * 
	 * @param b
	 */
	public void setUpdateNeeded(boolean b)
	{
		redrawNeeded = b;
	}

	/**
	 * Gets if the image buffering is used. This allow very fast repaint. This
	 * is useful if the component if moved, hidden, made visible very
	 * frequently.
	 * 
	 * @return boolean
	 */
	public boolean isImageBufferedUsed()
	{
		return imageBufferedUsed;
	}

	/**
	 * Sets if the image buffering is used. This allow very fast repaint. This
	 * is useful if the component if moved, hidden, made visible very
	 * frequently.
	 * 
	 * @param imageBufferedUsed
	 *            The imageBufferedUsed to set
	 */
	public void setImageBufferedUsed(boolean imageBufferedUsed)
	{
		this.imageBufferedUsed = imageBufferedUsed;
	}

	/**
	 * Sets the task that is periodically invoked. The task is invoked and then
	 * the repaint process is called.
	 * 
	 * @param cyclicTask
	 */
	public void refreshEveryMs(long milliseconds, BooleanSupplier whileCondition)
	{
		Threads.newThread_loop_periodic(milliseconds, whileCondition, () -> {
			if (isVisible())
			{
				setUpdateNeeded(true);
				repaint();
			}
		});
	}

	public Collection<SwingPlotterListener> getListeners()
	{
		return listeners;
	}

	public void setPlot(Plot p)
	{
		this.plot = p;
	}
}
