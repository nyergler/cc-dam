package org.cc.dam;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import java.util.*;

public class QueryView extends ViewPart {

	public static final String ID = "org.cc.dam.QueryView";
	private String[] tags = {"dc:author", "dc:title"};
	private Table queryTable;
	private Combo key;
	private Text value;
	private Button addBtn;
	private Button searchBtn;
	private Button clearBtn;
	

	public QueryView() {
		// TODO Auto-generated constructor stub
	}

	/***************************************************************************
	 * createPartControl creates a layout for the view, and creates / places the
	 * widgets into the view.
	 */
	public void createPartControl(Composite parent) {
		
		QueryController qc = new QueryController();
		GridData gd = new GridData(SWT.NONE);
	    gd.horizontalAlignment = GridData.FILL;
	    gd.grabExcessHorizontalSpace = true;
	      
		parent.setLayout(new GridLayout(1, true));

		// Window information Label
		Label queryLabel = new Label(parent, SWT.NONE);
		queryLabel.setText("Use the options below to search the database.");
		
		Group queryBuilder = new Group(parent, SWT.NONE);
		queryBuilder.setText("Query Builder");
		queryBuilder.setLayout(new GridLayout(1,true));
		queryBuilder.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL));
		
		queryTable = new Table(queryBuilder, SWT.NONE);
		queryTable.setHeaderVisible(true);
		queryTable.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL));

		TableColumn keyCol = new TableColumn(queryTable, SWT.LEFT);
		keyCol.setResizable(true);
		keyCol.setText("Key");

		TableColumn valueCol = new TableColumn(queryTable, SWT.LEFT);
		valueCol.setResizable(true);
		valueCol.setText("Value");

		keyCol.pack();
		valueCol.pack();

		Composite a = new Composite(queryBuilder, SWT.NONE);
		a.setLayout(new GridLayout(3, true));
		a.setLayoutData(gd);
		
		key = new Combo(a, SWT.BORDER);
		key.setLayoutData(gd);
		
		for(int i = 0; i < tags.length; i++){
			key.add(tags[i]);
		}
		
		
		value = new Text(a, SWT.BORDER);
		value.setLayoutData(gd);

		addBtn = new Button(a, SWT.None);
		addBtn.setText("Add");
		addBtn.addSelectionListener(qc);
		
		Composite panel = new Composite(parent,SWT.NONE);
		panel.setLayout(new GridLayout(2,true));
		//panel.setLayoutData(gd);
		  
		searchBtn = new Button(panel, SWT.NONE);
		searchBtn.setText("Search");
		searchBtn.addSelectionListener(qc);
	
		clearBtn = new Button(panel, SWT.NONE);
		clearBtn.setText("Clear");
	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private class QueryController implements SelectionListener {
		public void widgetSelected(SelectionEvent e){
			
			if (e.getSource() == addBtn){
				String[] data = {tags[key.getSelectionIndex()],value.getText()};
				TableItem a = new TableItem(queryTable,SWT.NONE);
				a.setText(data);
				
			}
			if (e.getSource() == searchBtn){
				System.out.println("hghg");
				HashMap query = new HashMap();
				TableItem[] items = queryTable.getItems();
				for(int i = 0; i < items.length; i++){
					query.put(items[i].getText(0), items[i].getText(1));
				}
				Controller.doQuery(query);
				
			}
			if (e.getSource() == clearBtn){
				queryTable.removeAll();
				Controller.refreshDatabaseView();
			}
	
			
			
		}
		public void widgetDefaultSelected(SelectionEvent e){
			
		}
	}

}
