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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

class SPTable<E> extends JTable
{
	private Color bgColor = Color.white;
	private Color aColor = new Color(240, 240, 240);

	private final TableCellRenderer cellRenderer = new DefaultTableCellRenderer()
	{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object object,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			// column 0 is the name column, it can't be selected
			if (column == 0)
			{
				Component r = super.getTableCellRendererComponent(table, object, false,
						hasFocus, row, column);
				r.setBackground(bgColor);
				return r;
			}
			else
			{
				Property<E> property = properties.get(column - 1);
				String value = property.getFormattedValue((E) object);
				Component r = super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);

				if ( ! isSelected)
				{
					if ((column + row) % 2 == 0)
					{
						r.setBackground(aColor);
					}
					else
					{
						r.setBackground(bgColor);
					}
				}

				return r;
			}
		}
	};

	private Palette colorPalette;
	private final List<Property<E>> properties;

	public SPTable(List<Property<E>> props, ListSelectionListener tableSelectionHandler)
	{
		this.properties = props;
		getSelectionModel().addListSelectionListener(tableSelectionHandler);
		getColumnModel().getSelectionModel()
				.addListSelectionListener(tableSelectionHandler);

		setAutoCreateRowSorter(true);
		setCellSelectionEnabled(true);
		setRowSelectionAllowed(true);
		setColumnSelectionAllowed(true);
	}

	public void setModel(List<E> rows)
	{
		setModel(new DefaultTableModel()
		{

			@Override
			public int getColumnCount()
			{
				// adds the name column
				return properties.size() + 1;
			}

			@Override
			public String getColumnName(int index)
			{
				return index == 0 ? "name"
						: properties.get(index - 1).getHumanReadableNameAndUnit();
			}

			@Override
			public boolean isCellEditable(int row, int col)
			{
				return false;
			}

			@Override
			public int getRowCount()
			{
				return rows.size();
			}
		});
	}

	public Palette getColorPalette()
	{
		return colorPalette;
	}

	public void setColorPalette(Palette colorPalette)
	{
		this.colorPalette = colorPalette;
	}

	@Override
	public Dimension getSize()
	{
		return new Dimension(getParent().getSize().width, 300);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		return cellRenderer;
	}

	JScrollPane createScrollPane()
	{
		return new JScrollPane(this)
		{

			@Override
			public Dimension getPreferredSize()
			{
				Dimension tps = SPTable.this.getPreferredSize();
				return new Dimension(tps.width, tps.height + 25);
			}
		};
	}
}
