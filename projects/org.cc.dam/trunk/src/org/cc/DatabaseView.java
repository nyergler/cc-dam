package org.cc;

import java.util.Observer;
import java.util.Observable;
import java.util.Locale;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.*;
import org.eclipse.core.runtime.IConfigurationElement;
import java.text.Collator;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.window.ApplicationWindow;

import java.util.HashSet;
import java.util.Iterator;

public class DatabaseView extends ViewPart implements Observer{
    //private Controller controller;
private Table table;


		public static final String ID ="org.cc.DatabaseView";
	public DatabaseView() {
        Controller.getDatabase().addObserver(this);
	}

	public void createPartControl(Composite parent) {
	    this.table = new Table(parent, SWT.FULL_SELECTION);
        this.table.setHeaderVisible(true);
        
        
        this.table.addListener(SWT.MouseDoubleClick,new Listener(){
        	public void handleEvent(Event t){
        		
        		// create the detail view window
        		DetailWin wwin = new DetailWin((String)table.getSelection()[0].getData());
        		// pop up the window
        		wwin.open();
        		
        	}
        });

        final TableColumn subject   = new TableColumn(table, SWT.LEFT);
        subject.setResizable(true);
        subject.setText("Title");
        
        final TableColumn predicate = new TableColumn(table, SWT.LEFT);
        predicate.setResizable(true);
        predicate.setText("Author");

        final TableColumn object    = new TableColumn(table, SWT.LEFT);
        object.setResizable(true);
        object.setText("Filename");

        subject.pack(); predicate.pack(); object.pack();
        Controller.refreshDatabaseView();

        Listener sortListener = new Listener() {
            public void handleEvent(Event e) {
                TableItem[] items = table.getItems();
                Collator collator = Collator.getInstance(Locale.getDefault());
                TableColumn column = (TableColumn)e.widget;
                int index = column == subject ? 0 : (column == predicate ? 1 : 2);
                for (int i = 1; i < items.length; i++) {
                    String value1 = items[i].getText(index);
                    for (int j = 0; j < i; j++){
                        String value2 = items[j].getText(index);
                        if (table.getSortDirection() == SWT.UP   && collator.compare(value1, value2) < 0 ||
                            table.getSortDirection() == SWT.DOWN && collator.compare(value1, value2) > 0    ) {
                            String[] values = {items[i].getText(0), items[i].getText(1), items[i].getText(2)};
                            Object data = items[i].getData();
                            items[i].dispose();
                            TableItem item = new TableItem(table, SWT.NONE, j);
                            item.setText(values);
                            item.setData(data);
                            items = table.getItems();
                            break;
                        }
                    }
                }
                if (table.getSortColumn() != column)
                    table.setSortColumn(column);
                else
                    table.setSortDirection(table.getSortDirection() == SWT.UP ? SWT.DOWN : SWT.UP);
            }
        };
        subject.addListener(SWT.Selection, sortListener);
        predicate.addListener(SWT.Selection, sortListener);
        object.addListener(SWT.Selection, sortListener);
        table.setSortColumn(subject);
        table.setSortDirection(SWT.UP);

	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

    /*
    public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
        super.setInitializationData(cfig, propertyName, data);
        String[] stuff = cfig.getAttributeNames();
        for (int i = 0; i < stuff.length; i += 1) {
            //System.out.println(stuff[i] + " => " + cfig.getAttribute(stuff[i]));
        }
    }
    */
    
    public void update(Observable sender, Object data) {
        if (data instanceof UpdateMessage) {
            UpdateMessage msg = (UpdateMessage)data;
            switch (msg.getType()) {
            case UpdateMessage.CLEAR:
            	this.table.removeAll();
            	break;
            case UpdateMessage.LISTING:
                HashSet hs = (HashSet)msg.getPayload();
                Iterator it = hs.iterator();
                while (it.hasNext()) {
                    String[] set2 = (String[])it.next();
                    TableItem row2 = new TableItem(this.table, 0);
                    row2.setText(set2);
                    row2.setData(set2[2]);
                }
                break;
            case UpdateMessage.QUERY:
                HashSet hs2 = (HashSet)msg.getPayload();
                Iterator it2 = hs2.iterator();
                while (it2.hasNext()) {
                    String[] set2 = (String[])it2.next();
                    TableItem row2 = new TableItem(this.table, 0);
                    row2.setText(set2);
                    row2.setData(set2[2]);
                }
                break;
            }
        }
        for (int i = 0; i < this.table.getColumnCount(); i += 1) {
            this.table.getColumn(i).pack();
        }
    }

}
