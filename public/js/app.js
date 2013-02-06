'use strict';


// Declare app level module which depends on filters, and services
angular.module('analysisApp', ['analysisApp.filters', 'analysisApp.services', 'analysisApp.directives']).
  config(['$routeProvider', function($routeProvider) {
	$routeProvider.
	when('/jenkinsserver', {controller:JenkinsServerListCtrl, templateUrl:'partials/jenkinsserver/list.html'}).
	when('/jenkinsserver/edit/:id', {controller:JenkinsServerEditCtrl, templateUrl:'partials/jenkinsserver/detail.html'}).
	when('/jenkinsserver/new', {controller:JenkinsServerCreateCtrl, templateUrl:'partials/jenkinsserver/detail.html'}).
	 
    when('/view1', {templateUrl: 'partials/partial1.html', controller: MyCtrl1}).
    when('/view2', {templateUrl: 'partials/partial2.html', controler: MyCtrl2}).
    otherwise({redirectTo: '/view1'});
  }]);
