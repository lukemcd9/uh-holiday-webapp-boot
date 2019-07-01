"use strict";
/*global holidayApp*/

holidayApp.filter("html", function($sce) {
    return function (val) {
        return $sce.trustAsHtml(val);
    };
});