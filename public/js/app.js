'use strict';

// Declare app level module which depends on filters, and services
angular.module(
		'analysisApp',
		[ 'ngRoute', 'analysisApp.rootScopeInitializer', 'analysisApp.filters', 'analysisApp.services',
				'analysisApp.directives', 'ui.bootstrap.alert', 'ui.bootstrap.modal', 'ui.bootstrap.tooltip', 'ui.bootstrap.dropdownToggle']).config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/dashboard', {
				controller : DashboardCtrl,
				templateUrl : 'partials/dashboard/dashboard.html'
			}).
			
			when('/panel', {
				controller : PanelCtrl,
				templateUrl : 'partials/dashboard/panel.html'
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
			
			when('/user', {
				controller : UserListCtrl,
				templateUrl : 'partials/user/list.html'
			}).when('/user/edit/:id', {
				controller : UserEditCtrl,
				templateUrl : 'partials/user/detail.html'
			}).when('/user/new', {
				controller : UserCreateCtrl,
				templateUrl : 'partials/user/detail.html'
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
