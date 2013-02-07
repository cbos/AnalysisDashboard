'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
var serviceModule = angular.module('analysisApp.services', ['ngResource']);
serviceModule.value('version', '0.1');

serviceModule.factory('JenkinsServer', function($resource) {
      var JenkinsServer = $resource('/jenkinsserver/:id',
          { id:'@id'}, {
            update: { method: 'POST' }
          }
      ); 
      
      return JenkinsServer;
    });