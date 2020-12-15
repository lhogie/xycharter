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

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xycharter.GraphicalElement;

public class GraphicalElementEditor extends Editor<GraphicalElement> {
	private JCheckBox visibleCheckbox = new JCheckBox("element is visible");
	private JCheckBox specificColor = new JCheckBox("element has a specific color", true);
	private JColorChooser colorChooser = new JColorChooser();

	public GraphicalElementEditor() {
		colorChooser.setChooserPanels(new AbstractColorChooserPanel[] { colorChooser.getChooserPanels()[2] });
		colorChooser.setPreviewPanel(new JLabel());
		setLayout(new GridLayout(3, 1));
		add(visibleCheckbox);
		add(specificColor);
		add(colorChooser);

		visibleCheckbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				getObject().setVisible(visibleCheckbox.isSelected());
				updatePlotter();
			}
		});

		specificColor.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				GraphicalElement ge = (GraphicalElement) getObject();

				if (specificColor.isSelected()) {
					if (ge.getParent() == null) {
						ge.setColor(Color.black);
					} else {
						ge.setColor(ge.getParent().getColor());
					}

					colorChooser.setColor(ge.getColor());
					colorChooser.setVisible(true);
				} else {
					ge.setColor(null);
					colorChooser.setVisible(false);
				}

				updatePlotter();
			}
		});

		colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				((GraphicalElement) getObject()).setColor(colorChooser.getColor());
				updatePlotter();
			}
		});
	}

	public void setObject(GraphicalElement e) {
		super.setObject(e);
		visibleCheckbox.setSelected(e.isVisible());
		colorChooser.setColor(e.getColor());
	}

	public String toString() {
		return "Basic";
	}

}
