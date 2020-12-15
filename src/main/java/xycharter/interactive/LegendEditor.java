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
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import xycharter.Legend;

public class LegendEditor extends Editor<Legend> {
	private JTextField textField = new JTextField(20);
	private JList fontList = new JList();

	public LegendEditor() {
		setLayout(new GridLayout(1, 1));
		add(new JLabel("Text:"));
		add(textField);

		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				((Legend) getObject()).setText(textField.getText());
				updatePlotter();
			}

			public void insertUpdate(DocumentEvent arg0) {
				((Legend) getObject()).setText(textField.getText());
				updatePlotter();
			}

			public void removeUpdate(DocumentEvent arg0) {
				((Legend) getObject()).setText(textField.getText());
				updatePlotter();
			}
		});

		fontList.setListData(Toolkit.getDefaultToolkit().getFontList());
	}

	public void setObject(Legend l) {
		super.setObject(l);
		textField.setText(l.getText());
	}

	public String toString() {
		return "Legend";
	}
}
