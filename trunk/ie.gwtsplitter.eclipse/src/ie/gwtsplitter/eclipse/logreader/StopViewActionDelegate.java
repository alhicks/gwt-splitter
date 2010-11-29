package ie.gwtsplitter.eclipse.logreader;

import ie.gwtsplitter.eclipse.viewpart.GWTLogReader;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class StopViewActionDelegate implements IViewActionDelegate {

	private IViewPart view;
	
	@Override
	public void init(IViewPart view) {
		this.view = view;
	}

	@Override
	public void run(IAction action) {
        if (this.view instanceof GWTLogReader) {
        	GWTLogReader logView = (GWTLogReader)this.view;
        	logView.task(false);
        	
            IContributionItem ic = 
            	this.view.getViewSite().getActionBars()
            		.getToolBarManager().find("ie.gwtsplitter.eclipse.view.logreader.Start");
            if (ic!=null && ic instanceof ActionContributionItem) {
            	IAction iaction=((ActionContributionItem)ic).getAction();
            	iaction.setEnabled(true);
            }
            action.setEnabled(false);        	
        }        
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
