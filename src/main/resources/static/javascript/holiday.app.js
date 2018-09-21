"use strict";
var holidayApp = angular.module("holidayApp", []);

holidayApp.factory("dataProvider", function($http) {
    return {
        loadData: function(callback, url) {
            $http.get(encodeURI(url))
                 .success(callback)
                 .error(function(data, status) {
                     console.log("Error in dataProvider; status: ", status);
                 });
        }
    };
});

function HolidayJsController($scope, dataProvider) {
    var url = "api/holidays";
    $scope.years = [];

    $scope.init = function() {
        var date = new Date();
        var year = date.getFullYear();
        $scope.yearCode = year.toString();
        $scope.years = [];
        $scope.years.push(year);

        dataProvider.loadData(function(d) {
            $scope.holidays = d.data;
            $scope.holidays.forEach(function(h) {
                var y = parseInt(h.year, 10);
                if ($scope.years.indexOf(y) < 0) {
                    $scope.years.push(y);
                }
            });
            $scope.years.sort(function(a, b) {
                return b - a;
            });
        }, url);
    };

    $scope.searchFilter = function() {
        return function(e) {
            var text = $scope.searchFor;
            if (angular.isUndefined(text)) {
                return true;
            }
            text = text.trim().toLowerCase();
            if (text === "") {
                return true;
            } else if (e.description.toLowerCase().indexOf(text) !== -1) {
                return true;
            } else if (e.observedDateFull.toLowerCase().indexOf(text) !== -1) {
                return true;
            } else if (e.officialDateFull.toString().indexOf(text) !== -1) {
                return true;
            }
            return false;
        };
    };
}

holidayApp.controller("HolidayJsController", HolidayJsController);
