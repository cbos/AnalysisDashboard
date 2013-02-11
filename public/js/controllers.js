'use strict';

/* Controllers */

function DashboardCtrl($scope, Computer) {
	$scope.computers = Computer.query();
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

function JenkinsServerEditCtrl($scope, $location, $routeParams, $http, JenkinsServer) {
	var self = this;
	
	JenkinsServer.get({id: $routeParams.id}, function(jenkinsserver) {
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