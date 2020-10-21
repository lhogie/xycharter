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
 
 
/*
 * Created on Mar 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package xycharter;



import java.awt.Color;
import java.util.List;
import java.util.Map;

import toools.math.Distribution;
import xycharter.render.ConnectedLineFigureRenderer;
import xycharter.render.HistogramPointRenderer;



/**
 * @author luc.hogie
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FigureFactory
{
	public static Figure createFigure(double[] data)
	{
		Figure figure = new Figure();
		
		for (int i = 0; i < data.length; ++i)
		{
			figure.addPoint(i, data[i]);
		}

		return figure;
	}

	public static <A extends Number, B extends Number> Figure createFigure(Map<A , B> map)
	{
		Figure figure = new Figure();

		for (Map.Entry<A, B> entry : map.entrySet())
		{
			double x = entry.getKey().doubleValue();
			double y = entry.getValue().doubleValue();
			figure.addPoint(x, y);
		}

		return figure;
	}

//	public static Figure createFigure(List<Number> x, List<Number> y)
	public static Figure createFigure(List<? extends Number> xList, List<? extends Number> yList)
	{
		if (xList.size() != yList.size())
			throw new IllegalArgumentException("the two lists do not have the same size");
		
		Figure figure = new Figure();
		int size = xList.size();

		for (int i = 0; i < size; ++i)
		{
			Number xi = xList.get(i);
			Number yi = yList.get(i);
			
			if (xi != null && yi != null)
			{
				double x = xi.doubleValue();
				double y = yi.doubleValue();
				figure.addPoint(x, y);
			}
		}		

		figure.rendererList.add(new ConnectedLineFigureRenderer());
		return figure;
	}
	
	public  static <T extends Number> Figure createFigure(Distribution<T> distribution)
	{
	    Figure figure = new Figure();
	    
	    for (T t : distribution.getOccuringObjects())
	    {
	        figure.addPoint(t.doubleValue(), distribution.getRelativeNumberOfOccurences(t));	        
	    }

	    figure.rendererList.add(new HistogramPointRenderer((x, y) -> Color.blue));
	    return figure;
	}
}
