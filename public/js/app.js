'use strict';

// Declare app level module which depends on filters, and services
angular.module(
		'analysisApp',
		[ 'analysisApp.rootScopeInitializer', 'analysisApp.filters', 'analysisApp.services',
				'analysisApp.directives', 'ui.bootstrap.alert', 'ui.bootstrap.modal', 'ui.bootstrap.tooltip']).config(
		[ '$routeProvider', function($routeProvider) {
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
			}).

			when('/job', {
				controller : JobListCtrl,
				templateUrl : 'partials/job/list.html'
			}).

			otherwise({
				redirectTo : '/dashboard'
			});
		} ]);
