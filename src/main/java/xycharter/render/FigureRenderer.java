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

package xycharter.render;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import xycharter.Figure;
import xycharter.Space;

/**
 * @author Luc Hogie
 */
public abstract class FigureRenderer
{
	private Stroke stroke = new BasicStroke();
	private int xShift = 0;
	private int yShift = 0;

	public final void draw(Figure object, Space space)
	{
		Graphics2D graphics = space.getFigureGraphics();
		graphics.setColor(object.getColor());
		graphics.setStroke(getStroke());
		drawImpl(object, space);
	}

	protected abstract void drawImpl(Figure f, Space space);

	public Stroke getStroke()
	{
		return stroke;
	}

	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
	}

	public abstract String getPublicName();

	public int getXShift()
	{
		return xShift;
	}

	public int getYShift()
	{
		return yShift;
	}

	public void setXShift(int xShift)
	{
		this.xShift = xShift;
	}

	public void setYShift(int yShift)
	{
		this.yShift = yShift;
	}
	
	@Override
	public String toString()
	{
		return getPublicName();
	}
}
