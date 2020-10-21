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

/**
 * @author Luc Hogie
 * 
 *         <p>
 *         A GraphicalElement is a object that will be painted on the user
 *         graphics. All painted objects are not GraphicalElements. Only objects
 *         that are used to paint the space (everything but not the user data)
 *         are GraphicalElements.
 *         </p>
 * 
 *         <p>
 *         GraphicalElement is a recursive structure. This allow the inheritance
 *         of properties such as color, visibility...
 *         </p>
 * 
 *         <p>
 *         The root GraphicalElement is the space itself. Its direct children
 *         are the 2 dimensions (X and Y) which also have some children (the 3
 *         axis...)
 *         </p>
 */
public class GraphicalElement
{
	private boolean visible = true;
	private Color color = null;
	private GraphicalElement parent = null;

	/**
	 * @returns the parent graphical element of this object. May be null if this
	 *          object is the root element of the tree (if it's a Space object).
	 */
	public GraphicalElement getParent()
	{
		return parent;
	}

	/**
	 * Sets the parent for this GraphicalElement. It can be set to null.
	 */
	public void setParent(GraphicalElement parent)
	{
		this.parent = parent;
	}

	/**
	 * Sets if the graphical element is visible or not. If the parent graphical
	 * element is not visible, this object will not be visible: the visible
	 * property is inherited.
	 */
	public boolean isVisible()
	{
		// if has an invisible parent
		if (parent != null && ! parent.isVisible())
		{
			return false;
		}
		else
		{
			return visible;
		}
	}

	/**
	 * Sets the visibility for this object.
	 */
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	/**
	 * @return the color of the graphical element. If the color has never be
	 *         set, the color of the parent graphical element is returned. If
	 *         there is no parent, the default color is black. So, the returned
	 *         color cannot be null. The color property is inherited.
	 */
	public Color getColor()
	{
		if (color == null)
		{
			if (parent == null)
			{
				return Color.black;
			}
			else
			{
				return parent.getColor();
			}
		}
		else
		{
			return color;
		}
	}

	/**
	 * Sets the color for this object. If it is set to null, the color of the
	 * parent graphial element will be used. If there is no parent defined, the
	 * color will be set to the default color black.
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}
}