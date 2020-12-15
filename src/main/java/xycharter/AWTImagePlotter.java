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

package xycharter;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The user may want to get an image object or the data of an image file (PNG,
 * JPEG, SVG...) for, for instance, return it to a HTTP-client that will have to
 * draw the image on the web page it will show.
 * 
 * @author Luc Hogie
 */
public class AWTImagePlotter {
	/**
	 * Creates and image with the given dimension.
	 */
	public static Image getAWTImage(Plot p, int width, int height) {		

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setClip(0, 0, width, height);
		p.draw(graphics);
		graphics.dispose();
		return image;
	}

	public static byte[] createImageData(Plot p, int width, int height, String type) {


		return createImageData((RenderedImage) getAWTImage(p, width, height), type);
	}

	public static byte[] createImageData(RenderedImage image, String type) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, type, os);
			os.close();
			image = null;
			return os.toByteArray();
		}
		catch (IOException ex) {
			throw new IllegalStateException("I/O error shouldn't have occured");
		}
	}

}
