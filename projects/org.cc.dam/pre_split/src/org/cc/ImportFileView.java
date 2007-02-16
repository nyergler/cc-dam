package org.cc;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.*;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;



public class ImportFileView extends ViewPart implements SelectionListener {
    private Controller controller;
    private Text url; // url text bar
    private Text browse; // browse text bar										
    private Button urlBtn; // button for url import							
    private Button browseBtn; // button for file browsing
    private Button importFileBtn; // button for local file import
    private String path; // path of file
    private ProgressBar bar;
    private Group remoteFileGroup;
    public static final String ID = "org.cc.ImportFileView"; // view ID
    
    private static final String[] FILTER_NAMES = {
        "Portable Document Format (*.pdf)",
    "All Files (*.*)"};
    
    
    private static final String[] FILTER_EXTS = { "*.pdf","*.*"};
    
    public ImportFileView() {
        // TODO Auto-generated constructor stub
        this.controller = new Controller();
        
    }
    /***
     * createPartControl creates a layout for the view, and creates / places the
     * widgets into the view.
     */
    public void createPartControl(Composite parent) {
        // TODO Auto-generated method stub
        
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        parent.setLayout(gridLayout); 
        path="http://";
        
        // Welcome text for the view
        Label welcomeLabel = new Label(parent, SWT.NONE);
        welcomeLabel.setText("Welcome to the file selection view.\nUse one of the methods to import a file.");
        
        // Local file browsing!
        Group localFileGroup = new Group(parent, SWT.NULL);
        
        localFileGroup.setText("Browse Local Files");
        
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        
        GridData gridData = new GridData(SWT.NONE);
        
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        //localFileGroup.setLayoutData(gridData);   
        localFileGroup.setLayout(gridLayout);
        localFileGroup.setLayoutData(gridData);
        
        
        browse = new Text(localFileGroup, SWT.BORDER);
        browse.setLayoutData(gridData);
        
        browseBtn = new Button(localFileGroup, SWT.NONE);
        browseBtn.setText("...");
        browseBtn.addSelectionListener(this);
        
        importFileBtn = new Button(localFileGroup, SWT.NONE);
        importFileBtn.setText("Import local file");
        importFileBtn.addSelectionListener(this);
        
        
        remoteFileGroup = new Group(parent, SWT.NULL);
        
        remoteFileGroup.setText("Remote file from URL");
        
        gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        remoteFileGroup.setLayout(gridLayout);
        GridData urlGridData = new GridData();
        urlGridData.horizontalAlignment = GridData.FILL;
        urlGridData.grabExcessHorizontalSpace = true;
        remoteFileGroup.setLayoutData(urlGridData);
        
        // text box for remote (URL) selection
        url = new Text(remoteFileGroup, SWT.BORDER);
        url.setText(path);
        url.setLayoutData(urlGridData);
       
        // button for "remote"
        
        urlBtn = new Button(remoteFileGroup, SWT.NONE);
        urlBtn.setText("Import file from URL");
        urlBtn.addSelectionListener(this);
    
        // Nathan is obsessed with drag and drop - so lets do it!
        
        // Create the dnd group
        Group dndGroup = new Group(parent, SWT.NULL);
        dndGroup.setText("Drag and Drop");
        // layout stuff
        gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        dndGroup.setLayout(gridLayout);
        GridData dndGridData = new GridData();
        dndGridData.horizontalAlignment = GridData.FILL;
        dndGridData.verticalAlignment= GridData.FILL;
        dndGridData.grabExcessHorizontalSpace = true;
        dndGridData.grabExcessVerticalSpace = true;
        
        dndGroup.setLayoutData(dndGridData);
        
       
        final Table dndArea = new Table(dndGroup, SWT.NONE);
        dndArea.setLayoutData(dndGridData); 
        
        final TableViewer viewer = new TableViewer(dndArea);
      
        Transfer[] agents = new Transfer[] {FileTransfer.getInstance()};
        int ops = DND.DROP_COPY | DND.DROP_MOVE;
        
        viewer.addDropSupport(ops, agents, new ViewerDropAdapter(viewer){
        	
        public boolean performDrop(Object o){
        	boolean status = true;
        	// cast object into array of absolute paths
        	String [] filePaths = (String[])o;
        	
        	//for all file paths
        	for(int i = 0; i < filePaths.length;i+=1){
        		try {
                    Controller.parseFile(filePaths[i]);
                }
            catch (Exception ex) { System.err.println(ex.toString()); status = false; }
            	
        	}
        	
        	return status;
        }
        public boolean validateDrop(Object target, int operation, TransferData type){
        	return FileTransfer.getInstance().isSupportedType(type);
        }
        	
        });
      
        
    }
    /***
     * widgetSelected is called when a button on the ImportFile view is pressed.
     * this method is responsible for determining where the action came from and
     * then carries out the appropriate action.
     * 
     * when an import file button is pressed (local and URL) the input is recorded 
     * in the path string (this string is the path or URL of the file to be imported.
     * 
     */
    public void widgetSelected(SelectionEvent e) {
        
        Shell shell = ((Control)e.getSource()).getShell();
        
        // a file needs to be imported
        if(e.getSource().equals(importFileBtn)){
            path = browse.getText();
            if(path.equals("") || path == null){
                
                MessageDialog.openWarning(shell, "File Import Error!", "A file was not selected! Please select a valid file.");
            }
            else if(!validExtension(path)){
                
                MessageDialog.openWarning(shell, "File Import Error!", "The file must be a PDF!");
            }
            
            else
                try {
                    Controller.parseFile(path);
                }
            catch (Exception ex) { System.err.println(e.toString()); }
            // clear the browse box after file was imported.
            browse.setText("");
        }
        // a URL was entered
        if(e.getSource().equals(urlBtn)){
            boolean flag = false;
            String filename = null;
            path = url.getText();
            StatusBarWin status = new StatusBarWin();
                         
            
            // form the filename
            filename = path.substring( path.lastIndexOf("/")+1);
            // pop up the message
            flag = MessageDialog.openQuestion(shell,"Remote File Confirmation", "Would you like to download and refrence "+filename+" locally?\n" +
                    "(By clicking \"No\", the file will only be refrenced by URL."+")");
            
            // the file needs to be saved locally
            if(flag){
                
                // set up the "save file" dialog an open it 
                FileDialog chooser2 = new FileDialog(shell, SWT.SAVE); 
                chooser2.setFileName(filename);
                chooser2.open();
                
                try {
                    // download the file 
                	status.open();
                	// start the progress bar
                	bar.setVisible(true);
                    if (!Controller.parseURL(path,chooser2.getFilterPath()+"/"+filename))
                        MessageDialog.openWarning(shell, "File Import Error!", "Sorry, I don't know how to parse this kind of file.");
                }
                catch (Exception ex) { ex.printStackTrace(); }
                
                
            }
            // we only want a temp copy of the file
            else{
                try {
                	// start the progress bar 
                	status.open();
                    // the controller will take care of the temp, just pass in null
                    if (!Controller.parseURL(path, null))
                        MessageDialog.openWarning(shell, "File Import Error!", "Sorry, couldn't open the file.");
                }
                catch (Exception ex) { ex.printStackTrace(); }
            }
            
            
            status.close();
            url.setText("http://");
        }

        // the user want to browse the filesystem
        if(e.getSource().equals(browseBtn)){
            
            // create file selector dialog
            FileDialog chooser = new FileDialog(shell);
            // apply filters
            chooser.setFilterNames(FILTER_NAMES);
            chooser.setFilterExtensions(FILTER_EXTS);
            // open the dialog
            if (chooser.open() != null) 
                browse.setText(chooser.getFilterPath() + System.getProperty("file.separator") + chooser.getFileName()); 	
        }
    }
    public String stripPath(String tmp){
  
    	if(tmp.substring(0,7).equals("file://"))
    	return tmp.substring(7);
    	else
    	return null;
    }
    public void widgetDefaultSelected(SelectionEvent e) {}
    public void setFocus() {
        // TODO Auto-generated method stub
        
    }
    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
        
    }
    private boolean validExtension(String path){
        
        // should i ignore the case?!
        return getExtension(path).equalsIgnoreCase(".pdf");
    }
}