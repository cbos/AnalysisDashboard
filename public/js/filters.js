'use strict';

/* Filters */

var filterModule = angular.module('analysisApp.filters', []);
filterModule.filter('interpolate', [ 'version', function(version) {
	return function(text) {
		return String(text).replace(/\%VERSION\%/mg, version);
	}
} ]);

filterModule.filter('nonStableJobs', function() {
	return function(items) {
		var arrayToReturn = [];
		for ( var i = 0; i < items.length; i++) {
			if (items[i].status != "STABLE" ) {
				arrayToReturn.push(items[i]);
			}
		}
		return arrayToReturn;
	}
});