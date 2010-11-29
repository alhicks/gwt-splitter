package ie.gwtsplitter.codeinspect;

import ie.gwtsplitter.codeinspect.domain.CodeInspectResult;
import ie.gwtsplitter.codeinspect.domain.Feature;
import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSPackage;
import ie.gwtsplitter.codeinspect.domain.GSSplit;
import ie.gwtsplitter.codeinspect.visitor.ClassCodeVisitor;
import ie.gwtsplitter.codeinspect.visitor.ClassInitCodeVisitor;
import ie.gwtsplitter.codeinspect.visitor.CodeVisitorBase;
import ie.gwtsplitter.codeinspect.visitor.MethodCodeVisitor;
import ie.gwtsplitter.codeinspect.visitor.MethodInvocationCodeVisitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class CodeInspector {
	
	/**
	 * Used by analyzeCode to store analysis.
	 */
	public static Map<Integer, GSSplit> gsSplits =
		new HashMap<Integer, GSSplit>();
	
	public static Map<String, GSPackage> gsPackages =
		new HashMap<String, GSPackage>();
	
	public static Map<String, Feature> features =
		new HashMap<String, Feature>();
	
	public static Map<String, GSClass> gsClasses = 
		new HashMap<String, GSClass>();
	
	
	/**
	 * 
	 * @param directoryPath 	Source code file path
	 * @param visitors 	List of AST visitors
	 * @return CodeInspectResult 	Result of analysis
	 */
	public static CodeInspectResult analyzeCode(final String directoryPath,
			final List<CodeVisitorBase> visitors) {

		// Get java files from path
		String[] filenames = getJavaFilenames(directoryPath);

		// Get an instance of java compiler
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		// Get a new instance of the standard file manager implementation
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
				null, null, null);

		// Set Project CLASSPATH
		String[] getJars = getJarFilenames(directoryPath);
		List<String> options = new ArrayList<String>();
		options.add("-classpath");
		StringBuilder sb = new StringBuilder();
		for (String jar : getJars) {
			sb.append(jar).append(File.pathSeparator);
		}
		System.out
				.println("\n+\n" + sb.toString().replace(':', '\n') + "+\n\n");
		options.add(sb.toString());

		// Get the list of java file objects, in this case we have only
		Iterable<? extends JavaFileObject> compilationUnit = fileManager
				.getJavaFileObjects(filenames);

		JavacTask task = (JavacTask) compiler.getTask(null, fileManager,
				null, options, null, compilationUnit);

		try {
			Iterable<? extends CompilationUnitTree> asts = task.parse();
			task.analyze();
			Trees trees = Trees.instance(task);

			// Workout class/method structure phase:1.
			for (CompilationUnitTree ast : asts) {
				ClassCodeVisitor classVisitor = new ClassCodeVisitor();
				classVisitor.compilationUnit = ast;
				classVisitor.scan(ast, trees);

				MethodCodeVisitor methodVisitor = new MethodCodeVisitor();
				methodVisitor.compilationUnit = ast;
				methodVisitor.scan(ast, trees);
			}

			// Workout class/method structure phase:2.
			for (CompilationUnitTree ast : asts) {
				MethodInvocationCodeVisitor invoVisitor =
					new MethodInvocationCodeVisitor();
				invoVisitor.compilationUnit = ast;
				invoVisitor.scan(ast, trees);
				
				ClassInitCodeVisitor classInitVisitor =
					new ClassInitCodeVisitor();
				classInitVisitor.compilationUnit = ast;
				classInitVisitor.scan(ast, trees);
			}

			// Run users Visitors
			for (CompilationUnitTree ast : asts) {
				for (CodeVisitorBase v : visitors) {
					v.compilationUnit = ast;
					v.scan(ast, trees);
				}
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CodeInspectResult cir = new CodeInspectResult();
		cir.gsSplits = gsSplits;	
		cir.features = features;
		cir.gsPackages = gsPackages;
		cir.gsClasses = gsClasses;
		
		return cir;
	}
	

	/**
	 * Process all files and directories under dir.
	 * 
	 * @param dir
	 *            directoy
	 * @param recordFiles
	 *            files
	 * @param fileType
	 *            file ending
	 */
	private static void visitAllDirsAndFiles(final File dir,
			final List<String> recordFiles, final String fileType) {
		process(dir, recordFiles, fileType);

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				visitAllDirsAndFiles(new File(dir, children[i]), recordFiles,
						fileType);
			}
		}
	}
	

	/**
	 * 
	 * @param dir
	 *            File directory
	 * @param recordFiles
	 *            List of found files
	 * @param fileType
	 *            Type of file endings to look for e.g. '.java'
	 */
	private static void process(final File dir, final List<String> recordFiles,
			final String fileType) {
		if (!dir.isDirectory() && dir.getName().endsWith(fileType)) {
			recordFiles.add(dir.getAbsolutePath());
		}
	}

	/**
	 * 
	 * @param directoryPath
	 *            Source code directory
	 * @return array of filenames
	 */
	private static String[] getJavaFilenames(final String directoryPath) {
		List<String> files = new LinkedList<String>();
		File file = new File(directoryPath);
		visitAllDirsAndFiles(file, files, ".java");

		String[] filenames = new String[files.size()];
		for (int i = 0; i < files.size(); i++) {
			filenames[i] = files.get(i);
		}
		return filenames;
	}

	/**
	 * 
	 * @param directoryPath
	 *            source code directory
	 * @return array of filenames
	 */
	private static String[] getJarFilenames(final String directoryPath) {
		File file = new File(directoryPath);
		String source = file.getAbsolutePath();
		String projectRoot = source.substring(0, source.length()
				- "src".length());
		List<String> files = new LinkedList<String>();
		visitAllDirsAndFiles(new File(projectRoot), files, ".jar");

		String[] filenames = new String[files.size()];
		for (int i = 0; i < files.size(); i++) {
			filenames[i] = files.get(i);
		}
		return filenames;
	}	
	
}
