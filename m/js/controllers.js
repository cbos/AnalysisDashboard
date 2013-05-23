'use strict';

angular.module('analysisApp.rootScopeInitializer', []).run(function($rootScope) 
		  {
			$rootScope.jobTypes = [
			                {name:'Full Build', value:'fullbuild', order:10},
			                {name:'Install', value:'install', order:20},
			                {name:'Upgrade', value:'upgrade', order:30},
			                {name:'Sync-merge', value:'syncmerge', order:40},
			                {name:'Drop-merge', value:'dropmerge', order:50},
			                {name:'Quick build', value:'quick-build', order:60},
			                {name:'Regression test', value:'regression-test', order:70},
			                {name:'Loadtests', value:'loadtests', order:80},
			                {name:'Misc', value:'misc', order:90},
			                {name:'Unknown', value:'', order:100}
				            ];
			
			$rootScope.getRuns = function(job) {
				if(job && job.lastBuild)
				{
					if(job.lastBuild.childBuilds.length >0)
					{
						return job.lastBuild.childBuilds;
					}
					return [job.lastBuild];
				}
				return null;
			}
		});

/* Controllers */

function MobileController($scope, $rootScope, $timeout, $http) {
	
	$scope.reload = function()
	{
		$http({method: 'GET', url: 'https://api.github.com/gists/3ef7272e0e9fabd02ed7'}).
	      success(function(data, status) {
	        $scope.status = status;
	        $scope.jobs = angular.fromJson(data.files['unstableList.json'].content);
	        $scope.lastUpdate = data['updated_at'];
	      }).
	      error(function(data, status) {
	        $scope.jobs = null;
	        $scope.status = status;
	    });
	}
	$timeout($scope.reload, 0);
}
