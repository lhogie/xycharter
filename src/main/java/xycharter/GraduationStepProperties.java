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

import java.awt.*;

import toools.math.MathsUtilities;
import toools.text.TextUtilities;

/**
 * @author Luc Hogie
 * 
 *         GraduationStepProperties defines the value of the step, its color,
 *         its font and its angle at each step of a graduation. This is a
 *         default implementation and users may derive it.
 * 
 *         Why subclassing it? Because maybe you want to put some special text
 *         at determined steps, or particular color? You can set to graduation
 *         line where you want... And a lot more.
 */
public class GraduationStepProperties
{
	public String getTextAt(double step)
	{
		int intStep = (int) step;

		if (step == intStep)
		{
			return Integer.toString(intStep);
		}
		else
		{
			double r = MathsUtilities.round(step, 6);
			
			if (MathsUtilities.isInteger(r))
			{
				return String.valueOf(TextUtilities.toHumanString((int) r));
			}
			else
			{
				return String.valueOf(r);
			}
			
		}
	}

	public Color getLineColorAt(double step)
	{
		return null;
	}

	public Color getTextColorAt(double step)
	{
		return null;
	}

	public int getLineLengthAt(double step)
	{
		return 3;
	}

	/**
	 * This feature is not yet supported.
	 * 
	 * @param textAngle
	 */
	public int getTextAngleAt(double step)
	{
		return 0;
	}
}
