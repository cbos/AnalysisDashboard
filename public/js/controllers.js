'use strict';

angular.module('analysisApp.rootScopeInitializer', []).run(function($rootScope) 
		  {
			debugger;
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

function DashboardCtrl($scope, Computer, Job) {
	$scope.computers = Computer.query();
	
	$scope.jobs = Job.query();
}

function ComputerListCtrl($scope, Computer) {
	$scope.computers = Computer.query();
}

function JobListCtrl($scope, Job) {
	$scope.jobs = Job.query();
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