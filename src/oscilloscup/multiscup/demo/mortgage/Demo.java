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

package oscilloscup.multiscup.demo.mortgage;

import java.util.ArrayList;
import java.util.List;

import oscilloscup.multiscup.Clock;
import oscilloscup.multiscup.Multiscope;
import oscilloscup.multiscup.Property;
import oscilloscup.render.FigureRenderer;
import toools.gui.Utilities;

public class Demo
{
	static int mois = 0;

	public static void main(String[] args)
	{
		Clock clock = new Clock()
		{
			@Override
			public double getTime()
			{
				return mois;
			}

			@Override
			public String getTimeUnit()
			{
				return "month";
			}
		};

		List<Property<BankSimulation>> props = Property
				.findAllProperties(BankSimulation.class);
		System.out.println(Property.getNames(props));
		Property.removeProperty(props, "prixDuBien");
		Property.findPropertyByName(props, "enBanque").setUnit("€");
		Property.findPropertyByName(props, "enBanque").setClock(clock);
		Property.findPropertyByName(props, "resteARembourser").setUnit("€");
		Property.findPropertyByName(props, "apport").setUnit("€");
		Property.findPropertyByName(props, "mensualite").setUnit("€");
		Property.findPropertyByName(props, "tauxEpargne").setUnit("%");
		Property.findPropertyByName(props, "gainEpargneTotal").setUnit("€");

		List<BankSimulation> simulations = new ArrayList<>();
		simulations.add(new BankSimulation("BNP", 129000, 1170, 900, 527));
		simulations.add(new BankSimulation("Banque postale", 123000, 896, 0, 800));
		simulations.add(new BankSimulation("caisse d'épargne", 123000, 921, 300, 0));

		Multiscope<BankSimulation> pane = new Multiscope<BankSimulation>(props)
		{
			@Override
			protected String getRowNameFor(BankSimulation e)
			{
				return e.name;
			}

			@Override
			protected int getNbPointsInSlidingWindow(BankSimulation row,
					Property<BankSimulation> p)
			{
				return 20000;
			}

			@Override
			protected FigureRenderer getSpecificRenderer(BankSimulation row,
					Property<BankSimulation> property)
			{
				return null;
			}

		};

		pane.setRows(simulations);

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{

					if (mois == 86)
						break;

					// Threads.sleepMs(100);

					for (BankSimulation s : simulations)
					{
						s.change(clock.getTime());
					}

					pane.newStep();
					++mois;
				}

				System.out.println("done");
			}
		}).start();

		Utilities.displayInJFrame(pane, "Demo");
	}
}
