'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
var serviceModule = angular.module('analysisApp.services', ['ngResource']);
serviceModule.value('version', '0.1');

serviceModule.factory('JenkinsServer', function($resource) {
      var JenkinsServer = $resource('/jenkinsserver/:id',
          { }, {
            update: { method: 'POST' }
          }
      ); 
      
      JenkinsServer.prototype.update = function(cb) {
          return JenkinsServer.update({id: this.id},
              angular.extend({}, this), cb);
        };
      
      return JenkinsServer;
    });