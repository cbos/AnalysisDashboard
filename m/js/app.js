'use strict';

// Declare app level module which depends on filters, and services
angular.module(
		'analysisApp',
		[ 'analysisApp.rootScopeInitializer', 'analysisApp.filters', 'analysisApp.services']).config(['$httpProvider', function($httpProvider) {
			$httpProvider.defaults.useXDomain = true;
		    delete $httpProvider.defaults.headers.common["X-Requested-With"];
		}]);