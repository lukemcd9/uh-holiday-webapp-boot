(function() {

    function HolidayJsController($scope, dataProvider) {
        var url = 'api/holidays';
        $scope.years = [];

        $scope.init = function() {
            var date = new Date();
            var year = date.getFullYear();
            $scope.yearCode = year.toString();
            $scope.years = [];
            $scope.years.push(year);

            dataProvider.loadData(function(d) {
                var data = d.data;
                $scope.holidays = data;
                for (var i = 0; i < data.length; i++) {
                    var y = parseInt(data[i].year);
                    if ($scope.years.indexOf(y) < 0) {
                        $scope.years.push(y);
                    }
                }
                $scope.years.sort(function(a, b) {
                    return b - a
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
                if (text == "") {
                    return true;
                } else if (e.observedDateFull.toLowerCase().indexOf(text) != -1) {
                    return true;
                } else if (e.officialDateFull.toString().indexOf(text) != -1) {
                    return true;
                }
                return false;
            };
        }
    }

    holidayApp.controller("HolidayJsController", HolidayJsController);

})();
