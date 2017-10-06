(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('PromotionsDetailController', PromotionsDetailController);

    PromotionsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Promotions', 'Recharges'];

    function PromotionsDetailController($scope, $rootScope, $stateParams, previousState, entity, Promotions, Recharges) {
        var vm = this;

        vm.promotions = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kiwiCellApp:promotionsUpdate', function(event, result) {
            vm.promotions = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
