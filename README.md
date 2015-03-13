### Project Structure Directory:
/CodeInspect 					- parser,analyser and splitter

/gwt
	/TrackModule 				- gwt user logging module

/ie.gwtsplitter.eclipse 			- eclipse plugin
/jfreechartjars					- required for plugin


This research focuses on applying automated techniques for code splitting in Google Web Toolkit (GWT) 1? applications. Code splitting is one of the advanced features in the 2.0 release of GWT and aims to speed up the compiled application by separating the code into individual download segments. Segments are downloaded only when required by the end user. Code splitting is a useful feature because as applications get more complex and have a bigger footprint, it is necessary to find ways to speed up the response time for the user.

GWT allows developers to build JavaScript? applications using the Java language. The toolkit converts Java code into JavaScript?. GWT abstracts from the low-level browser quirks of JavaScript? development. The comprehensive set of features provided by GWT represents a viable alternative to JavaScript? programming. Over the last several years GWT has become increasingly popular. The resulting tools, which have been developed by the GWT community, are proof of this popularity. One example of such a tool is the GWT Eclipse Plugin which enables simple compilation and deployment of GWT-based applications within the Eclipse environment.

Despite advances in GWT, such as compiler optimisations, a potential bottleneck at application start up is still relatively common. The cause of this bottleneck is the need to download the entire JavaScript? application. This methodology represents a poor use of resources, as the end user may not require the full feature set of the application. The GWT team have introduced, in an effort to alleviate this bottleneck, a technique called code splitting. Code splitting gives developers an API to support splitting their application up into chunks that are loaded on demand, as triggered by subsequent events. The use of code splitting separates the download impact over the entire user’s session and thus is intended to reduce the number of download bottlenecks. An additional benefit is that the user will download only the code they require for the features of the application they use.

This research extends the code splitting technique by providing a mechanism that helps objectively identify code segments for code splitting. This analysis is based upon static code analysis, feature annotations and/or recorded user usage. This assists the user as their required downloads will be staggered according to the architecture of the application and the application will feel more like a responsive desktop application rather than a web application.

An Eclipse plugin is utilised, the intention of which is to implement this advancement of code splitting techniques. The plugin analyses the application source code and quantitative data and displays graphical information to the developer. The plugin thus highlights to the developer where code splitting should take place.

To demonstrate the concepts proposed in this research a comparison is made between a codesplitting sample application produced by the GWT team and the same application split using the techniques developed in this research. The research illustrates that it is not only possible to automatically apply code splitting, but that the concepts proposed produce positive results, as can be seen in a reduction in the initial JavaScript? code load of the sample application by 65%.
