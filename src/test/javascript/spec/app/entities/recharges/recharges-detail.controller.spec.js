'use strict';

describe('Controller Tests', function() {

    describe('Recharges Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRecharges, MockSuscriberData, MockPromotions;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRecharges = jasmine.createSpy('MockRecharges');
            MockSuscriberData = jasmine.createSpy('MockSuscriberData');
            MockPromotions = jasmine.createSpy('MockPromotions');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Recharges': MockRecharges,
                'SuscriberData': MockSuscriberData,
                'Promotions': MockPromotions
            };
            createController = function() {
                $injector.get('$controller')("RechargesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'kiwiCellApp:rechargesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
