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

import toools.io.file.Directory;
import toools.io.file.RegularFile;

/**
 * The user may want to get an image object or the data of an image file (PNG,
 * JPEG, SVG...) for, for instance, return it to a HTTP-client that will have to
 * draw the image on the web page it will show.
 * 
 * @author Luc Hogie
 */
public class GNUPlotFilePlotter
{
	public static void getGNUplotData(Figure f, Directory d, String name)
	{
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < f.getNbPoints(); ++i)
		{
			buf.append(f.x(i));
			buf.append('\t');
			buf.append(f.y(i));
			buf.append('\n');
		}

		buf.append('\n');
		new RegularFile(d, name + ".dat").setContentAsASCII(buf.toString());
	}
}
