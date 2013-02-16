'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.
var serviceModule = angular.module('analysisApp.services', [ 'ngResource' ]);
serviceModule.value('version', '0.2');

serviceModule.factory('JenkinsServer', function($resource, $http, $rootScope) {
	var JenkinsServer = $resource('/jenkinsserver/:id', {
		id : '@id'
	});

	JenkinsServer.prototype.analyzeServer = function() {
		var js = this;
		js.isBusyAnalyzing = true;
		$http.get('/jenkinsserver/' + this.id + '/analyzeServer').success(
				function(data, status, headers, config) {
					js.isBusyAnalyzing = false;
				}).error(function(data, status, headers, config) {
			js.isBusyAnalyzing = false;

			$rootScope.addAlert("error", "Analysis of the server failed", "HTML status code:" + status, data);
		});
	};

	JenkinsServer.prototype.isAnalyzing = function() {
		return !!this.isBusyAnalyzing;
	}

	return JenkinsServer;
});

serviceModule.factory('Computer', function($resource) {
	var Computer = $resource('/computer/:id', {
		id : '@id'
	});
	return Computer;
});

serviceModule.factory('Job', function($resource) {
	var Job = $resource('/job/:id', {
		id : '@id'
	});
	return Job;
});