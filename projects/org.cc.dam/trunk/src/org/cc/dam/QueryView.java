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
import java.io.*;


public class QueryView extends ViewPart {

	public static final String ID = "org.cc.dam.QueryView";
	private String[] tags = {"dc:author", "dc:title"};
	private Table queryTable;
	private Combo key;
	private Text value;
	private Button addBtn;
	private Button clearBtn;
	private Button saveBtn;
	private Button loadBtn;
	
	

	public QueryView() {
		// TODO Auto-generated constructor stub
	}

	/***************************************************************************
	 * createPartControl creates a layout for the view, and creates / places the
	 * widgets into the view.
	 */
	public void createPartControl(Composite parent) {
		
		// Controller for query actions
		QueryController qc = new QueryController();
		// gridata
		GridData gd = new GridData();
	    gd.horizontalAlignment = GridData.FILL;
	    gd.grabExcessHorizontalSpace = true;
	   
		parent.setLayout(new GridLayout(1,false));

		// Window information Label
		Label queryLabel = new Label(parent, SWT.NONE);
		queryLabel.setText("Use the options below to search the database.");
		
		// Query Builder
		Group queryBuilder = new Group(parent, SWT.NONE);
		queryBuilder.setText("Query Builder");
		queryBuilder.setLayout(new GridLayout(1,true));
		queryBuilder.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL));
		// Make the table
		queryTable = new Table(queryBuilder, SWT.NONE);
		queryTable.setHeaderVisible(true);
		queryTable.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL));
		
		// Key
		TableColumn keyCol = new TableColumn(queryTable, SWT.LEFT);
		keyCol.setResizable(true);
		keyCol.setText("Key");
		// Value
		TableColumn valueCol = new TableColumn(queryTable, SWT.LEFT);
		valueCol.setResizable(true);
		valueCol.setText("Value");

		keyCol.pack();
		valueCol.pack();
		queryTable.pack();
		
		// Controls
		Composite a = new Composite(queryBuilder, SWT.NONE);
		a.setLayout(new GridLayout(2, false));
		a.setLayoutData(gd);
		
		// not used yet
// Composite logic = new Composite(a,SWT.None);
// logic.setLayout(new GridLayout(2,false));
// Button andBtn = new Button(logic, SWT.RADIO);
// andBtn.setText("AND");
// Button orBtn = new Button(logic, SWT.RADIO);
// orBtn.setText("OR");
//		
// Label blank = new Label(a,SWT.None);
// blank.setText("");
		
		// Key combo box
		key = new Combo(a, SWT.BORDER);
		key.setItems(tags); 
		// Value text box
		value = new Text(a, SWT.BORDER);
		value.setLayoutData(gd);
		
		addBtn = new Button(a, SWT.None);
		addBtn.setText("Add Constraint");
		addBtn.addSelectionListener(qc);
	
		clearBtn = new Button(a, SWT.None);
		clearBtn.setText("Clear Constraints");
		clearBtn.addSelectionListener(qc);
		
		saveBtn = new Button(a,SWT.None);
		saveBtn.setText("save");
		saveBtn.addSelectionListener(qc);
		
		loadBtn = new Button(a,SWT.None);
		loadBtn.setText("load");
		loadBtn.addSelectionListener(qc);
		
	
	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private class QueryController implements SelectionListener {
		public void widgetSelected(SelectionEvent e){
			
			if (e.getSource() == addBtn){
				
				HashMap<String, String> query = new HashMap<String, String>();
				// Make TableItem from data
				String[] data = {tags[key.getSelectionIndex()],value.getText()};
				TableItem a = new TableItem(queryTable,SWT.NONE);
				a.setText(data);
				 for (int i = 0; i < queryTable.getColumnCount(); i += 1) { 
				     queryTable.getColumn(i).pack(); 
				     }
				// Reset text
				value.setText("");
				
				// pull all constraints out of table
				TableItem[] items = queryTable.getItems();
				// put constraits into HashMap
				for(int i = 0; i < items.length; i++){
					query.put(items[i].getText(0), items[i].getText(1));
				}
				// query the database.
				Controller.doQuery(query);
			}
			
			if (e.getSource() == clearBtn){
				queryTable.removeAll();
				Controller.refreshDatabaseView();
			}
			if(e.getSource() == saveBtn){
				
				Vector vec = new Vector();
				TableItem[] items = queryTable.getItems();
			
				for(int i = 0; i < items.length; i++){
					String key = items[i].getText(0);
					String data = items[i].getText(1);
					vec.add(new Constraint(key,data));
				}
				try{
					FileOutputStream fos = new FileOutputStream(new File("query.ser"));
					ObjectOutputStream obj = new ObjectOutputStream(fos);
					obj.writeObject(vec);
					obj.flush();
					obj.close();
					fos.close();
				}
				catch(IOException er){
					System.err.println("Could not save query.");
				}
			}
			if(e.getSource() == loadBtn){
				queryTable.removeAll();
				try{
					FileInputStream in = new FileInputStream("query.ser");
					ObjectInputStream obj = new ObjectInputStream(in);
					Vector constraints = (Vector)obj.readObject();
					Iterator it = constraints.iterator();
					
					while(it.hasNext()){
						Constraint c = (Constraint)it.next();
						TableItem ti = new TableItem(queryTable,SWT.None);
						ti.setText(0, c.getKey());
						ti.setText(1, c.getData());
					}
					
					// do the query
					TableItem[] items = queryTable.getItems();
					HashMap query = new HashMap();
					for(int i = 0; i < items.length; i++){
						query.put(items[i].getText(0), items[i].getText(1));
					}
					Controller.doQuery(query);
					
					
				}
				catch(Exception er){
					System.err.println("Failed to load query.");
				}
					
				
			}
		}
		public void widgetDefaultSelected(SelectionEvent e){
			
		}
	}

}
