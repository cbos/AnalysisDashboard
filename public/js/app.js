'use strict';

// Declare app level module which depends on filters, and services
angular.module(
		'analysisApp',
		[ 'analysisApp.filters', 'analysisApp.services',
				'analysisApp.directives' ]).config(
		[ '$routeProvider', function($routeProvider, Computer) {
			$routeProvider.when('/dashboard', {
				controller : DashboardCtrl,
				templateUrl : 'partials/dashboard/dashboard.html'
			}).

			when('/jenkinsserver', {
				controller : JenkinsServerListCtrl,
				templateUrl : 'partials/jenkinsserver/list.html'
			}).when('/jenkinsserver/edit/:id', {
				controller : JenkinsServerEditCtrl,
				templateUrl : 'partials/jenkinsserver/detail.html'
			}).when('/jenkinsserver/new', {
				controller : JenkinsServerCreateCtrl,
				templateUrl : 'partials/jenkinsserver/detail.html'
			}).

			when('/computer', {
				controller : ComputerListCtrl,
				templateUrl : 'partials/computer/list.html'
			}).when('/computer/edit/:id', {
				controller : ComputerEditCtrl,
				templateUrl : 'partials/computer/detail.html'
			}).

			otherwise({
				redirectTo : '/dashboard'
			});
		} ]);
