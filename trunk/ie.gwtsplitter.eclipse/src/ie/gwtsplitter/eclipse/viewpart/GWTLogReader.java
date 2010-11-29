package ie.gwtsplitter.eclipse.viewpart;

import ie.gwtsplitter.reader.DataCollecter;
import ie.gwtsplitter.reader.PathElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

public class GWTLogReader extends ViewPart {

	private Timer timer = null;
	private TimerTask task = null;
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	private final static File f = new File("/Users/alanh/local/tomcat/gwt-splitter-track.log"); 
	
	private Text text;
	
	public GWTLogReader() {
		// TODO Auto-generated constructor stub
	}

	static Composite thisParent = null;
	static TabFolder tabFolder = null;
	static ChartComposite frame = null;
	static TabItem chartTabItem = null;
	static TabItem logTabItem = null;
	static TabItem tableTabItem = null;
	
	@Override
	public void createPartControl(Composite parent) {
		
		thisParent = parent;
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		tabFolder = new TabFolder(parent, SWT.NONE);
		frame = null;
		{
			{
				logTabItem = new TabItem(tabFolder, SWT.NONE);
				logTabItem.setText("Log View");
				{
					text = new Text(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
					logTabItem.setControl(text);
				}
			}
			{
				chartTabItem = new TabItem(tabFolder, SWT.NONE);
				chartTabItem.setText("Chart View");
				{
					frame = projectChart(tabFolder,new String[0], new double[0]);
					chartTabItem.setControl(frame);
				}
			}
		}
		{
			tableTabItem = new TabItem(tabFolder, SWT.NONE);
			tableTabItem.setText("Grid View");
			{
				table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
	
				tableTabItem.setControl(table);
				table.setHeaderVisible(true);
				table.setLinesVisible(true);
			}
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private static long lm = 0;
	
	private static int count = 10;
	private Table table;
	
	public void task(boolean runTimer) {
		text.setText(readFile());
		if (runTimer) {
			timer = new Timer();
			task = new TimerTask() {
				@Override
				public void run() {
					Display display = getSite().getShell().getDisplay();
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							if (lm!=f.lastModified()) {
								lm = f.lastModified();
								DataCollecter dc = getDataCollector(readFile());
								Map<String,PathElement> paths = dc.getPaths();
								String data = "";
								for (String key: paths.keySet()) {
									data += key + "\t" + paths.get(key.toString());
								}
								
								// update log
								text.setText(data);
								
								Map<String,Integer> patternCounts = dc.getPatternCounts();
								String[] patterns = new String[patternCounts.size()];
								double[] values = new double[patternCounts.size()];
								
								int index=0;
								for (String key: patternCounts.keySet()) {
									patterns[index] = key;
									values[index] = patternCounts.get(key).doubleValue();
									index++;
								}
								
								// update chart
								frame.dispose();
								frame = projectChart(tabFolder, patterns, values);
								chartTabItem.setControl(frame);
								
								// update table
								table.dispose();
								table = getTable(tabFolder, patterns, values);
								tableTabItem.setControl(table);
								thisParent.layout();
							}
						}
					});
				}
			};
			timer.schedule(task, 0, 100);
			
		} else {
			timer.cancel();
		}
	}
	
	public DataCollecter getDataCollector(String data) {
		return new DataCollecter(data);
	}	
	
	private String readFile() {
		String data = "";
		try {
	        BufferedReader in = new BufferedReader(new FileReader(f.getAbsolutePath()));
	        String str;
	        while ((str = in.readLine()) != null) {
	            data += str + LINE_SEPARATOR;
	        }
	        in.close();
	    } catch (IOException e) {
	    	;
	    }
	    System.out.println(data);
	    return data;
	}
	
	public static ChartComposite projectChart(Composite parent, String[] patterns, double[] values) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i=0;i<patterns.length;i++) {
        	dataset.setValue(values[i], patterns[i], patterns[i]);
        }
        JFreeChart jfc = ChartFactory.createBarChart3D("Pattern Chart", "patterns", "hits", dataset, PlotOrientation.VERTICAL, true, true, false);
				
		ChartComposite chart = new ChartComposite(parent, SWT.NONE, jfc, true);
        return chart;      
	}	
	
	public Table getTable(Composite composite, String[] patterns, double[] values) {
		table = new Table(composite, SWT.BORDER);
		table.setLinesVisible(true);
		TableColumn tc1 = new TableColumn(table, SWT.LEFT);
		TableColumn tc2 = new TableColumn(table, SWT.RIGHT);
		
		tc1.setText("Pattern");
		tc2.setText("Hits");
		tc1.setWidth(190);
		tc2.setWidth(70);
		table.setHeaderVisible(true);
		
		for (int i=0; i<patterns.length; i++) {
			String[] data = new String[2];
			data[0] = patterns[i];
			data[1] = String.valueOf(values[i]);
			TableItem item1 = new TableItem(table, SWT.NONE);
			item1.setText(data);
		}  
		return table;
	}
	  
}
