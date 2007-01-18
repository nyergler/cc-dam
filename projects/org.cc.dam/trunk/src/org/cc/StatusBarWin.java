package org.cc;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ProgressBar;

public class StatusBarWin extends ApplicationWindow {
	
	private ProgressBar bar;
	public StatusBarWin(){
	super(null);
	}
	 protected Control createContents(Composite parent)
	  {
		  getShell().setText("Your File is Downloading!");
		  parent.setSize(300,100);
		  
		  GridLayout layout = new GridLayout();
		  layout.numColumns = 1;
		  		    
		   parent.setLayout(layout);
		  
	       Group status = new Group(parent, SWT.NULL);
	       status.setText("Status");
	       GridLayout statusLayout = new GridLayout();
	       statusLayout.numColumns=1;
	       status.setLayout(statusLayout);
	       GridData layoutData = new GridData(); 
	       layoutData.grabExcessHorizontalSpace= true;
	       layoutData.horizontalAlignment = GridData.FILL;
	       status.setLayoutData(layoutData);
	        bar = new ProgressBar(status,SWT.INDETERMINATE);
	        bar.setLayoutData(layoutData);
		  
		  return parent;
}
}