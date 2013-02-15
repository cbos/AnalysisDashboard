'use strict';

/* Controllers */

function DashboardCtrl($scope, Computer, Job) {
	$scope.computers = Computer.query();
	
	$scope.jobs = Job.query();
}

function ComputerListCtrl($scope, Computer) {
	$scope.computers = Computer.query();
	
	$scope.change = function(index) {
	    $scope.computers[index].$save();
	};
	
	$scope.destroy = function(index) {
		$scope.computers[index].$remove(function() {
			$scope.computers.splice(index, 1);
		});
	};
}

function JobListCtrl($scope, Job) {
	$scope.jobs = Job.query();
	
	$scope.change = function(index) {
	    $scope.jobs[index].$save();
	};
	
	$scope.destroy = function(index) {
		$scope.jobs[index].$remove(function() {
			$scope.jobs.splice(index, 1);
		});
	};
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