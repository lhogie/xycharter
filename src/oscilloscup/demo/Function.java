package oscilloscup.demo;

import oscilloscup.Figure;
import oscilloscup.render.ConnectedLineFigureRenderer;
import oscilloscup.render.FigureRenderer;

public interface Function
{
	double f(double x);

	default Figure toFigure(double start, double end, double step)
	{
		Figure f = new Figure();
		f.setName("y = f(x)");

		FigureRenderer r = new ConnectedLineFigureRenderer();
		f.addRenderer(r);

		for (double t = start; t < end; t += step)
		{
			f.addPoint(t, f(t));
		}

		return f;
	}
}
