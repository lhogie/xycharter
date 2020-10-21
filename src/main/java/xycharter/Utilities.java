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

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * @author luc
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Utilities
{
	/**
	 * Double precision arithmetics works too bad on integer values.
	 * This convert 1.0000000001 to 1
	public static double round( double a )
	{
		a *= 1000000000;
		a = java.lang.Math.round(a);
		a /= 1000000000;
    	return a;
	}
     */
	
	public static List colorList = new Vector(Arrays.asList(new Object[] {Color.red, Color.blue, Color.green, Color.yellow, Color.orange}));
}
