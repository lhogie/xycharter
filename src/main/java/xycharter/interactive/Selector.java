package xycharter.interactive;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import xycharter.SelectionListener;

public abstract class Selector implements MouseMotionListener {
	public List<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();
	protected final Selection selection = new Selection();
	Color color = Color.gray;
	final InteractiveSwingPlotter plotter;

	Selector(InteractiveSwingPlotter plotter) {
		this.plotter = plotter;
	}

	public abstract void paint(Graphics g);

	protected abstract void click(double x, double y);
}
