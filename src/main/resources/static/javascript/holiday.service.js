"use strict";
/*global holidayApp*/

holidayApp.factory("dataProvider", function($http, $log) {
    return {
        loadData: function(callback, url) {
            $http.get(encodeURI(url))
                 .success(callback)
                 .error(function(data, status) {
                     $log.error("Error in dataProvider; status: ", status);
                 });
        }
    };
});
