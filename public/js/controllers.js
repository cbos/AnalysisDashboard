'use strict';

/* Controllers */


function MyCtrl1() {}
MyCtrl1.$inject = [];


function MyCtrl2() {
}
MyCtrl2.$inject = [];


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


function JenkinsServerEditCtrl($scope, $location, $routeParams, JenkinsServer) {
var self = this;

JenkinsServer.get({id: $routeParams.id}, function(project) {
  self.original = project;
  $scope.jenkinsserver = new JenkinsServer(self.original);
});

$scope.isClean = function() {
  return angular.equals(self.original, $scope.jenkinsserver);
}

$scope.destroy = function() {
	debugger;
  self.original.$remove(function() {
    $location.path('/jenkinsserver');
  });
};

$scope.save = function() {
	debugger;
  $scope.jenkinsserver.$save(function() {
    $location.path('/');
  });
};
}