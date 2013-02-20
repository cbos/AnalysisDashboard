'use strict';

angular.module('analysisApp.rootScopeInitializer', []).run(function($rootScope) 
		  {
			$rootScope.alerts = [];

			$rootScope.addAlert = function(type, info, status, data)
			{
				$rootScope.alerts.push({type: type, info: info, status: status, data:data});
			};

			$rootScope.closeAlert = function(index)
			{
			     $rootScope.alerts.splice(index, 1);
			};
			
			$rootScope.change = function(entity) {
			    entity.$save();
			};

			$rootScope.destroy = function(entityArray, entity) {
				var index = entityArray.indexOf(entity);
				entity.$remove(function() {
					entityArray.splice(index, 1);
				});
			};

		});

/* Controllers */

function DashboardCtrl($scope, $timeout,  Computer, Job) {
	
	$scope.reload = function()
	{
		$scope.computers = Computer.query();
		
		$scope.jobs = Job.query();
		
		$timeout($scope.reload, 60000);
	}
	$timeout($scope.reload, 0);
	
	$scope.stopWatching = function(job) {
		job.watch = false;
		job.$save();
	};
}

function ComputerListCtrl($scope, Computer) {
	$scope.computers = Computer.query();
}

function JobListCtrl($scope, Job) {
	$scope.jobs = Job.query();
	
	$scope.types = [
                {name:'Full Build', value:'fullbuild'},
                {name:'Install', value:'install'},
                {name:'Upgrade', value:'upgrade'},
                {name:'Sync-merge', value:'syncmerge'},
                {name:'Drop-merge', value:'dropmerge'},
                {name:'Loadtests', value:'loadtests'},
                {name:'Regression test', value:'regression-test'},
                {name:'Quick build', value:'quick-build'},
                {name:'Misc', value:'misc'},
                {name:'Unknown', value:''}
	            ];
}

function JenkinsServerListCtrl($scope, JenkinsServer) {
	$scope.jenkinsservers = JenkinsServer.query();
}

function JenkinsServerCreateCtrl($scope, $location, JenkinsServer) {
	$scope.save = function() {
		JenkinsServer.save($scope.jenkinsserver, function(jenkinsserver) {
			$location.path('/jenkinsserver/edit/' + jenkinsserver.id);
		});
	}
}

function JenkinsServerEditCtrl($scope, $location, $routeParams, $http,
		JenkinsServer) {
	var self = this;

	JenkinsServer.get({
		id : $routeParams.id
	}, function(jenkinsserver) {
		self.original = jenkinsserver;
		$scope.jenkinsserver = new JenkinsServer(self.original);
	});

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.jenkinsserver);
	}

	$scope.destroy = function() {
		self.original.$remove(function() {
			$location.path('/jenkinsserver');
		});
	};

	$scope.save = function() {
		$scope.jenkinsserver.$save(function() {
			$location.path('/jenkinsserver');
		});
	};
}