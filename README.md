# AnalysisDashboard #

## Idea of AnalysisDashboard ##

Analysis of the buildstreet takes time, especially when there are issues.
This tool is mend to help in the analysis and recognize when failures/issues are recurring. 
Overtime it should also be able to identify random issues. 

## Setup of the AnalysisDashboard ##

* [PlayFramework](http://www.playframework.org/documentation/2.0.4/Home) as web framework, it support handling of REST URLs and can quickly render dynamic webpages.
* [Bootstrap](http://twitter.github.com/bootstrap/) for uniform styling
* [AngularJS](http://angularjs.org/) for the web-app part

## How to start

* Download PlayFramework
* Create a database (for example called 'analysisDashboard')
* Edit conf/application.conf and add something like  
	db.default.driver=com.mysql.jdbc.Driver  
	db.default.url="jdbc:mysql://localhost/analysisDashboard"  
	db.default.user=root  
	db.default.password=password  
	db.default.jndiName=DefaultDS  
	jpa.default=defaultPersistenceUnit  
* Execute 'run' of [PlayFramework Console](http://www.playframework.com/documentation/2.1.0/PlayConsole) and open http://localhost:9000 for testing

## Structure of the project

\+ app 								(backend code)  
--\+ Global.java 					(application handler)  
--\+ analysis  						(containing the analyzers of Jenkins)  
--\+ controllers  					(containing the controllers for handling the frontend requests)  
--\+ jsonhandling 					(containing the wrappers which wrap the json object for Jenkins objects)  
--\+ model 							(containing the data model of the analyisDashboard)  
--\+ views 							(default folder for Play views)  
\+ test 							(tests for the backend)  
\+ config  
--\+ application.conf 				(configuration of the application)  
--\+ routes 						(mapping of rest urls to controllers and artifacts)  
\+ public 							(web files)  
--\+ css   
--\+ img  
--\+ js 							(angularjs files like app, controller, filters, services)  
--\+ --\+ bootstrap 				(javascript files bootstrap)  
--\+ lib   
--\+ --\+ angular 					(libraries of angularjs)  
--\+ partials 						(html files for the views of angularjs like editors, dashboard etc)  
--\+ template 						(html files for the angular directives)  
