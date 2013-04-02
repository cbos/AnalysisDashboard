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
