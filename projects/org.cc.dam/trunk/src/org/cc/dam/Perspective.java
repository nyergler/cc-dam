package org.cc.dam;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IFolderLayout;

public class Perspective implements IPerspectiveFactory {
	private String leftFolderID= "org.cc.dam.leftfolder";
	
	public void createInitialLayout(IPageLayout layout) {
	
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);

	// create a layout folder
	 IFolderLayout left_folder = layout.createFolder(
		 leftFolderID, IPageLayout.LEFT, (float) 0.45, layout.getEditorArea());
	
	// add the ImportFile view to the folder
	left_folder.addView(ImportFileView.ID);
	// add the QueryView tot he folder
	left_folder.addView(QueryView.ID);

	// add the Database view!
	layout.addView(DatabaseView.ID,
            IPageLayout.RIGHT, (float) .5,IPageLayout.ID_EDITOR_AREA);
	
	}
	String getLeftFolderID(){return leftFolderID;}
}
