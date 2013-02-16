'use strict';

/* Directives */

angular.module('analysisApp.directives', []).directive('appVersion',
		[ 'version', function(version) {
			return function(scope, elm, attrs) {
				elm.text(version);
			};
		} ]);

angular.module("ui.bootstrap.alert", []).directive('alert', function() {
	return {
		restrict : 'EA',
		templateUrl : 'partials/directive/alert.html',
		transclude : true,
		replace : true,
		scope : {
			type : '=',
			close : '&'
		}
	};
});