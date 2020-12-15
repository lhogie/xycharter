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

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xycharter.Arrow;

/**
 * @author luc.hogie
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ArrowEditor extends Editor<Arrow> {
	private JSlider widthSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 10);
	private JSlider lenghtSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 10);

	public ArrowEditor() {
		{
			widthSlider.setMajorTickSpacing(10);
			widthSlider.setMinorTickSpacing(1);
			widthSlider.setPaintLabels(true);
			widthSlider.setPaintTicks(true);
			widthSlider.setPaintTrack(true);
		}

		{
			lenghtSlider.setMajorTickSpacing(10);
			lenghtSlider.setMinorTickSpacing(1);
			lenghtSlider.setPaintLabels(true);
			lenghtSlider.setPaintTicks(true);
			lenghtSlider.setPaintTrack(true);
		}

		setLayout(new GridLayout(2, 2));
		add(new JLabel("Width"));
		add(widthSlider);
		add(new JLabel("Lenght"));
		add(lenghtSlider);

		widthSlider.addChangeListener(e -> {
			getObject().setWidth(widthSlider.getValue());
			updatePlotter();
		});

		lenghtSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				((Arrow) getObject()).setLength(lenghtSlider.getValue());
				updatePlotter();
			}
		});
	}

	@Override
	public void setObject(Arrow a) {
		super.setObject(a);
		widthSlider.setValue(a.getWidth());
	}

	@Override
	public String toString() {
		return "Arrow";
	}
}
