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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author luc.hogie Created on Jun 4, 2004
 */
public class FilePlotter
{
	public static void plotFile(Plot p, File file, int width, int height)
			throws IOException
	{
		List<Integer> l = Arrays.asList(new Integer[] { 1, 2 });

		String name = file.getName();
		int pos = name.lastIndexOf('.');

		if (pos <= 0)
		{
			throw new IllegalArgumentException("file " + name + " has no extension");
		}
		else
		{
			String ext = name.substring(pos + 1).toLowerCase();
			byte[] data = getData(p, width, height, ext);
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(data);
			bos.flush();
			bos.close();
			fos.flush();
			fos.close();
		}
	}

	private static byte[] getData(Plot p, int width, int height, String fileExt)
	{
		if (fileExt.equalsIgnoreCase("jpg"))
		{
			return new JPEGPlotter().plot(p, width, height);
		}
		else if (fileExt.equalsIgnoreCase("png"))
		{
			return new PNGPlotter().plot(p, width, height);
		}
		else if (fileExt.equals("eps"))
		{
			return new EPSPlotter().plot(p, width, height);
		}
		else
		{
			throw new IllegalArgumentException("file extension not registered");
		}
	}

}
