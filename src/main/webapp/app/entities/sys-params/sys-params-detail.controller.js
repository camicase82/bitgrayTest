(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('SysParamsDetailController', SysParamsDetailController);

    SysParamsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SysParams'];

    function SysParamsDetailController($scope, $rootScope, $stateParams, previousState, entity, SysParams) {
        var vm = this;

        vm.sysParams = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kiwiCellApp:sysParamsUpdate', function(event, result) {
            vm.sysParams = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
