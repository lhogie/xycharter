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

/*
 * Created on May 1, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package oscilloscup.render;

import java.awt.Color;

import oscilloscup.Figure;
import oscilloscup.Space;
import oscilloscup.Utilities;

/**
 * @author luc.hogie
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CamembertFigureRenderer extends FigureRenderer
{

	protected void drawImpl(Figure figure, Space space)
	{
		// if this figure is a camembert
		if (figure.getNbFigures() > 0 && figure.getNbPoints() == 0)
		{
			double sum = getSum(figure);
			double startAngle = 0;

			int radiusInPixel = Math.min(
					space.getXDimension().getGraphicsSize(space.getFigureGraphics()),
					space.getYDimension().getGraphicsSize(space.getFigureGraphics()));

			int x = space.getXDimension().convertToGraphicsCoordonateSystem( - 1);
			int y = space.getYDimension().convertToGraphicsCoordonateSystem(1);
			int w = (int) (2
					/ space.getXDimension().getLogicalIntervalRepresentedByOnePixel());
			int h = (int) (2
					/ space.getYDimension().getLogicalIntervalRepresentedByOnePixel());
			space.getFigureGraphics().drawArc(x, y, w, h, 0, 360);

			for (int i = 0; i < figure.getNbFigures(); ++i)
			{
				Figure f = figure.getFigureAt(i);
				double rate = f.getNbPoints() / sum;
				// int angle = (int) (2 * Math.PI * rate);
				double angle = (int) (360 * rate);
				// space.getFigureGraphics().setColor()
				System.out.println("drawing arc " + angle);

				Color color = (Color) Utilities.colorList.get(i);
				space.getFigureGraphics().setColor(color);
				space.getFigureGraphics().fillArc(x, y, w, h, (int) startAngle,
						(int) angle);
				startAngle += angle;

				color = (Color) Utilities.colorList.get(i);
				space.getFigureGraphics().setColor(color);
				space.getFigureGraphics().drawArc(x, y, w, h, (int) startAngle,
						(int) angle);

				int x1 = space.getXDimension().convertToGraphicsCoordonateSystem(0);
				int y1 = space.getYDimension().convertToGraphicsCoordonateSystem(0);
				int x2 = space.getXDimension().convertToGraphicsCoordonateSystem(
						Math.cos(Math.PI * startAngle / 180));
				int y2 = space.getYDimension().convertToGraphicsCoordonateSystem(
						Math.sin(Math.PI * startAngle / 180));
				space.getFigureGraphics().drawLine(x1, y1, x2, y2);

			}

		}
	}

	private double getSum(Figure figure)
	{
		double sum = 0;

		for (int i = 0; i < figure.getNbFigures(); ++i)
		{
			sum += figure.getFigureAt(i).getNbPoints();
		}

		return sum;
	}

	@Override
	public String getPublicName()
	{
		return "pie-chart";
	}


}
