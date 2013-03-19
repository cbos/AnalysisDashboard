import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "AnalysisDashboard"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
    	javaCore, javaJpa, 
      "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
      "mysql" % "mysql-connector-java" % "5.1.18"
    )

	val main = play.Project(appName, appVersion, appDependencies).settings(

		//Fix issue in generation scalaDoc (with WebSockets)
		sources in doc in Compile := List(),

		//This line is needed to load the .json files correctly for Play framework tests
		//See http://journal.michaelahlers.org/2013/01/play-framework-and-testing-resources.html
		//console: show test:resource-directory will show the right folder now
		
      	resourceDirectory in Test <<= (baseDirectory) apply  {(baseDir: File) => baseDir / "test"}  
    )
}
