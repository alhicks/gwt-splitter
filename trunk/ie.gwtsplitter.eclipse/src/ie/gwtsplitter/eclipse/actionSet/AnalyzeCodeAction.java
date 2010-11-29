package ie.gwtsplitter.eclipse.actionSet;

import ie.gwtsplitter.codeinspect.CodeInspector;
import ie.gwtsplitter.codeinspect.Marker;
import ie.gwtsplitter.codeinspect.domain.CodeInspectResult;
import ie.gwtsplitter.codeinspect.visitor.CodeVisitorBase;
import ie.gwtsplitter.codeinspect.visitor.GWTSplitCodeVisitor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import mobius.util.plugin.ConsoleUtils;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class AnalyzeCodeAction implements IWorkbenchWindowActionDelegate {

	IJavaProject project = null;
	
	
	public static CodeInspectResult cir = null;
	
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		//System.out.println(project.get);

		ConsoleUtils.ConsoleOutputWrapper c = new ConsoleUtils.ConsoleOutputWrapper();
		c.wrap();
		
		try {
			// Find 'src' directory
			IResource srcRoot = null;
			IPackageFragmentRoot[] root = project.getPackageFragmentRoots();
			for (IPackageFragmentRoot current: root) {
				if (current.getResource().getName().endsWith("src")) {
					srcRoot = current.getResource();
					break;
				}
			}
			
			//Add visitors
			List<CodeVisitorBase> visitors = new LinkedList<CodeVisitorBase>();
			
			GWTSplitCodeVisitor gwtCodeVisitor = new GWTSplitCodeVisitor();
			visitors.add(gwtCodeVisitor);
			
			String srcPath = "";
			if (srcRoot!=null) {
				srcPath = srcRoot.getRawLocation().toString();
				srcPath = "/Users/alanh/local/gwt-splitter-svn/trunk/test-apps/ShowcaseGWT_BasicSplit/src/";
				cir = CodeInspector.analyzeCode(srcPath, visitors);
				System.out.print(" ======= ");
				System.out.print("GWT Split Count: " + gwtCodeVisitor.getCount());
			}
			
			// Remove Markers
			removeAllMarkers();
			
			//IWorkbenchPage page = 
			//	Workbench.getInstance().getActiveWorkbenchWindow().getActivePage();
			//IPerspectiveDescriptor perspective = page.getPerspective();
			//String viewId = "de.vogella.jfreechart.pie.view"; //defined by you
			//get the reference for your viewId
			//IViewReference ref = page.findViewReference(viewId);
			//IViewPart x = null;
			//release the view
			//perspective.getViewFactory.releaseView(ref);
			
			for (String pKey: cir.gsPackages.keySet()) {
				System.out.println(pKey);
			}
			
			for (Marker marker: gwtCodeVisitor.getMarkers()) {
				String pathWithProject = marker.getFileName().replaceAll(srcPath, "");
				IResource resource = project.getProject().findMember("src/"+pathWithProject);
				IMarker iMarker = resource.createMarker(IMarker.BOOKMARK);
				iMarker.setAttribute(IMarker.MESSAGE, marker.getDisplayTag());
				iMarker.setAttribute(IMarker.LINE_NUMBER, marker.getStartLineNumber());
				iMarker.setAttribute(IMarker.CHAR_START, marker.getStartPosition());
				iMarker.setAttribute(IMarker.CHAR_END, marker.getEndPosition());
			}
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		c.unwrap();
		
	}

	public static void listFilesInFolder(IFolder folder) throws CoreException {
		IResource[] resources = folder.members();
		for (IResource resource: resources) {
			//System.out.println(resource.getName());
			if (resource instanceof Folder) {
				//System.out.println(" => " + resource.getName());
				listFilesInFolder((Folder)resource);
				
			} else {
				//IMarker marker = resource.createMarker(IMarker.PROBLEM);
				//marker.setAttribute(IMarker.MESSAGE, "test");
				//System.out.println(" --> " + resource.getName());
			}
		}
		
	}
	
	private void removeAllMarkers() {
		try {
			project.getProject().deleteMarkers(IMarker.MARKER, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection s = (IStructuredSelection)selection;
			
			project = null;
			
			Iterator it = s.iterator();
			
			while (it.hasNext()) {
				Object o = it.next();
//				if (o instanceof IResource) {
//					IResource r = (IResource) o;
//					if (project==null) {
//						project = r.getProject();
//					
//					} else if (project==r.getProject()) {
//						; // do nothing.
//						PackageFragmentRoot p;
//					} else {
//						action.setEnabled(false);
//						return;
//					}
//				} else 
				if (o instanceof IJavaElement) {
					IJavaElement jp = (IJavaElement)o;
					project = jp.getJavaProject();
				} else {
					System.out.println(o.getClass());
				}
				
			}
			
			if (project==null) {
				action.setEnabled(false);
			} else {
				action.setEnabled(true);
			}
			return;
			
//			try {
//				// check project type
//				//if (project.hasNature("org.eclipse.jdt.core.javanature")) {
//					action.setEnabled(true);
//					return;
//				} else {
//					action.setEnabled(false);
//					return;
//				}
//			} catch (CoreException e) {
//				action.setEnabled(false);
//				return;
//			}
			
		}
		
	}

}
