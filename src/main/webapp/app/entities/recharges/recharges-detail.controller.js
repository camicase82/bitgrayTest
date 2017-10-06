(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('RechargesDetailController', RechargesDetailController);

    RechargesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Recharges', 'SuscriberData', 'Promotions'];

    function RechargesDetailController($scope, $rootScope, $stateParams, previousState, entity, Recharges, SuscriberData, Promotions) {
        var vm = this;

        vm.recharges = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kiwiCellApp:rechargesUpdate', function(event, result) {
            vm.recharges = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
