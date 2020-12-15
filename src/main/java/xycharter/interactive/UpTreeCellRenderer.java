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

package xycharter.interactive;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class UpTreeCellRenderer extends DefaultTreeCellRenderer
{
	static Map iconMap;

	public UpTreeCellRenderer()
	{
		if (iconMap == null)
		{
			iconMap = new HashMap();
			/*
			 * iconMap.put(Arrow.class, new
			 * ImageIcon(Object.class.getResource("/res/arrow.gif")));
			 * iconMap.put(Space.class, new
			 * ImageIcon(Object.class.getResource("/res/aim.gif")));
			 * iconMap.put(Dimension.class, new
			 * ImageIcon(Object.class.getResource("/res/redBall.gif")));
			 * iconMap.put(AxisLine.class, new
			 * ImageIcon(Object.class.getResource("/res/greenBall.gif")));
			 * iconMap.put(Axis.class, new
			 * ImageIcon(Object.class.getResource("/res/blueBall.gif")));
			 * iconMap.put(Graduation.class, new
			 * ImageIcon(Object.class.getResource("/res/blueDiamond.gif")));
			 * iconMap.put(Legend.class, new
			 * ImageIcon(Object.class.getResource("/res/spiral.gif")));
			 */
		}
	}

	public Component getTreeCellRendererComponent(JTree tree, Object node,
			boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		Object object = ((DefaultMutableTreeNode) node).getUserObject();
		Icon icon = (Icon) iconMap.get(object.getClass());
		JLabel component = (JLabel) super.getTreeCellRendererComponent(tree, node,
				selected, expanded, leaf, row, hasFocus);
		component.setIcon(icon);

		return component;
	}

}
