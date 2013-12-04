'use strict';

angular.module('analysisApp.rootScopeInitializer', []).run(function($rootScope, $modal, Task, Failure, TestMethod, AnalyzerWebSocket) 
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
				$rootScope.dialogOpen = true;
				var modalInstance = $modal.open({
					templateUrl : 'partials/job/jobdetails.html',
					controller : JobDetailsController,
					resolve : {
						job : function() {
							return job;
						}
					}
				});

				modalInstance.result.then(function() {
					$rootScope.dialogOpen = false;
				});
			}
			
			$rootScope.showTask = function(task)
			{
				$rootScope.dialogOpen = true;
				var modalInstance = $modal.open({
					templateUrl : 'partials/task/taskdetails.html',
					controller : TaskDetailsController,
					resolve : {
						task : function() {
							return task;
						}
					}
				});

				modalInstance.result.then(function() {
					$rootScope.dialogOpen = false;
				});
			}
			
			$rootScope.showIssue = function(issue)
			{
				$rootScope.dialogOpen = true;
				var modalInstance = $modal.open({
					templateUrl : 'partials/issue/issuedetails.html',
					controller : IssueDetailsController,
					resolve : {
						issue : function() {
							return issue;
						}
					}
				});

				modalInstance.result.then(function() {
					$rootScope.dialogOpen = false;
				});
			}
			
			$rootScope.showHistoryOfFailure = function(failure)
			{
				TestMethod.forFailure(failure).then(function(response){
					$rootScope.showHistory(new TestMethod(response.data));
				});
			}
			
			$rootScope.showHistory = function(testmethod)
			{
				$rootScope.dialogOpen = true;
				var modalInstance = $modal.open({
					templateUrl : 'partials/testmethod/history.html',
					controller : TestMethodHistoryController,
					resolve : {
						testmethod : function() {
							return testmethod;
						}
					}
				});
				modalInstance.result.then(function() {
					$rootScope.dialogOpen = false;
				});
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
			
			$rootScope.linkComputerTask = function(computer) {
				var tasks = $rootScope.dashboardController.tasks;
				for ( var i = 0; i < tasks.length; i++) {
					var task = tasks[i];
					if(task.type=="computertask")
					{
						if(task.computerId == computer.id)
						{
							computer.__task = task;
							return true;
						}
					}
				}
				computer.__task = null;
				return false;
			}
			
			$rootScope.linkJobTask = function(job) {
				
				job.__task = null;
				angular.forEach($rootScope.dashboardController.tasks, function(task)
				{
					angular.forEach(task.relatedJobs, function(relatedJob) {
						if(relatedJob.id == job.id)
						{
							job.__task = task;
						}
				    });
				});
				return job.__task != null;
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
				
				var newJobTask = {'type':'jobtask', 'relatedJobs': [{'id':job.id, 'name': job.name}] ,'details':details};
				updateJobName(newJobTask);
				Task.save(newJobTask, function(task) {
					if($rootScope.dashboardController)
					{
						$rootScope.dashboardController.tasks.push(task);
					}
				});
			}
			
			function updateJobName(jobTask)
			{
				var jobs = "";
				var count = 0;
				angular.forEach(jobTask.relatedJobs, function(relatedJob) {
					count++;
					if(count>1)
					{
						jobs += ", "
					}
					jobs += relatedJob.name
			    });
				jobTask.summary = "Investigate failure(s) of " + jobs
			}
			
			$rootScope.connectTask = function(jobTask, job, link)
			{
				if(link)
				{
					jobTask.relatedJobs.push({'id':job.id, 'name': job.name});
				}
				else
				{
					var relatedIndexToDelete = -1;
					angular.forEach(jobTask.relatedJobs, function(relatedJob) {
						if(relatedJob.id == job.id)
						{
							relatedIndexToDelete = jobTask.relatedJobs.indexOf(relatedJob);
						}
				    });
					if(relatedIndexToDelete>-1)
					{
						jobTask.relatedJobs.splice(relatedIndexToDelete, 1)
					}
				}
				updateJobName(jobTask);
				$rootScope.change(jobTask);
			}
			
			$rootScope.createIssue = function() {
				$rootScope.showIssue({'type':""});
			}
			
			$rootScope.connectIssue = function(failure, issue) {
				failure.issue = issue;
				Failure.save(failure);
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
			
			AnalyzerWebSocket.onMessage(function(m) {
				$rootScope.$apply(function() {
					var status = angular.fromJson(m.data);
					$rootScope.analyzerStatus = status;
					if(!status.isExecuting && $rootScope.dashboardController)
					{
						$rootScope.dashboardController.analyzerFinished();
					}
				})
			});
		});

/* Controllers */

function DashboardCtrl($scope, $rootScope, $timeout,  Computer, Issue, Job, Task, User) {
	
	$scope.reload = function()
	{
		//don't reload if there is modal view open, then you might loss data
		if(!$rootScope.dialogOpen)
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
		var newComputerTask = {'summary': "Bring " + computer.displayName + " back online", 'type':'computertask', 'computerId': computer.id};
		Task.save(newComputerTask, function(task) {
			$scope.tasks.push(task);
		});
	}
	
	$scope.changeAssignee = function(task, user) {
		task.assignee = user;
		task.$save();
	}
	
	$scope.analyzerFinished = function()
	{
		//do nothing
	}
}

function PanelCtrl($scope, $rootScope, $timeout, Computer, Issue, Job, Task, User) {
	
	$scope.reload = function()
	{
		$scope.computers = Computer.query();
		$scope.jobs = Job.unstableJobs();
		$scope.tasks = Task.todayList();
		$scope.issues = Issue.todayList();
	}
	$timeout($scope.reload, 0);
	$rootScope.dashboardController = $scope;
	
	$scope.analyzerFinished = function()
	{
		$scope.reload();
	}
}

function JobEditCtrl($scope, $routeParams, Job) {
	Job.get({
		id : $routeParams.id
	}, function(job) {
		$scope.job = job;
		
		Job.history(job).then(function(response) {
			$scope.jobHistory = response.data;
		});
	});
}

var JobDetailsController = function($scope, $rootScope, $modalInstance, Job, job) {
	$scope.job = job

	Job.history(job).then(function(response) {
		$scope.jobHistory = response.data;
	});
	$scope.close = function() {
		$modalInstance.close();
	}
}

var TaskDetailsController = function($scope, $rootScope, $modalInstance, task) {
	$scope.taskToEdit = task
	
	$scope.close = function() {
		$rootScope.change($scope.taskToEdit);
		$modalInstance.close();
	}

	$scope.taskDone = function() {
		$scope.taskToEdit.done = !$scope.taskToEdit.done;
		$scope.close();
	}
	
	$scope.taskRemove = function() {
		$rootScope.destroy($rootScope.dashboardController.tasks, $scope.taskToEdit);
		$modalInstance.dismiss('removed');
	}
}

var IssueDetailsController = function($scope, $rootScope, Issue, $modalInstance, issue) {
	$scope.issueToEdit = issue
	$scope.close = function() {
		if($scope.issueToEdit.id)
		{
			$rootScope.change($scope.issueToEdit);
		}
		else
		{
			Issue.save($scope.issueToEdit, function(savedIssue) {
				if($rootScope.dashboardController)
				{
					$rootScope.dashboardController.issues.push(savedIssue);
				}
			});
		}
		$modalInstance.close();
	}

	$scope.issueRemove = function() {
		if($scope.issueToEdit.id)
		{
			$rootScope.destroy($rootScope.dashboardController.issues, $scope.issueToEdit);
		}
		$modalInstance.dismiss('removed');
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

function RandomFailureListCtrl($scope, TestMethod) {
	$scope.totalPages = 0;
	$scope.totalRandomFailures = 0;
	$scope.pageSize = 10;
	
	// default criteria that will be sent to the server
	$scope.filterCriteria = {
		pageNumber : 1,
	};

	// The function that is responsible of fetching the result from the server
	// and setting the grid to the new result
	$scope.fetchResult = function() {
		return TestMethod.randomFailureList($scope.filterCriteria.pageNumber).then(function(response) {
			var data = response.data;
			$scope.failures = data.failures;
			$scope.totalPages = data.totalPages;
			$scope.totalRandomFailures = data.totalRandomFailures;
			$scope.pageSize = data.pageSize;
		}, function() {
			$scope.failures = [];
			$scope.totalPages = 0;
			$scope.totalRandomFailures = 0;
		});
	};

	//called when navigate to another page in the pagination
	$scope.selectPage = function(page) {
		$scope.filterCriteria.pageNumber = page;
		$scope.fetchResult();
	};

	//manually select a page to trigger an ajax request to populate the grid on page load
	$scope.selectPage(1);
}

var TestMethodHistoryController = function($scope, $rootScope, TestMethod, $modalInstance, testmethod) {
	$scope.testmethod = testmethod
	$scope.close = function() {
		$modalInstance.close();
	}
	
	$scope.totalPages = 0;
	$scope.totalFailures = 0;
	$scope.pageSize = 10;
	
	// default criteria that will be sent to the server
	$scope.filterCriteria = {
		pageNumber : 1,
	};
	
	$scope.fetchResult = function() {
		TestMethod.loadFailures($scope.testmethod, $scope.filterCriteria.pageNumber).then(function(response) {
			var data = response.data;
			$scope.failures = data.failures;
			$scope.totalPages = data.totalPages;
			$scope.totalFailures = data.totalFailures;
			$scope.pageSize = data.pageSize;
		}, function() {
			$scope.failures = [];
			$scope.totalPages = 0;
			$scope.totalFailures = 0;
		});
	};
	
	//called when navigate to another page in the pagination
	$scope.selectPage = function(page) {
		$scope.filterCriteria.pageNumber = page;
		$scope.fetchResult();
	};

	//manually select a page to trigger an ajax request to populate the grid on page load
	$scope.selectPage(1);
}