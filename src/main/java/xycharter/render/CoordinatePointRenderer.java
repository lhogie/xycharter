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

import xycharter.Figure;
import xycharter.Space;

/**
 * @author Luc Hogie
 *
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CoordinatePointRenderer extends TextPointRenderer
{
	public CoordinatePointRenderer()
	{
		super((x, y) -> "(" + x + ", " + y + ")");
	}

	@Override
	public void drawImpl(Figure f, Space space)
	{
		for (int i = 0; i < f.getNbPoints(); ++i)
		{
			setXShift(0);
			setYShift(10);
		}

		super.drawImpl(f, space);
	}

	@Override
	public String getPublicName()
	{
		return "coordinate";
	}

}
