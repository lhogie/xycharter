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
 * Created on Mar 23, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package xycharter.interactive;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;

import xycharter.Arrow;
import xycharter.Axis;
import xycharter.AxisLine;
import xycharter.Dimension;
import xycharter.Figure;
import xycharter.Graduation;
import xycharter.GraphicalElement;
import xycharter.Grid;
import xycharter.Legend;
import xycharter.Space;
import xycharter.SwingPlotter;
import xycharter.render.FigureRenderer;

public class ControlFrame extends JFrame implements TreeSelectionListener
{
	private JTree tree = new JTree();
	private SwingPlotter plotter;
	private JPanel editorContainer = new JPanel(new FlowLayout());
	private Map<Class<?>, Editor> editorMap = new HashMap<>();

	public ControlFrame(SwingPlotter plotter)
	{
		super("Control panel");
		this.plotter = plotter;
		editorMap.put(GraphicalElement.class, new GraphicalElementEditor());
		editorMap.put(Arrow.class, new ArrowEditor());
		editorMap.put(Legend.class, new LegendEditor());

		setSize(plotter.getSize().width, plotter.getSize().height);
		setLocation(plotter.getLocationOnScreen().x + plotter.getSize().width,
				plotter.getLocationOnScreen().y);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(BorderLayout.WEST, new JScrollPane(tree));

		contentPane.add(BorderLayout.CENTER, editorContainer);

		tree.getSelectionModel()
				.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellRenderer(new UpTreeCellRenderer());
		tree.setBorder(new EtchedBorder());
		tree.addTreeSelectionListener(this);

		for (int i = 0; i < tree.getRowCount(); ++i)
		{
			tree.expandRow(i);
		}

		setVisible(true);
	}



	private void buildTree(DefaultMutableTreeNode root, FigureRenderer r)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(r);
		root.add(node);
	}
	
	private void buildTree(DefaultMutableTreeNode root, Space space)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(space);
		root.add(node);
		buildTree(node, space.getXDimension());
		buildTree(node, space.getYDimension());
		buildTree(node, space.getLegend());
	}

	private void buildTree(DefaultMutableTreeNode root, Legend legend)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(legend);
		root.add(node);
	}

	private void buildTree(DefaultMutableTreeNode root, Dimension dimension)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(dimension);
		root.add(node);
		buildTree(node, dimension.getLowerBoundAxis());
		buildTree(node, dimension.getOriginAxis());
		buildTree(node, dimension.getUpperBoundAxis());
		buildTree(node, dimension.getGrid());
		buildTree(node, dimension.getLegend());
	}

	private void buildTree(DefaultMutableTreeNode root, Grid grid)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(grid);
		root.add(node);
	}

	private void buildTree(DefaultMutableTreeNode root, Axis axis)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(axis);
		root.add(node);
		buildTree(node, axis.getLine());
		buildTree(node, axis.getGraduation());
	}

	private void buildTree(DefaultMutableTreeNode root, AxisLine axisLine)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(axisLine);
		root.add(node);
		buildTree(node, axisLine.getArrow());
	}

	private void buildTree(DefaultMutableTreeNode root, Graduation graduation)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(graduation);
		root.add(node);
	}

	private void buildTree(DefaultMutableTreeNode root, Arrow arrow)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(arrow);
		root.add(node);
	}

	public SwingPlotter getPlotter()
	{
		return plotter;
	}

	public void valueChanged(TreeSelectionEvent event)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath()
				.getLastPathComponent();
		Object object = node.getUserObject();

		editorContainer.removeAll();

		if (object instanceof String)
		{
			editorContainer.add(
					new JLabel(new ImageIcon(Object.class.getResource("/res/logo.png"))));
		}
		else
		{
			getEditors(object).forEach(e -> {
				e.setObject(object);
				editorContainer.add(e);
			});

			editorContainer.doLayout();
			editorContainer.validate();
		}
	}

	private List<Editor> getEditors(Object object)
	{
		List<Editor> r = new ArrayList<>();
		Class clazz = object.getClass();

		while (clazz != null)
		{
			Editor editor = editorMap.get(clazz);

			if (editor != null && ! r.contains(editor))
			{
				editor.setUpUI(this);
				r.add(editor);
			}

			clazz = clazz.getSuperclass();
		}

		return r;
	}

	private JFrame getPlotterFrame()
	{
		Component parent = plotter.getParent();

		while (true)
		{
			if (parent == null)
			{
				return null;
			}
			else if (parent instanceof JFrame)
			{
				return (JFrame) parent;
			}
			else
			{
				parent = parent.getParent();
			}
		}
	}

	private int getIndexOfComponent(Container container, Component component)
	{
		for (int i = 0; i < container.getComponentCount(); ++i)
		{
			if (container.getComponent(i) == component)
			{
				return i;
			}
		}

		throw new IllegalArgumentException("component is not in container");
	}
}
