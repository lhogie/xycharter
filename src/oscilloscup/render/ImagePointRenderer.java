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

package oscilloscup.render;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

import oscilloscup.Dimension;
import oscilloscup.Figure;
import oscilloscup.Space;

/**
 * @author Luc Hogie
 */
public class ImagePointRenderer extends FigureRenderer
{
	private Image image;

	@Override
	public void drawImpl(Figure f, Space space)
	{
		ImageObserver imageObserver = space.getImageObserver();

		if (image != null && imageObserver != null)
		{
			for (int i = 0; i < f.getNbPoints(); ++i)
			{
				Dimension xDimension = space.getXDimension();
				Dimension yDimension = space.getYDimension();

				int px = xDimension.convertToGraphicsCoordonateSystem(f.x(i));
				int py = yDimension.convertToGraphicsCoordonateSystem(f.y(i));
				int x = px - image.getWidth(imageObserver) / 2 + getXShift();
				int y = py - image.getHeight(imageObserver) / 2 + getYShift();
				space.getFigureGraphics().drawImage(image, x, y, imageObserver);
			}
		}
	}

	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}

	public void setImage(URL url)
	{
		setImage(Toolkit.getDefaultToolkit().createImage(url));
	}

	@Override
	public String getPublicName()
	{
		return "image";
	}

}
