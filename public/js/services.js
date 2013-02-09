'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
var serviceModule = angular.module('analysisApp.services', ['ngResource']);
serviceModule.value('version', '0.1');

serviceModule.factory('JenkinsServer', function($resource, $http, $rootScope) {
      var JenkinsServer = $resource('/jenkinsserver/:id',
          { id:'@id'}, {
            update: { method: 'POST' }
          }
      ); 
      
      JenkinsServer.prototype.analyzeComputers = function() {
    	  var js = this;
    	  js.isAnalyzing = true;
          $http.get('/jenkinsserver/' + this.id + '/analyzeComputers').
          success(function(data, status, headers, config) {
        	  js.isAnalyzing = false;
    	  }).
    	  error(function(data, status, headers, config) {
    		  js.isAnalyzing = false;
    		  
    		  $rootScope.errorObject = { data: data, status : status, info: "Analysis of the computers failed"}
    		  $rootScope.showError = true;
		  });
      };
      
      JenkinsServer.prototype.isAnalyzingComputers = function()
      {
    	  return !!this.isAnalyzing;
      }
      
      return JenkinsServer;
    });