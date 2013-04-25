import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "AnalysisDashboard"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
    	javaCore, javaJpa, 
      "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
      "mysql" % "mysql-connector-java" % "5.1.18",
      "org.apache.commons" % "commons-email" % "1.3.1", 
      "commons-io" % "commons-io" % "2.0.1"
    )

	val main = play.Project(appName, appVersion, appDependencies).settings(

		// Add app folder as resource directory so that widget files are in the classpath
		unmanagedResourceDirectories in Compile <+= baseDirectory( _ / "app"),
		// but filter out java files that would then also be copied to the classpath
		excludeFilter in Compile in unmanagedResources := "*.java",

		//Fix issue in generation scalaDoc (with WebSockets)
		sources in doc in Compile := List(),

		//This line is needed to load the .json files correctly for Play framework tests
		//See http://journal.michaelahlers.org/2013/01/play-framework-and-testing-resources.html
		//console: show test:resource-directory will show the right folder now
		
      	resourceDirectory in Test <<= (baseDirectory) apply  {(baseDir: File) => baseDir / "test"}  
    )
}
