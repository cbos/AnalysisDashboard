'use strict';

/* Controllers */

function DashboardCtrl($scope, Computer, Job) {
	$scope.computers = Computer.query();
	
	$scope.jobs = Job.query();
}

function ComputerListCtrl($scope, Computer) {
	$scope.computers = Computer.query();
}

function JenkinsServerListCtrl($scope, JenkinsServer) {
	$scope.jenkinsservers = JenkinsServer.query();
}

function JobListCtrl($scope, Job) {
	$scope.jobs = Job.query();
}

function ComputerEditCtrl($scope, $location, $routeParams, $http, Computer) {
	var self = this;

	Computer.get({
		id : $routeParams.id
	}, function(computer) {
		self.original = computer;
		$scope.computer = new Computer(self.original);
	});

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.computer);
	}

	$scope.destroy = function() {
		self.original.$remove(function() {
			$location.path('/computer');
		});
	};

	$scope.save = function() {
		$scope.computer.$save(function() {
			$location.path('/computer');
		});
	};
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

function JobEditCtrl($scope, $location, $routeParams, $http, Job) {
	var self = this;

	Job.get({
		id : $routeParams.id
	}, function(job) {
		self.original = job;
		$scope.job = new Job(self.original);
	});

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.job);
	}

	$scope.destroy = function() {
		self.original.$remove(function() {
			$location.path('/job');
		});
	};

	$scope.save = function() {
		$scope.job.$save(function() {
			$location.path('/job');
		});
	};
}