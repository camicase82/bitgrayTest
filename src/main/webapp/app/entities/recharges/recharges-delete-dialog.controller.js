(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('RechargesDeleteController',RechargesDeleteController);

    RechargesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Recharges'];

    function RechargesDeleteController($uibModalInstance, entity, Recharges) {
        var vm = this;

        vm.recharges = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Recharges.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
