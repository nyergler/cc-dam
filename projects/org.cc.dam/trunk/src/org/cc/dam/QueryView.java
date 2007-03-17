package org.cc.dam;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
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

	private String[] tags = { "dc:title", "dc:creator" };

	private Table queryTable;

	private Combo key;

	private Text value;

	private Button addBtn;

	private Button clearBtn;

	private Button saveBtn;

	private Button loadBtn;

	private Button backBtn;
	
	private Button forwardBtn;

	private LinkedList<ConstraintList> history;
	
	private int cursor;

	public QueryView() {
		history = new LinkedList<ConstraintList>();
		
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

		parent.setLayout(new GridLayout(1, false));

		// Window information Label
		Label queryLabel = new Label(parent, SWT.NONE);
		queryLabel.setText("Use the options below to search the database.");

		Composite topControls = new Composite(parent, SWT.NONE);
		topControls.setLayout(new GridLayout(2, false));
		topControls.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Group historyGroup = new Group(topControls, SWT.NONE);
		historyGroup.setLayout(new GridLayout(2, false));
		historyGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		historyGroup.setText("History");

		backBtn = new Button(historyGroup, SWT.None);
		backBtn.setText("Back");
		backBtn.addSelectionListener(qc);
		
		forwardBtn = new Button(historyGroup, SWT.None);
		forwardBtn.setText("Forward");
		forwardBtn.addSelectionListener(qc);
		
		if(history.isEmpty()){
			backBtn.setEnabled(false);
			forwardBtn.setEnabled(false);
		}

		Group slGroup = new Group(topControls, SWT.NONE);
		slGroup.setLayout(new GridLayout(2, false));
		slGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		slGroup.setText("Save / Load");

		saveBtn = new Button(slGroup, SWT.None);
		saveBtn.setText("save");
		saveBtn.addSelectionListener(qc);

		loadBtn = new Button(slGroup, SWT.None);
		loadBtn.setText("load");
		loadBtn.addSelectionListener(qc);

		// Query Builder
		Group queryBuilder = new Group(parent, SWT.NONE);
		queryBuilder.setText("Query Builder");
		queryBuilder.setLayout(new GridLayout(1, true));
		queryBuilder.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL));
		// Make the table
		queryTable = new Table(queryBuilder, SWT.NONE);
		queryTable.setHeaderVisible(true);
		queryTable.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL));

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
	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private class QueryController implements SelectionListener {
		private ConstraintList cl;
		public QueryController(){
			
		}
		
		public void widgetSelected(SelectionEvent e) {
			Shell shell = ((Control) e.getSource()).getShell();
			
			final String delim = "@";

			if (e.getSource() == addBtn) {
				cl = new ConstraintList();
				HashMap<String, String> query = new HashMap<String, String>();
				// Make TableItem from data
				String[] data = { tags[key.getSelectionIndex()],
						value.getText() };
				TableItem a = new TableItem(queryTable, SWT.NONE);
				a.setText(data);
				for (int i = 0; i < queryTable.getColumnCount(); i += 1) {
					queryTable.getColumn(i).pack();
				}
			
				// Reset text
				value.setText("");

				// pull all constraints out of table
				TableItem[] items = queryTable.getItems();
				// put constraits into HashMap
				for (int i = 0; i < items.length; i++) {
					String key = items[i].getText(0);
					String value = items[i].getText(1);
					
					query.put(key,value);
					// add the key-value pair to the constraints list
					cl.addConstraint(key,value);
				}
				// query the database.
				Controller.doQuery(query);
				// add the constraints to the history.
				history.addLast(cl);
				// set the cursor to new search
				cursor = history.size();
				// we can now go back into history
				backBtn.setEnabled(true);
				

			}
			if (e.getSource() == backBtn) {
				// were going back
				cursor--;
				// update buttons
				if(cursor == 0)
					backBtn.setEnabled(false);
				if( cursor < history.size())
					forwardBtn.setEnabled(true);
				// load the history into the view
				loadHistory(cursor);
					

			}
			if(e.getSource() == forwardBtn){
				// were going forward
				cursor++;
				// udpate buttons
				if(cursor == (history.size()-1))
					forwardBtn.setEnabled(false);
				if(cursor > 0)
					backBtn.setEnabled(true);
				// load the history in the view.
				loadHistory(cursor);
			}

			if (e.getSource() == clearBtn) {

				queryTable.removeAll();
				Controller.refreshDatabaseView();
			}
			if (e.getSource() == saveBtn) {

				FileDialog chooser = new FileDialog(shell, SWT.SAVE);
				String path = chooser.open();
				try {

					PrintWriter outFile = new PrintWriter(new FileWriter(path));
					TableItem[] items = queryTable.getItems();

					for (int i = 0; i < items.length; i++) {
						String key = items[i].getText(0);
						String data = items[i].getText(1);
						outFile.println(key + delim + data);

					}
					outFile.flush();
					outFile.close();

				} catch (IOException ex) {
					System.err.println("Error saving query.");
				}

			}
			if (e.getSource() == loadBtn) {
				queryTable.removeAll();
				try {
					FileDialog chooser = new FileDialog(shell, SWT.OPEN);
					String path = chooser.open();
					BufferedReader inFile = new BufferedReader(new FileReader(
							path));
					String line;
					while ((line = inFile.readLine()) != null) {
						StringTokenizer tok = new StringTokenizer(line, delim);
						if (tok.countTokens() == 2) {
							TableItem ti = new TableItem(queryTable, SWT.None);
							ti.setText(0, tok.nextToken());
							ti.setText(1, tok.nextToken());
						} else {
							System.err.println("Invalid query file.");
							break;
						}
					}

					// do the query
					TableItem[] items = queryTable.getItems();
					HashMap <String,String>query = new HashMap<String,String>();
					for (int i = 0; i < items.length; i++) {
						query.put(items[i].getText(0), items[i].getText(1));
					}
					Controller.doQuery(query);

				} catch (Exception er) {
					System.err.println("Failed to load query.");
				}

			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {

		}
		private void loadHistory(int time){
			queryTable.removeAll();
			Iterator it = history.get(time).getIterator();
			while (it.hasNext()) {
				
				ConstraintWrapper a = (ConstraintWrapper) it.next();
				TableItem ti = new TableItem(queryTable, SWT.NONE);
				ti.setText(0, a.getKey());
				ti.setText(1, a.getValue());
				
				/***
				 * Things still to do:
				 * -perform the actual query once history is loaded.
				 * 
				 */
		}
			
		}
	}

}
