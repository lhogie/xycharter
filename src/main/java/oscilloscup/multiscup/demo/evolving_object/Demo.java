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

package oscilloscup.multiscup.demo.evolving_object;

import java.util.Arrays;

import javax.swing.JFrame;

import oscilloscup.multiscup.Multiscope;
import oscilloscup.multiscup.Property;
import oscilloscup.multiscup.PropertyValueFormatter;
import oscilloscup.render.BooleansFigureRenderer;
import oscilloscup.render.FigureRenderer;
import toools.gui.Utilities;

public class Demo
{
	public static void main(String[] args)
	{
		Property<EvolvingObject> multi = new Property<EvolvingObject>()
		{
			@Override
			public Object getRawValue(EvolvingObject target)
			{
				return target.hashCode() * target.ID;
			}
		};

		Property<EvolvingObject> ds = new Property<EvolvingObject>()
		{
			@Override
			public Object getRawValue(EvolvingObject target)
			{
				return 3;
			}
		};

		Property<EvolvingObject> boolprop = new Property<EvolvingObject>()
		{
			@Override
			public Object getRawValue(EvolvingObject target)
			{
				if (target.ID == 1)
				{
					return Math.random() < 0.5 ? true : false;
				}
				else
				{
					return (int) (Math.random() * 10);
				}

			}
		};
		boolprop.setName("int or bool");

		Property<EvolvingObject> foo = Property.getProperty(EvolvingObject.class, "foo");
		foo.setName("foo");
		foo.setReformatter(new PropertyValueFormatter.PrettyDecimalsFormatter(2));
		foo.setPlotBounds(0d, null);
		Property<EvolvingObject> bar = Property.getProperty(EvolvingObject.class, "bar");
		bar.setPlotBounds(null, 0d);

		Multiscope<EvolvingObject> c = new Multiscope<EvolvingObject>(
				Arrays.asList(foo, bar, multi, ds, boolprop))
		{

			@Override
			protected String getRowNameFor(EvolvingObject e)
			{
				return String.valueOf(e.ID);
			}

			@Override
			protected int getNbPointsInSlidingWindow(EvolvingObject row,
					Property<EvolvingObject> p)
			{
				return 20;
			}

			@Override
			protected FigureRenderer getSpecificRenderer(EvolvingObject row,
					Property<EvolvingObject> property)
			{
				if (row.ID == 1)
					return new BooleansFigureRenderer();

				return null;
			}
		};

		c.setRows(Arrays.asList(new EvolvingObject(), new EvolvingObject(),
				new EvolvingObject(), new EvolvingObject()));
		c.setRefreshPeriodMs(1000);
		Utilities.displayInJFrame(c, "Demo");
	}
}
