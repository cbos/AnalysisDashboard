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
			
			$rootScope.imageJobStatus = function(job)
			{
				if(job)
				{
					var img = "";
					switch (job.status)
					{
					case "UNSTABLE":
						img = "yellow";
					  break;
					case "STABLE":
						img = "blue";
					  break;
					case "FAILED":
						img = "red";
					  break;
					default:
						img = "grey";
					}
					if(job.building)
					{
						return "img/" + img + "_anime.gif";
					}
					return "img/" + img + ".png";
				}
			}
				
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
		$http({method: 'GET', url: 'https://gist.github.com/cbos/3ef7272e0e9fabd02ed7/raw/16d7416cd83ae1ab68bebec8c1a988f16957427d/unstableList.json'}).
	      success(function(data, status) {
	        $scope.status = status;
	        $scope.jobs = data;
	      }).
	      error(function(data, status) {
	        $scope.jobs = null;
	        $scope.status = status;
	    });
			//$scope.jobs = GistData.query();
	}
	$timeout($scope.reload, 0);
}
