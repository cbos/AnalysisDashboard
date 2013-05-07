'use strict';

angular.module('analysisApp.rootScopeInitializer', []).run(function($rootScope, Task, Failure) 
		  {
			$rootScope.alerts = [];

			$rootScope.addAlert = function(type, info, status, data)
			{
				$rootScope.alerts.push({type: type, info: info, status: status, data:data});
			};

			$rootScope.closeAlert = function(index)
			{
			     $rootScope.alerts.splice(index, 1);
			};
			
			$rootScope.change = function(entity) {
			    entity.$save();
			};

			$rootScope.destroy = function(entityArray, entity) {
				var index = entityArray.indexOf(entity);
				entity.$remove(function() {
					entityArray.splice(index, 1);
				});
			};
			
			$rootScope.jobTypes = [
			                {name:'Full Build', value:'fullbuild', order:10},
			                {name:'Install', value:'install', order:20},
			                {name:'Upgrade', value:'upgrade', order:30},
			                {name:'Sync-merge', value:'syncmerge', order:40},
			                {name:'Drop-merge', value:'dropmerge', order:50},
			                {name:'Quick build', value:'quick-build', order:60},
			                {name:'Regression test', value:'regression-test', order:70},
			                {name:'Loadtests', value:'loadtests', order:80},
			                {name:'Misc', value:'misc', order:90},
			                {name:'Unknown', value:'', order:100}
				            ];
			
			$rootScope.issueTypes = [
					                {name:'Product', value:'product'},
					                {name:'Testcase/Testutils', value:'testcase'},
					                {name:'Configuration', value:'configuration'},
					                {name:'Infrastructure - Jenkins', value:'jenkins'},
					                {name:'Infrastructure - Slave setup', value:'slave'},
					                {name:'Unknown', value:''}
						            ];
			
			$rootScope.showBuild = function(job)
			{
				$rootScope.jobToShow = job;
			}
			
			$rootScope.showTask = function(task, tasks)
			{
				$rootScope.taskToShow = task;
				$rootScope.taskList = tasks;
			}
			
			$rootScope.showIssue = function(issue)
			{
				$rootScope.issueToShow = issue;
			}
			
			$rootScope.imageJobStatus = function(job)
			{
				if(job)
				{
					var img = "";
					switch (job.status)
					{
					case "UNSTABLE":
						img = "yellow";
					  break;
					case "STABLE":
						img = "blue";
					  break;
					case "FAILED":
						img = "red";
					  break;
					default:
						img = "grey";
					}
					if(job.building)
					{
						return "img/" + img + "_anime.gif";
					}
					return "img/" + img + ".png";
				}
			}
			
			$rootScope.createJobTask = function(job) {
				var details = "";
				if(job.lastBuild)
				{	
					details = "Investigate failure(s) of buildnr " + job.lastBuild.buildNumber  + "<BR>\n"; 
					angular.forEach(job.lastBuild.failures, function(failure, key){
						if(!failure.testMethodName)
						{
							details += "Failure summary: " + failure.summary;
						}
					});
				}
				
				var newJobTask = {'summary': "Investigate failure(s) of " + job.name, 'details':details};
				Task.save(newJobTask, function(task) {
					if($rootScope.dashboardController)
					{
						$rootScope.dashboardController.tasks.push(task);
					}
				});
			}
			
			$rootScope.createIssue = function() {
				$rootScope.issueToShow = {'type':""};
			}
			
			$rootScope.getRuns = function(job) {
				if(job && job.lastBuild)
				{
					if(job.lastBuild.childBuilds.length >0)
					{
						return job.lastBuild.childBuilds;
					}
					return [job.lastBuild];
				}
				return null;
			}
			
			$rootScope.toggleRandomFailure = function(failure) {
				failure.randomFailure = !failure.randomFailure;
				Failure.save(failure);
			}
		});

/* Controllers */

function DashboardCtrl($scope, $rootScope, $timeout,  Computer, Issue, Job, Task, User, AnalyzerWebSocket) {
	
	$scope.reload = function()
	{
		//don't reload if there is modal view open, then you might loss data
		if(!$rootScope.jobToShow && !$rootScope.taskToShow && !$rootScope.issueToShow)
		{
			$scope.computers = Computer.query();
			$scope.jobs = Job.unstableJobs();
			$scope.tasks = Task.todayList();
			$scope.issues = Issue.todayList();
		}
		
		$timeout($scope.reload, 60000);
	}
	$timeout($scope.reload, 0);
	$rootScope.users = User.query();
	$rootScope.dashboardController = $scope;
	
	$scope.stopWatching = function(job) {
		job.watch = false;
		job.$save();
	};
	
	$scope.imageComputerStatus = function(computer)
	{
		if(computer.offline)
		{
			return "computer-x.png";
		}
		return "computer.png";
	}
	
	$scope.createComputerTask = function(computer) {
		var newComputerTask = {'summary': "Bring " + computer.displayName + " back online"};
		Task.save(newComputerTask, function(task) {
			$scope.tasks.push(task);
		});
	}
	
	$scope.changeAssignee = function(task, user) {
		task.assignee = user;
		task.$save();
	}
	
	AnalyzerWebSocket.onMessage(function(m) {
		$scope.$apply(function() {
			$scope.analyzerStatus = angular.fromJson(m.data);
		})
	});
}

function PanelCtrl($scope, $rootScope, $timeout, Computer, Issue, Job, Task, User, AnalyzerWebSocket) {
	
	$scope.reload = function()
	{
		$scope.computers = Computer.query();
		$scope.jobs = Job.unstableJobs();
		$scope.tasks = Task.todayList();
		$scope.issues = Issue.todayList();
	}
	$timeout($scope.reload, 0);
	
	AnalyzerWebSocket.onMessage(function(m) {
		$scope.$apply(function() {
			$scope.analyzerStatus = angular.fromJson(m.data);
			if(!$scope.analyzerStatus.isExecuting)
			{
				$scope.reload();
			}
		})
	});
}

var JobDetailsController = function($scope, $rootScope) {
	$scope.close = function() {
		$rootScope.jobToShow = null;
	}
}

var TaskDetailsController = function($scope, $rootScope) {
	$scope.close = function() {
		$rootScope.change($rootScope.taskToShow);
		$rootScope.taskToShow = null;
	}

	$scope.taskDone = function() {
		$rootScope.taskToShow.done = !$rootScope.taskToShow.done;
		$scope.close();
	}
	
	$scope.taskRemove = function() {
		$rootScope.destroy($rootScope.taskList, $rootScope.taskToShow);
		$rootScope.taskToShow = null;
	}
}

var IssueDetailsController = function($scope, $rootScope, Issue) {
	$scope.close = function() {
		if($rootScope.issueToShow.id)
		{
			$rootScope.change($rootScope.issueToShow);
		}
		else
		{
			Issue.save($rootScope.issueToShow, function(savedIssue) {
				if($rootScope.dashboardController)
				{
					$rootScope.dashboardController.issues.push(savedIssue);
				}
			});
		}
		$rootScope.issueToShow = null;
	}

	$scope.issueRemove = function() {
		$rootScope.destroy($rootScope.dashboardController.issues, $rootScope.issueToShow);
		$rootScope.issueToShow = null;
	}
}

function ComputerListCtrl($scope, Computer) {
	$scope.computers = Computer.query();
}

function JobListCtrl($scope, Job) {
	$scope.jobs = Job.query();
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

function UserListCtrl($scope, User) {
	$scope.users = User.query();
}

function UserCreateCtrl($scope, $location, User) {
	$scope.save = function() {
		User.save($scope.user, function(user) {
			$location.path('/user/edit/' + user.id);
		});
	}
}

function UserEditCtrl($scope, $location, $routeParams, $http,
		User) {
	var self = this;

	User.get({
		id : $routeParams.id
	}, function(user) {
		self.original = user;
		$scope.user = new User(self.original);
	});

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.user);
	}

	$scope.destroy = function() {
		self.original.$remove(function() {
			$location.path('/user');
		});
	};

	$scope.save = function() {
		$scope.user.$save(function() {
			$location.path('/user');
		});
	};
}