(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('PromotionsDeleteController',PromotionsDeleteController);

    PromotionsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Promotions'];

    function PromotionsDeleteController($uibModalInstance, entity, Promotions) {
        var vm = this;

        vm.promotions = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Promotions.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
