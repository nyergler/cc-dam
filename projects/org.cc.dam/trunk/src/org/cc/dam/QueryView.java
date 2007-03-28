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
	
	private MetaDataLoader mdLoader;

	private String[] tags;

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
	/***
	 * Creates a lyout for the view and places the
	 * widgets into the view.
	 */
	public void createPartControl(Composite parent) {
		//Load the metadata
		mdLoader = new MetaDataLoader();
		tags = mdLoader.getAllTags();

		// Controller for query actions
		QueryController qc = new QueryController();
		
		// set the parents layout
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
		
		// we have no history, disable the buttons
		if (history.isEmpty()) {
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
		a.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Key combo box
		key = new Combo(a, SWT.BORDER);
		key.setItems(tags);
		// Value text box
		value = new Text(a, SWT.BORDER);
		value.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

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
	/***
	 * Inner class to handle all query events
	 */
	private class QueryController implements SelectionListener {
		
		private ConstraintList cl;
		/***
		 * Constructor
		 * does nothing.
		 */
		public QueryController() {

		}
		/***
		 * Handles selection events from the query view
		 */
		public void widgetSelected(SelectionEvent e) {
			Shell shell = ((Control) e.getSource()).getShell();
			/***
			 * Add a constraint to the queryTable, then perform query.
			 */
			if (e.getSource() == addBtn) {
				cl = new ConstraintList();
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
				for (int i = 0; i < items.length; i++) {
					String key = items[i].getText(0);
					String value = items[i].getText(1);
					cl.addConstraint(key, value);
				}
				// add the constraints to the history.
				history.addLast(cl);
				// set the cursor to new search
				cursor = history.size()-1;
				// we can now go back into history
				backBtn.setEnabled(true);
				// query the database.
				Controller.doQuery(cl.toHashMap());

			}
			/***
			 * Load the previous query kept in memory
			 */
			if (e.getSource() == backBtn) {
				
				// were going back
				cursor--;
				
				// update buttons
				if (cursor <= -1)
					backBtn.setEnabled(false);
				if (cursor < history.size())
					forwardBtn.setEnabled(true);
				// load the history into the view
				loadHistory(cursor);

			}
			/***
			 * Load the query that is forward from where we are at.
			 */
			if (e.getSource() == forwardBtn) {
				// were going forward
				cursor++;
				// udpate buttons
				if (cursor == (history.size() - 1))
					forwardBtn.setEnabled(false);
				if (cursor > 0)
					backBtn.setEnabled(true);
				// load the history in the view.
				loadHistory(cursor);
			}
			/***
			 * Clear the query table and refresh the database
			 */
			if (e.getSource() == clearBtn) {
				clearQueryTable();
			}
			/***
			 * Save the current query to a file
			 */
			if (e.getSource() == saveBtn) {

				FileDialog chooser = new FileDialog(shell, SWT.SAVE);
				String path = chooser.open();
				history.get(cursor).saveToFile(path);
			}
			/***
			 * Load a query from a file
			 */
			if (e.getSource() == loadBtn) {
				clearQueryTable();
				cl = new ConstraintList();
				try {
					FileDialog chooser = new FileDialog(shell, SWT.OPEN);
					String path = chooser.open();
					BufferedReader inFile = new BufferedReader(new FileReader(
							path));
					String line;
					while ((line = inFile.readLine()) != null) {
						StringTokenizer tok = new StringTokenizer(line, ConstraintList.DELIM);
						if (tok.countTokens() == 2) {
							String key = tok.nextToken();
							String value = tok.nextToken();
							cl.addConstraint(key, value);
						} else {
							System.err.println("Invalid query file.");
							break;
						}
					}

					history.addLast(cl);
					// set the cursor to new search
					cursor = history.size()-1;
					// we can now go back into history
					backBtn.setEnabled(true);
					
					loadHistory(cursor);

				} catch (Exception er) {
					System.err.println("Failed to load query.");
				}

			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {

		}
		/***
		 * Loads a query from the history linked list.
		 * (private helper)
		 * @param time The index of the query to load
		 */
		private void loadHistory(int time) {
			//clear the table
			clearQueryTable();
			// pull out the query
			Iterator it = history.get(time).toIterator();
			// build the table
			while (it.hasNext()) {
				ConstraintWrapper a = (ConstraintWrapper) it.next();
				TableItem ti = new TableItem(queryTable, SWT.NONE);
				ti.setText(0, a.getKey());
				ti.setText(1, a.getValue());
				// perform the query
				Controller.doQuery(history.get(time).toHashMap());
			}

		}
		/***
		 * Clears the queryTable and refrshes that database
		 * (private helper)
		 */
		private void clearQueryTable() {
			queryTable.removeAll();
			Controller.refreshDatabaseView();
		}
	}

}
