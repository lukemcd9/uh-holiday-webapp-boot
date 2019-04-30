describe("HolidayJsController", function() {

    beforeEach(module('holidayApp'));

    var scope;
    var controller;
    var dataProvider;

    beforeEach(inject(function($rootScope, $controller, dataProvider) {
        scope = $rootScope.$new();
        controller = $controller('HolidayJsController', {
            $scope: scope,
            dataProvider: dataProvider
        });
    }));

    it("checkInitFunction", function() {        
        spyOn(scope, "loadData").and.callFake(function() {            
            scope.holidays.push({
                "id": 88,              
                "year": 2017,
                "description": "New Year's Day",
                "observedDate": "2017-01-02",
                "officialDate": "2017-01-01"              
            });
            scope.holidays.push({
                "id": 141,              
                "year": 2020,
                "description": "Thanksgiving",
                "observedDate": "2020-11-26",
                "officialDate": "2020-11-26"              
            });
            scope.holidays.push({
                "id": 142,              
                "year": 2020,
                "description": "Christmas",
                "observedDate": "2020-12-25",
                "officialDate": "2020-12-25"              
            });
        }); 

        expect(controller).toBeDefined();        
        expect(scope.holidays).toBeDefined();
        expect(scope.holidays.length).toEqual(0);
        
        // What we are testing:
        scope.init();

        expect(scope.loadData).toHaveBeenCalled();
        expect(scope.holidays).toBeDefined();
        expect(scope.holidays.length).toEqual(3);
        
        expect(scope.holidays[0].id).toEqual(88);        
        expect(scope.holidays[0].year).toEqual(2017);
        expect(scope.holidays[0].description).toEqual("New Year's Day");       
        expect(scope.holidays[0].observedDate).toEqual("2017-01-02");       
        expect(scope.holidays[0].officialDate).toEqual("2017-01-01");       
                
        expect(scope.holidays[1].id).toEqual(141);        
        expect(scope.holidays[1].year).toEqual(2020);
        expect(scope.holidays[1].description).toEqual("Thanksgiving");       
        expect(scope.holidays[1].observedDate).toEqual("2020-11-26");       
        expect(scope.holidays[1].officialDate).toEqual("2020-11-26");       
        
        expect(scope.holidays[2].id).toEqual(142);
        expect(scope.holidays[2].year).toEqual(2020);
        expect(scope.holidays[2].description).toEqual("Christmas");
        expect(scope.holidays[2].observedDate).toEqual("2020-12-25");       
        expect(scope.holidays[2].officialDate).toEqual("2020-12-25");       
    });

});
