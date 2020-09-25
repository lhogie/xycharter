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
 
 


package oscilloscup.multiscup;

import java.awt.Color;

import toools.gui.ListColorPalette;

public class Palette extends ListColorPalette
{
	public static Palette defaultPalette = new Palette();

	int counter = 0;

	public Palette()
	{
		getColorList().add(Color.blue);
		getColorList().add(Color.red);
		getColorList().add(Color.green);
		getColorList().add(Color.pink);
		getColorList().add(Color.black);
		getColorList().add(Color.cyan);
		getColorList().add(Color.magenta);
		getColorList().add(Color.gray);
		getColorList().add(Color.yellow);
	}

	public void reset()
	{
		counter = 0;
	}

	public Color getNextColor()
	{
		return getColor(counter++ % getNumberOfColors());
	}

}
