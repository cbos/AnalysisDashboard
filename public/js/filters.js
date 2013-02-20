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
		if (items) {
			var arrayToReturn = [];
			for ( var i = 0; i < items.length; i++) {
				if (items[i].status != "STABLE") {
					arrayToReturn.push(items[i]);
				}
			}
			return arrayToReturn;
		}
		return items;
	}
});

//This method orders the jobs in the order of the jobtype (jobtype definition contains order values)
filterModule.filter('jobType', function() {
	return function(items, types) {
		if (items) {
			// Less than 0: Sort "a" to be a lower index than "b"
			// Zero: "a" and "b" should be considered equal, and no sorting performed.
			// Greater than 0: Sort "b" to be a lower index than "a".
			items.sort(function(a, b) {
				var diff = getOrder(a, types) - getOrder(b,types);
				if(diff == 0)
				{
					return (a.name<b.name?-1:(a.name>b.name?1:0));
				}
				return diff;
			});
		}
		return items;
	}
});

function getOrder(job, types) {
	for ( var i = 0; i < types.length; i++) {
		if(types[i].value == job.type)
		{
			return types[i].order;
		}
	}
	return 1000;
}