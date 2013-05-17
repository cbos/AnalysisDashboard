'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.
var serviceModule = angular.module('analysisApp.services', [ 'ngResource' ]);

serviceModule.factory('GistData', function($resource, $http, $rootScope) {
	var GistData = $resource('https://gist.github.com/cbos/3ef7272e0e9fabd02ed7/raw/16d7416cd83ae1ab68bebec8c1a988f16957427d/unstableList.json');
	return GistData;
});
