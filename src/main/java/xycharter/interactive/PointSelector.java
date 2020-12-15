package xycharter.interactive;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import xycharter.Figure;
import xycharter.Figure.ClosestPoint;

public class PointSelector extends Selector {
	int edgeLen = 20;

	PointSelector(InteractiveSwingPlotter plotter) {
		super(plotter);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(color);

		for (OnFigureSelection s : selection) {
			for (int i : s.pointIndices) {
				double x = s.figure.x(i);
				double y = s.figure.y(i);
				int x1 = plotter.getPlot().getSpace().getXDimension().convertToGraphicsCoordonateSystem(x);
				int y1 = plotter.getPlot().getSpace().getYDimension().convertToGraphicsCoordonateSystem(y);
				x1 -= edgeLen / 2;
				y1 -= edgeLen / 2;
				int x2 = x1 + edgeLen;
				int y2 = y1 + edgeLen;
				color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 64);
				g.setColor(color);
				g.fillRect(x1, y1, x2 - x1, y2 - y1);
				g.setColor(Color.black);
				g.drawRect(x1, y1, x2 - x1, y2 - y1);
				g.drawLine(x1, y1, x2, y2);
				g.drawLine(x1, y2, x2, y1);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	protected void click(double x, double y) {
		selection.clear();
		ClosestPoint closestPoint = Figure.closestPoint(x, y, plotter.getPlot().figures());
		OnFigureSelection s = new OnFigureSelection();
		s.figure = closestPoint.figure;
		s.pointIndices.add(closestPoint.index);
		selection.add(s);
		plotter.repaint(0);
		selectionListeners.forEach(l -> l.pointsSelected(plotter, selection));
	}
}
