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

import toools.thread.Threads;

class EvolvingObject
{
	static int n = 0;
	final int ID = n++;
	private double foo = 0;
	private double bar = 0;

	public EvolvingObject()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				setName(getClass().getName());
				
				while (true)
				{
					foo += Math.random() - 0.5;

					if ((bar += 10 * Math.random()) > 100)
					{
						bar = - bar;
					}

					Threads.sleepMs(100);
				}

			}
		}.start();
	}
}