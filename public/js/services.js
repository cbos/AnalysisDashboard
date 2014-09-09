'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.
var serviceModule = angular.module('analysisApp.services', [ 'ngResource' ]);
serviceModule.value('version', '0.4');

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

			$rootScope.addAlert("danger", "Analysis of the server failed", "HTML status code:" + status, data);
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

serviceModule.factory('Job', function($resource, $http) {
	var Job = $resource('/job/:id', {
		id : '@id'
	});
	
	Job.unstableJobs = function() {
        return Job.query({id: 'unstableList'});
    };
    
    Job.history = function(job) {
		return $http.get('/job/' + job.id + '/history')
    };
	return Job;
});

serviceModule.factory('User', function($resource) {
	var User = $resource('/user/:id', {
		id : '@id'
	});
	return User;
});

serviceModule.factory('Task', function($resource) {
	var Task = $resource('/task/:id', {
		id : '@id'
	});
	
	Task.todayList = function() {
        return Task.query({id: 'todayList'});
    };
	return Task;
});

serviceModule.factory('Failure', function($resource, $http) {
	var Failure = $resource('/failure/:id', {
		id : '@id'
	});
	return Failure;
});

serviceModule.factory('TestMethod', function($resource, $http) {
	var TestMethod = $resource('/testmethod/:id', {
		id : '@id'
	});
	TestMethod.forFailure = function(failure) {
		return $http.get('/testmethod/forfailure/' + failure.id)
    };
    
    TestMethod.loadFailures = function(testmethod, pageNumber) {
		return $http.get('/testmethod/' + testmethod.id + '/failures/' + pageNumber);
    };
    
    TestMethod.randomFailureList = function(pageNumber) {
		return $http.get('/testmethod/randomList/' + pageNumber)
    };
	return TestMethod;
});

serviceModule.factory('Issue', function($resource) {
	var Issue = $resource('/issue/:id', {
		id : '@id'
	});
	
	Issue.todayList = function() {
        return Issue.query({id: 'todayList'});
    };
    Issue.prototype.jiraLink = function()
    {
    	return "https://jira.opentext.com/browse/" + this.jira_id;
    };
	return Issue;
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

serviceModule.factory('EclipseIntegration', function($resource, $http, $rootScope) {

	function EclipseIntegration()
	{
	}
	
	EclipseIntegration.url = "http://localhost:58642";
	EclipseIntegration.iconURL = EclipseIntegration.url + "/icon";
	EclipseIntegration._enabled = false;
	
	EclipseIntegration.isEnabled = function() {
		if(!EclipseIntegration._externalCheckDone)
		{
			EclipseIntegration._externalCheckDone = true;
			EclipseIntegration.checkEclipseStarted();
		}
		return EclipseIntegration._enabled;
	};
	
	EclipseIntegration.checkEclipseStarted = function() {
		EclipseIntegration._isBusyAnalyzing = true;
		$http.get(EclipseIntegration.url + '/postevent').success(
				function(data, status, headers, config) {
					EclipseIntegration._isBusyAnalyzing = false;
					EclipseIntegration._enabled = true;
				}).error(function(data, status, headers, config) {
					EclipseIntegration._isBusyAnalyzing = false;
					EclipseIntegration._enabled = false;
		});
	};
	
	EclipseIntegration.sendEvent = function(eventData) {
		$http.post(EclipseIntegration.url + '/postevent', eventData).success(
				function(data, status, headers, config) {
				}).error(function(data, status, headers, config) {
					$rootScope.addAlert("danger", "Eclipse post failed", "HTML status code:" + status, data);
		});
	};

	EclipseIntegration.isAnalyzing = function() {
		return !!EclipseIntegration._isBusyAnalyzing;
	}
	return EclipseIntegration;
});

