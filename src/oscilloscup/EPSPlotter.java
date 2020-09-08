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

package oscilloscup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jibble.epsgraphics.EpsGraphics2D;

/**
 * The user may want to get an image object or the data of an image file (PNG,
 * JPEG, SVG...) for, for instance, return it to a HTTP-client that will have to
 * draw the image on the web page it will show.
 * 
 * @author Luc Hogie
 */
public class EPSPlotter extends Plot2Bytes
{
	@Override
	public byte[] plot(Plot plot, int width, int height)
	{
		try
		{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			EpsGraphics2D g = new EpsGraphics2D(null, outputStream, 0, 0, width, height);
			g.setClip(0, 0, width, height);
			plot.draw(g);
			g.flush();
			g.close();
			outputStream.close();
			return outputStream.toByteArray();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			throw new IllegalStateException("bug");
		}
	}

}
