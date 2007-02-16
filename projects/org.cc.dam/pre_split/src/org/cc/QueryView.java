package org.cc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import java.util.Vector;
import java.util.HashMap;

public class QueryView extends ViewPart {
    private Button submitBtn;// Submit button
    private Button basicSubmit;
    private Button basicClear;
    private Button clearBtn;
    private Button addBtn;
    private Text basicTf;
    private Vector keys;
    private Vector values;
    private Vector deletes;
    private Group queryGroup;
    private GridData gridData;
    
    public static final String ID = "org.cc.QueryView";
    public QueryView() {
        // TODO Auto-generated constructor stub
    }

    /***
     * createPartControl creates a layout for the view, and creates / places the
     * widgets into the view.
     */
    public void createPartControl(Composite parent) {
        // parallel arrays used to convert "friendly names" to keys
    	final String[] basic_constraints = {"Title","Author","License"};
    	final String[] basic_keys = {"dc:title","dc:creator","cc:license"};
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        parent.setLayout(gridLayout);
        
        gridData = new GridData(SWT.NONE);
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;

        // Window information Label
        Label queryLabel = new Label(parent, SWT.NONE);
        queryLabel.setText("Use the options below to search the database.");
        
        GridData gridDatah = new GridData(SWT.NONE);
        gridDatah.horizontalAlignment = GridData.FILL;
        gridDatah.grabExcessHorizontalSpace = true;
        
        Group basic = new Group(parent,SWT.NULL);
        basic.setText("Basic Search");
        basic.setLayout(gridLayout);
        basic.setLayoutData(gridDatah);
        
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        
        Composite c = new Composite(basic,SWT.NONE);
        c.setLayout(gridLayout);
        c.setLayoutData(gridData);
        //c.pack();
        final Combo searchType = new Combo(c,SWT.READ_ONLY);
        for(int i = 0; i< basic_constraints.length;i+=1)
        	searchType.add(basic_constraints[i]);
       
        basicTf = new Text(c,SWT.BORDER);
        basicTf.setLayoutData(gridData);
        
        Composite d = new Composite(basic,SWT.NONE);
        d.setLayout(gridLayout);
        //d.setLayoutData(gridData);
        //d.pack();
        basicSubmit = new Button(d,SWT.NONE);
        basicSubmit.setText("Submit");
        basicSubmit.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) { }
            public void widgetSelected(SelectionEvent e) {
                HashMap hm = new HashMap();
                int index = searchType.getSelectionIndex();
                             
                if(index >= 0 && index < basic_keys.length && !basicTf.getText().equals("")){
                	hm.put(basic_keys[index],basicTf.getText());
                	Controller.doQuery(hm);
                }
                else return;
            }
        } );
        
        basicClear = new Button(d,SWT.NONE);
        basicClear.setText("Clear Results");
        basicClear.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) { }
            public void widgetSelected(SelectionEvent e) {
                Controller.refreshDatabaseView();
            }
        } );
        
        GridData gridDataf = new GridData(SWT.NONE);
        gridDataf.horizontalAlignment = GridData.FILL;
        gridDataf.grabExcessHorizontalSpace = true;
        gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        Group filter_type = new Group(parent,SWT.None);
        filter_type.setLayout(gridLayout);
        filter_type.setLayoutData(gridDataf);
        filter_type.setText("File Types");
        
        final Button pdf_filter = new Button(filter_type,SWT.CHECK);
        pdf_filter.setText("PDF");
        pdf_filter.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) { }
            public void widgetSelected(SelectionEvent e) {
            	
            	if(pdf_filter.getSelection()){
            		final HashMap hm = new HashMap();
            		hm.put("dc:format","application/pdf");
            		Controller.doQuery(hm);
            	}
            	else{
            		Controller.refreshDatabaseView();
            	}
            }
              
        } );
        
        
        
    
        GridData gridDataa = new GridData(SWT.NONE);
        gridDataa.horizontalAlignment = GridData.FILL;
        gridDataa.grabExcessHorizontalSpace = true;
        gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        // Query boxes
        queryGroup = new Group(parent, SWT.NULL);
        queryGroup.setText("Advanced Search");

        queryGroup.setLayout(gridLayout);
        queryGroup.setLayoutData(gridDataa);
        
        /*
        
        Label alpha_descr = new Label(this.queryGroup, SWT.NONE);
        alpha_descr.setText("Metadata Key");
        
        Label beta_descr = new Label(this.queryGroup, SWT.NONE);
        beta_descr.setText("Data Value");
        
        Label gamma_descr = new Label(this.queryGroup, SWT.NONE);
        gamma_descr.setText("");
        
        alpha_descr.setLayoutData(this.gridData);
        beta_descr.setLayoutData(this.gridData);
        gamma_descr.setLayoutData(this.gridData);
        
        alpha_descr.pack();
        beta_descr.pack();
        gamma_descr.pack();
        
        */
        

        Text alpha = new Text(this.queryGroup, SWT.BORDER);
        alpha.setLayoutData(gridData);
        alpha.setText("Metadata Key");
        
        Text beta = new Text(this.queryGroup, SWT.BORDER);
        beta.setLayoutData(gridData);
        beta.setText("Data Value");
       
        final Button gamma = new Button(this.queryGroup, SWT.NONE);
        gamma.setText("Delete");
        gamma.addSelectionListener(new SelectionListener() {
                public void widgetDefaultSelected(SelectionEvent e) { }
                public void widgetSelected(SelectionEvent e) {
                    int index = deletes.indexOf(gamma);
                    ((Text)keys.elementAt(index)).dispose();
                    ((Text)values.elementAt(index)).dispose();
                    gamma.dispose();
                    keys.removeElementAt(index);
                    values.removeElementAt(index);
                    deletes.removeElementAt(index);
                    queryGroup.pack(true);
                    queryGroup.getParent().pack(true);
                }
            } );

        this.keys = new Vector();
        this.values = new Vector();
        this.deletes = new Vector();
        
        this.keys.addElement(alpha);
        this.values.addElement(beta);
        this.deletes.addElement(gamma);
        
        alpha.pack();
        beta.pack();
        gamma.pack();
        
        final Composite btn_pane = new Composite(parent,SWT.None);
        btn_pane.setLayout(gridLayout);
       

        submitBtn = new Button(btn_pane, SWT.NONE);
        submitBtn.setText("Submit");
        submitBtn.addSelectionListener(new SelectionListener() {
                public void widgetDefaultSelected(SelectionEvent e) { }
                public void widgetSelected(SelectionEvent e) {
                    HashMap hm = new HashMap();
                    for (int index = 0; index < keys.size(); index += 1)
                        if (!((Text)keys.elementAt(index)).getText().equals("")) 
                            hm.put(((Text)keys.elementAt(index)).getText(),
                                   ((Text)values.elementAt(index)).getText());
                    if (hm.size() != 0)
                        Controller.doQuery(hm);
                }
            } );

        clearBtn = new Button(btn_pane, SWT.NONE);
        clearBtn.setText("Clear Results");
        clearBtn.addSelectionListener(new SelectionListener() {
                public void widgetDefaultSelected(SelectionEvent e) { }
                public void widgetSelected(SelectionEvent e) {
                    Controller.refreshDatabaseView();
                }
            } );

        addBtn = new Button(btn_pane, SWT.NONE);
        addBtn.setText("Add Constraint");
        addBtn.addSelectionListener(new SelectionListener() {
                public void widgetDefaultSelected(SelectionEvent e) { }
                public void widgetSelected(SelectionEvent e) {
                          Text   alpha = new Text(queryGroup, SWT.BORDER);
                          Text   beta  = new Text(queryGroup, SWT.BORDER);
                    final Button gamma = new Button(queryGroup, SWT.NONE);
                    
                    gamma.setText("Delete");
                    gamma.addSelectionListener(new SelectionListener() {
                        public void widgetDefaultSelected(SelectionEvent e) { }
                        public void widgetSelected(SelectionEvent e) {
                            int index = deletes.indexOf(gamma);
                            ((Text)keys.elementAt(index)).dispose();
                            ((Text)values.elementAt(index)).dispose();
                            gamma.dispose();
                            keys.removeElementAt(index);
                            values.removeElementAt(index);
                            deletes.removeElementAt(index);
                            queryGroup.pack(true);
                            queryGroup.getParent().pack(true);
                        }
                    } );
                    
                    alpha.setLayoutData(gridData);
                    beta.setLayoutData(gridData);
                    
                    keys.addElement(alpha);
                    values.addElement(beta);
                    deletes.addElement(gamma);
                    
                    alpha.pack();
                    beta.pack();
                    gamma.pack();
                    
                    queryGroup.pack(true);
                    queryGroup.getParent().pack(true);
                }
            } );
            
    }

    public void setFocus() {
        // TODO Auto-generated method stub

    }

}
