(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('SuscriberDataDetailController', SuscriberDataDetailController);

    SuscriberDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SuscriberData'];

    function SuscriberDataDetailController($scope, $rootScope, $stateParams, previousState, entity, SuscriberData) {
        var vm = this;

        vm.suscriberData = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kiwiCellApp:suscriberDataUpdate', function(event, result) {
            vm.suscriberData = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
