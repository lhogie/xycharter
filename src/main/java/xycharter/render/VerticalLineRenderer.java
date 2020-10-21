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

import xycharter.Dimension;
import xycharter.Figure;
import xycharter.Space;

public class VerticalLineRenderer extends FigureRenderer
{
	@Override
	public void drawImpl(Figure f, Space space)
	{
		Dimension xDimension = space.getXDimension();

		for (int i = 0; i < f.getNbPoints(); ++i)
		{
			int x = xDimension.convertToGraphicsCoordonateSystem(f.x(i));
			space.getFigureGraphics().drawLine(x, 0, x, 250);
		}
	}

	@Override
	public String getPublicName()
	{
		return "vertical line";
	}

}