package xycharter.demo;

import xycharter.Figure;
import xycharter.render.ConnectedLineFigureRenderer;

public interface ParametricFunction {
	Point f(double x);

	default Figure toFigure(double start, double end, double step) {
		Figure f = new Figure();
		f.setName("(x, y) = f(t)");

		f.addRenderer(new ConnectedLineFigureRenderer());

		for (double t = start; t < end; t += step) {
			Point p = f(t);
			f.addPoint(p.x, p.y);
		}

		return f;
	}

}
