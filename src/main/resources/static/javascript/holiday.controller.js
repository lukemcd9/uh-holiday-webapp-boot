"use strict";
/*global holidayApp*/

function HolidayJsController($scope, dataProvider) {
    var url = "api/holidays";
    $scope.years = [];
    $scope.orderByField = "observedDateFull.toEpochDay";
    $scope.reverseSort = false;
    $scope.holidays = [];
    $scope.holidayArrow = " ";
    $scope.observedArrow = "<i class='fa fa-chevron-down' />"; //or <i class='fa fa-arrow-down' /> for whichever is default
    $scope.officialArrow = " ";

    $scope.init = function() {
        var date = new Date();
        var year = date.getFullYear();
        $scope.yearCode = year.toString();
        $scope.years = [];
        $scope.years.push(year);
        $scope.loadData();
    }

    $scope.loadData = function() {
        dataProvider.loadData(function(d) {
            $scope.holidays = d.data;
            $scope.holidays.forEach(function(h) {
                $scope.holiday = h;
                var y = parseInt(h.year, 10);
                if ($scope.years.indexOf(y) < 0) {
                    $scope.years.push(y);
                }
            });
            $scope.years.sort(function(a, b) {
                return b - a;
            });
        }, url);
    }

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
    }

    $scope.sortBy = function(column) {
        $scope.orderByField = column;
        $scope.reverseSort = !$scope.reverseSort;
    }

    $scope.showHoliday = function(holiday) {
        $scope.holiday = holiday;
        console.log('Holiday: ' + holiday.description);
        $('#holiday').modal();
    }
    $scope.showHolidayArrow = function() {
        if ($scope.reverseSort) {
            $scope.holidayArrow = "<i class='fa fa-chevron-up'/>";
            //$scope.holidayArrow= "<i class='fa fa-arrow-up' />";
        }
        else {
            $scope.holidayArrow = "<i class='fa fa-chevron-down'/>";
            //$scope.holidayArrow= "<i class='fa fa-arrow-down' />";
        }
        $scope.observedArrow = " ";
        $scope.officialArrow = " ";
    }

    $scope.showObservedArrow = function() {
        if ($scope.reverseSort) {
            $scope.observedArrow = "<i class='fa fa-chevron-up'/>";
            //$scope.observedArrow= "<i class='fa fa-arrow-up' />";
        }
        else {
            $scope.observedArrow = "<i class='fa fa-chevron-down'/>";
            //$scope.observedArrow= "<i class='fa fa-arrow-down' />";
        }
        $scope.holidayArrow = " ";
        $scope.officialArrow = " ";
    }

    $scope.showOfficialArrow = function() {
        if ($scope.reverseSort) {
            $scope.officialArrow = "<i class='fa fa-chevron-up'/>";
            //$scope.officialArrow= "<i class='fa fa-arrow-up' />";
        }
        else {
            $scope.officialArrow = "<i class='fa fa-chevron-down'/>";
            //$scope.officialArrow= "<i class='fa fa-arrow-down' />";
        }
        $scope.observedArrow = " ";
        $scope.holidayArrow = " ";
    }

}

holidayApp.controller("HolidayJsController", HolidayJsController);


