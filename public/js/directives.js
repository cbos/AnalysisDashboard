'use strict';

/* Directives */

var directivesModule = angular.module('analysisApp.directives', []);
directivesModule.directive('appVersion', [ 'version', function(version) {
	return function(scope, elm, attrs) {
		elm.text(version);
	};
} ]);

directivesModule.directive('stopEvent', function () {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            element.bind(attr.stopEvent, function (e) {
                e.stopPropagation();
                e.preventDefault();
            });
        }
    };
 });

directivesModule.directive('calendarGraph', function() {
	return {
		restrict : 'E',
		templateUrl: 'template/calendar-graph/calendar-graph.html',
		link : function(scope, elm, attrs) {
			
			scope.isLoading = true;
			scope.width = 960;
			scope.height = 136;

			var data;
			var cellSize = 17; // cell size
			var days = [ "S", "M", "T", "W", "T", "F", "S" ]; // days

			var day = d3.time.format("%w");
			var week = d3.time.format("%U");
			var year = d3.time.format("%Y");
			var monthName = d3.time.format("%b");
			var dayOfTheMonth = d3.time.format("%d");
			
			scope.$watch(attrs.data, function(value) {
				if(angular.isObject(value))
				{
					data = value;
					drawGrid();
					scope.isLoading = false;
				}
			});

			function drawGrid() {
				
				var svg = d3.select(elm[0].firstChild).selectAll("svg")
					.data(d3.range(1))
					.enter()
					.append("svg")
					.attr("width", scope.width)
					.attr("height", scope.height)
					.attr("class", "calendar-graph")
					.append("g").attr("transform", "translate(" + ((scope.width - cellSize * 53) / 2) + ","	+ (scope.height - cellSize * 7 - 1) + ")");

				// Add days in front
				for ( var x = 0; x < days.length; x++) {
					svg.append("text")
						.style("text-anchor", "middle")
						.attr("class", "wday")
						.attr("dx", "-10")
						.attr("dy", 12 + (x * cellSize))
						.text(days[x]);
				}
				
				var startDate = new Date(data[0][0]);
				var startWeek = week(startDate);
				var startYear = year(startDate);
				// number of weeks in starting year
				var weeksInYear = week(new Date(startYear + "/12/31"));
				
				var relativeWeek = function(date) {
					var actualYear = year(date);
					var actualWeek = week(date);
					return actualWeek - startWeek + weeksInYear	* (actualYear - startYear);
				};
				
				d3.map(data).forEach(function(key, value){
					var date = new Date(value[0]);
					if((key === 0 && dayOfTheMonth(date)<20) || (day(date)===0 && dayOfTheMonth(date)<=7))
					{
						svg.append("text")
							.attr("class", "month")
							.attr("dx", (relativeWeek(date) * cellSize))
							.attr("dy", "-5")
							.text(monthName(date));
					}
				});
				
				var dayStyle = function(data)
				{
					var results = data[1];
					if(results.failed>0)
					{
						return "fill:red";
					}
					if(results.unstable>0)
					{
						return "fill:gold";
					}
					if(results.aborted>0)
					{
						return "fill:gray";
					}
					if(results.stable>0)
					{
						return "fill: #8cc665;";
					}
				};

				var rect = svg.selectAll(".day").data(function(d) {
					return data;
				}).enter()
					.append("rect")
					.attr("class", "day")
					.attr("width", cellSize)
					.attr("height", cellSize)
					.attr("x", function(d) { var date = new Date(d[0]); return relativeWeek(date) * cellSize;})
					.attr("y", function(d) { return day(new Date(d[0])) * cellSize;	})
					.attr("style", function(d) { return dayStyle(d); });
				
				rect.append("title").text(function(d) {
					var tooltip = "";
					tooltip += d[0];
					tooltip += ":";
					var results = d[1];
					tooltip += " \nFailed: " + results.failed;
					tooltip += " \nUnstable: " + results.unstable;
					tooltip += " \nAborted: " + results.aborted;
					tooltip += " \nStable: " + results.stable;
					return tooltip;
				});
			}
		}
	}
});