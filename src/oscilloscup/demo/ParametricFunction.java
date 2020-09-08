package oscilloscup.demo;

import oscilloscup.Figure;
import oscilloscup.render.ConnectedLineFigureRenderer;
import oscilloscup.render.FigureRenderer;

public interface ParametricFunction
{
	Point f(double x);

	default Figure toFigure(double start, double end, double step)
	{
		Figure f = new Figure();
		f.setName("(x, y) = f(t)");

		FigureRenderer r = new ConnectedLineFigureRenderer();
		f.addRenderer(r);

		for (double t = start; t < end; t += step)
		{
			Point p = f(t);
			f.addPoint(p.x, p.y);
		}

		return f;
	}

}
