'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.
var serviceModule = angular.module('analysisApp.services', [ 'ngResource' ]);
serviceModule.value('version', '0.2');

serviceModule.factory('JenkinsServer', function($resource, $http, $rootScope) {
	var JenkinsServer = $resource('/jenkinsserver/:id', {
		id : '@id'
	});

	JenkinsServer.prototype.analyzeServer = function() {
		var js = this;
		js.isBusyAnalyzing = true;
		$http.get('/jenkinsserver/' + this.id + '/analyzeServer').success(
				function(data, status, headers, config) {
					js.isBusyAnalyzing = false;
				}).error(function(data, status, headers, config) {
			js.isBusyAnalyzing = false;

			$rootScope.addAlert("error", "Analysis of the server failed", "HTML status code:" + status, data);
		});
	};

	JenkinsServer.prototype.isAnalyzing = function() {
		return !!this.isBusyAnalyzing;
	}

	return JenkinsServer;
});

serviceModule.factory('Computer', function($resource) {
	var Computer = $resource('/computer/:id', {
		id : '@id'
	});
	return Computer;
});

serviceModule.factory('Job', function($resource) {
	var Job = $resource('/job/:id', {
		id : '@id'
	});
	return Job;
});

serviceModule.factory('User', function($resource) {
	var Job = $resource('/user/:id', {
		id : '@id'
	});
	return Job;
});

serviceModule.factory('Task', function($resource) {
	var Job = $resource('/task/:id', {
		id : '@id'
	});
	return Job;
});

serviceModule.factory('AnalyzerWebSocket', function($location) {
	var onOpenWebSocket, onCloseWebSocket, onMessageWebSocket;
	var location = "ws://" + $location.host() + ":" + $location.port()
			+ "/websocket"
	var ws = new WebSocket(location);
	ws.onopen = function() {
		if (onOpenWebSocket !== undefined) {
			onOpenWebSocket();
		}
	};
	ws.onclose = function() {
		if (onCloseWebSocket !== undefined) {
			onCloseWebSocket();
		}
	};
	ws.onmessage = function(m) {
		if (onMessageWebSocket !== undefined) {
			onMessageWebSocket(m);
		}
	};

	return {
		onOpen : function(handler) {
			onOpenWebSocket = handler;
		},
		onClose : function(handler) {
			onCloseWebSocket = handler;
		},
		onMessage : function(handler) {
			onMessageWebSocket = handler;
		}
	};
});

