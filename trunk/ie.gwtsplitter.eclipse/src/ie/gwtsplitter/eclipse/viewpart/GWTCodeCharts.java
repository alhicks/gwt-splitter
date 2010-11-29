package ie.gwtsplitter.eclipse.viewpart;

import ie.gwtsplitter.codeinspect.domain.CodeInspectResult;
import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSMethod;
import ie.gwtsplitter.codeinspect.domain.GSPackage;
import ie.gwtsplitter.eclipse.actionSet.AnalyzeCodeAction;

import java.awt.Font;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

public class GWTCodeCharts extends ViewPart {

	public static final String ID = "de.vogella.jfreechart.pie.view";

	static Composite thisParent = null;
	static ChartComposite frame = null;
	
	static CodeInspectResult cir = null;
	
	public void createPartControl(Composite parent) {
		thisParent = parent;
		frame = projectChart(parent);
	}

	public void setFocus() {
	}

	static String currentPackageName = null;
	
	public static ChartComposite classChart(Composite parent, String packageName, String className) {
		
        DefaultPieDataset dataset = new DefaultPieDataset();
        cir = AnalyzeCodeAction.cir;
        
        GSPackage gsPackage = cir.gsPackages.get(packageName);
        GSClass gsClass = gsPackage.getGSClass(className);
        for (GSMethod method: gsClass.getMethods()) {
        	if (!method.codeGenerated) {
        		dataset.setValue(method.prettyName, method.totalSize());
        	}
        }
		
		JFreeChart jfc = ChartFactory.createPieChart3D("Class Chart: "+className, dataset, true, true, false);
		PiePlot pp = (PiePlot) jfc.getPlot();
		pp.setSectionOutlinesVisible(false);
		pp.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		pp.setNoDataMessage("No Data");
		pp.setCircular(false);
		pp.setLabelGap(0.02);
		
		ChartComposite chart = new ChartComposite(parent, SWT.NONE, jfc, true);
		chart.addChartMouseListener(new ChartMouseListener() {
			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {
			}
			
			@Override
			public void chartMouseClicked(ChartMouseEvent arg0) {
				frame.dispose();
				frame = projectChart(thisParent);
				thisParent.layout();
			}
		});
        return chart;
	}	
	
	public static ChartComposite packageChart(Composite parent, String packageName) {
		
		cir = AnalyzeCodeAction.cir;
		currentPackageName = packageName;
		
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for (String pKey: cir.gsPackages.keySet()) {
        	GSPackage gsPackage = cir.gsPackages.get(pKey);
        	if (pKey.equals(packageName)) {
	        	for (String key: gsPackage.gsClasses.keySet()) {
	        		GSClass gsClass = gsPackage.gsClasses.get(key);
	        		dataset.setValue(gsClass.name, gsClass.getSize());
	        	}
	        	break;
        	}
        }
		
		JFreeChart jfc = ChartFactory.createPieChart3D("Package Chart: "+packageName, dataset, true, true, false);
		PiePlot pp = (PiePlot) jfc.getPlot();
		pp.setSectionOutlinesVisible(false);
		pp.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		pp.setNoDataMessage("No Data");
		pp.setCircular(false);
		pp.setLabelGap(0.02);
		
		ChartComposite chart = new ChartComposite(parent, SWT.NONE, jfc, true);
		chart.addChartMouseListener(new ChartMouseListener() {
			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {
			}
			
			@Override
			public void chartMouseClicked(ChartMouseEvent arg0) {
				PieSectionEntity pse = (PieSectionEntity) arg0.getEntity();
				frame.dispose();
				frame = classChart(thisParent, currentPackageName, pse.getSectionKey().toString());
				thisParent.layout();
			}
		});
        return chart;
	}	
	
	public static ChartComposite projectChart(Composite parent) {
		
		cir = AnalyzeCodeAction.cir;
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		try {
			int max = 0;
	        for (String pKey: cir.gsPackages.keySet()) {
	        	GSPackage gsPackage = cir.gsPackages.get(pKey);
	        	int packageSize = 0;
	        	for (String key: gsPackage.gsClasses.keySet()) {
	        		GSClass gsClass = gsPackage.gsClasses.get(key);
	        		packageSize += gsClass.getSize();
	        	}
	        	dataset.setValue(gsPackage.name, packageSize);
	        	max++;
	        	if (max>8) {
	        		break;
	        	}
	        }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		JFreeChart jfc = ChartFactory.createPieChart3D("Project Chart", dataset, true, true, false);
		PiePlot pp = (PiePlot) jfc.getPlot();
		pp.setSectionOutlinesVisible(false);
		pp.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		pp.setNoDataMessage("No Data");
		pp.setCircular(false);
		pp.setLabelGap(0.02);
		
		ChartComposite chart = new ChartComposite(parent, SWT.NONE, jfc, true);
		chart.addChartMouseListener(new ChartMouseListener() {
			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {
			}
			
			@Override
			public void chartMouseClicked(ChartMouseEvent arg0) {
				PieSectionEntity pse = (PieSectionEntity) arg0.getEntity();
				frame.dispose();
				frame = packageChart(thisParent, pse.getSectionKey().toString());
				thisParent.layout();
			}
		});
        return chart;
	}
	
}
