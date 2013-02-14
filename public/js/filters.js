'use strict';

/* Filters */

var filterModule = angular.module('analysisApp.filters', []);
filterModule.filter('interpolate',
		[ 'version', function(version) {
			return function(text) {
				return String(text).replace(/\%VERSION\%/mg, version);
			}
		} ]);

filterModule.filter('filterNonStableJobs', function() {
    return function(job) {
      if(job.status == "STABLE")
      {
    	  return null;
      }
      return job;
    }
  });

/*
.filter('weDontLike', function(){

	return function(items, name){

	    var arrayToReturn = [];        
	    for (var i=0; i<items.length; i++){
	        if (items[i].name != name) {
	            arrayToReturn.push(items[i]);
	        }
	    }

	    return arrayToReturn;
	};*/