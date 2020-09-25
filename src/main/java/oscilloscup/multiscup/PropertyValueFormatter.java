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

public interface PropertyValueFormatter
{

	String format(Object rawValue);

	public static final PropertyValueFormatter noFormatting = new PropertyValueFormatter()
	{
		@Override
		public String format(Object rawValue)
		{
			return rawValue.toString();
		}
	};

	public static class PrettyDecimalsFormatter implements PropertyValueFormatter
	{
		private final int nbDecimals;

		public PrettyDecimalsFormatter(int nbDecimals)
		{
			this.nbDecimals = nbDecimals;
		}

		@Override
		public String format(Object rawValue)
		{
			if (rawValue instanceof Number)
			{
				double d = ((Number) rawValue).doubleValue();
				double factor = Math.pow(10, nbDecimals);
				d = ((int) (d * factor)) / factor;

				if (d == (int) d)
				{
					return String.valueOf((int) d);
				}
				else
				{
					return String.valueOf(d);
				}
			}
			else
			{
				return rawValue.toString();
			}
		}

	}

	public static class Kilos implements PropertyValueFormatter
	{
		@Override
		public String format(Object rawValue)
		{
			double d = ((Number) rawValue).doubleValue();
			return String.valueOf(d / 1000);
		}
	}
}
