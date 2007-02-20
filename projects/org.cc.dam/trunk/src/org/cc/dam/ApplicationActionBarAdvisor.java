package org.cc.dam;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;


public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	
	// actions
	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	
	
	
	
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
    	// make and register the exit action
    	exitAction = ActionFactory.QUIT.create(window);
    	register(exitAction);
    	// make and register the about action
    	aboutAction = ActionFactory.ABOUT.create(window);
    	register(aboutAction);
    }
    protected void fillMenuBar(IMenuManager menuBar) {
    	
    	// lets create a menu! 
    	MenuManager damMenu = new MenuManager("&File","File");
    	
    	// add coponents to the menu
    	damMenu.add(aboutAction);
    	damMenu.add(exitAction);
    	
    	
    	// add the damMenu to the menu bar
    	menuBar.add(damMenu);
    	
    	
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
    	
    	// creating a toolbar
    	IToolBarManager toolbar = new ToolBarManager(coolBar.getStyle());

    	// adding components to the toolbar    	
    	toolbar.add(aboutAction);
    	toolbar.add(exitAction);
    	
    	// adding the toolbar
    	coolBar.add(toolbar);
    	
    	
    }
    
    public void populateCoolBar(IActionBarConfigurer configurer) {
    	ICoolBarManager mgr = configurer.getCoolBarManager();
    	IToolBarManager toolbar = new ToolBarManager(mgr.getStyle());
    	mgr.add(toolbar);
    	toolbar.add(aboutAction);
    	toolbar.add(exitAction);
    }
}
