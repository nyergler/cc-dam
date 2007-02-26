package org.cc.dam;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Point;

import java.util.HashMap;
import java.util.Iterator;

public class DetailWin extends ApplicationWindow implements SelectionListener, FocusListener, KeyListener{

	private TableItem[] entry;
	private Button addBtn;
	private String filename;
	private  TableEditor editor;
	private Table detailTable;
	private Text text;
	private Text type;
	private Text info;
	private TableItem item;
	private ToolItem close;
	private ToolItem save;
	private ToolItem remove;


     public DetailWin(String filename) {
         super(null);
         
         this.filename = filename;
  
     }

	  protected Control createContents(Composite parent)
	  {
		  getShell().setText("Asset Detail View");
		  parent.setSize(500,325);
		  
		  GridLayout layout = new GridLayout();
		  layout.numColumns = 1;
		  		    
		  parent.setLayout(layout);
		  
		  // Title
		  Label title = new Label(parent,SWT.None);
		  title.setText("File detail for: "+this.getFilename());
		  
		  GridData gd = new GridData();
		  gd.grabExcessHorizontalSpace = true;
		  gd.horizontalAlignment = SWT.RIGHT;
		  // Coolbar setup
		  CoolBar coolbar = new CoolBar(parent, SWT.NONE);
		  coolbar.setLayoutData(gd);
		  // Set the toolbar style
	      CoolItem item = new CoolItem(coolbar, SWT.NONE);
	      ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);
	      // Toolbar items
	      
	      remove = new ToolItem(toolbar,SWT.NONE);
	      new ToolItem(toolbar,SWT.SEPARATOR);
	      save = new ToolItem(toolbar, SWT.NONE);
	      close = new ToolItem(toolbar, SWT.NONE);
	    
	      // set the items text
	      save.setText("Save");
	      close.setText("Close");
	      remove.setText("Remove PDF");
	      
	      // Adding the listeners
	      save.addSelectionListener(this);
	      close.addSelectionListener(this);
	      remove.addSelectionListener(this);
	          
	      Point p = toolbar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	      toolbar.setSize(p);
	      Point p2 = item.computeSize(p.x, p.y);
	      item.setControl(toolbar);
	      item.setSize(p2);
	      
	      GridData gridData = new GridData();
		  gridData.horizontalAlignment = GridData.FILL;
		  gridData.verticalAlignment = GridData.FILL;
		  gridData.grabExcessHorizontalSpace = true;
		  gridData.grabExcessVerticalSpace = true;
		  detailTable = new Table(parent, SWT.FULL_SELECTION);
		  detailTable.setLayoutData(gridData);
		  editor = new TableEditor(detailTable);
		  
		  detailTable.addSelectionListener(this);
		  detailTable.setHeaderVisible(true);
		  
		  TableColumn meta = new TableColumn(detailTable,SWT.LEFT);
		  meta.setText("Metadata Key");
		  meta.setResizable(true);
		  
		  TableColumn data = new TableColumn(detailTable,SWT.LEFT);
		  data.setText("Data");
		  data.setResizable(true);
		 
		  meta.pack();
		  data.pack();
		 
		  TableItem path = new TableItem(detailTable,0);
		  path.setText(0, "Location: ");
		  path.setText(1,filename);
		  // method to read the metadata and output to table
		  this.fillTable();
		  
		  // Add metadata SWT widget setup
		  GridLayout wide5 = new GridLayout();
		  GridData addGridData = new GridData();
		  addGridData.grabExcessHorizontalSpace = true;
		  addGridData.horizontalAlignment = SWT.FILL;
		  wide5.numColumns = 5;
		  
		  Group addGroup = new Group(parent, SWT.NULL);
	      addGroup.setText("Add metadata");
	      addGroup.setLayout(wide5);
	      addGroup.setLayoutData(addGridData);
	      
	      new Label(addGroup,SWT.NONE).setText("Metadata key:");	 
	      type = new Text(addGroup, SWT.BORDER);
	      type.setLayoutData(addGridData);
	     
	      new Label(addGroup,SWT.NONE).setText("Data:");
	      info = new Text(addGroup, SWT.BORDER);
	      info.setLayoutData(addGridData);
	      addBtn = new Button(addGroup,SWT.NULL);
	      addBtn.setText("add");
	      addBtn.addSelectionListener(this);
	      
	    return parent;
	  }
	  
	  public void widgetSelected(SelectionEvent e) {
		  if(e.getSource().equals(addBtn)){
			  String[] tmp = {type.getText(),info.getText()};
			  TableItem row = new TableItem(detailTable,0);
			  row.setText(tmp);
		  }
		  // current metdata needs to be saved to the database
		  if(e.getSource().equals(save)){
			  this.detailTable.setSelection(0);
			  HashMap<String, String> hm = new HashMap<String, String>();
			  for (int i = 0; i < this.detailTable.getItemCount(); i += 1) {
				  hm.put(this.detailTable.getItem(i).getText(0), this.detailTable.getItem(i).getText(1));
			  }
			  Controller.setMetadataForFile(this.filename, hm);
			  Controller.refreshDatabaseView();
		  }
		  if(e.getSource().equals(remove)){
			  
			 Controller.getDatabase().deleteEntryForFileWithMetadata(filename, null);
			 Controller.refreshDatabaseView();
			 this.getShell().dispose();
		  }
		  if(e.getSource().equals(close)){
			 
			  this.getShell().dispose();
		  }
		  if(e.getSource().equals(detailTable)){
			  
			  Control old = editor.getEditor();
			  if(old != null){old.dispose();}
			  
			  // id the selected row
			  int index = detailTable.getSelectionIndex();
			  if(index == -1 || index == 0){return;}
			  item = detailTable.getItem(index);
			  
			  text = new Text(detailTable,SWT.NONE);
			  text.addFocusListener(this);
			  text.addKeyListener(this);
			  
			  editor.horizontalAlignment = SWT.LEFT;
			  editor.grabHorizontal = true;
			  editor.minimumWidth= 50;
			  editor.setEditor(text,item,1);
			 
			  text.setText(item.getText(1));
			  text.setFocus();
		  }
			  
		  }
		  // close the window
	
public void widgetDefaultSelected(SelectionEvent e) {}
	  
public void keyPressed(KeyEvent e){
	
}
public void keyReleased(KeyEvent e){
	
	final int ENTER_KEY = 13; // key code for ENTER
	if(e.keyCode == ENTER_KEY){
		
		if(text.getText().equals(""))
			item.dispose();
		else
		item.setText(1, text.getText());
		// if auto delete cursor is in the same pos. ?!
		
		this.detailTable.setSelection(0);
	}
}
public void keyTyped(KeyEvent e){}

public void focusGained(FocusEvent e){}
public void focusLost(FocusEvent e){
	
	//auto delete blank metadata
	if(text.getText().equals(""))
		item.dispose();
	else
	item.setText(1, text.getText());
	
	
	
}
private void fillTable(){

	HashMap hash = Controller.getMetadataForFile(filename);
	
     this.entry = new TableItem[hash.size()];
     Iterator iter = hash.keySet().iterator();
     for (int i = 0; i < hash.size() && iter.hasNext(); i += 1) {
         this.entry[i] = new TableItem(detailTable, 0);
         String key  = (String)iter.next();
         String[] tmp = { key, (String)hash.get(key) }; 
         this.entry[i].setText(tmp);
         
        
     }
     for (int i = 0; i < this.detailTable.getColumnCount(); i += 1) { 
     this.detailTable.getColumn(i).pack(); 
     }
}
private String getFilename(){
	//char a = System.getProperty( "path.separator" ).charAt(0);
	return this.filename.substring(1+this.filename.lastIndexOf('/'));
}
}
