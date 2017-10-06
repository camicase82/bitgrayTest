(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('SuscriberDataDeleteController',SuscriberDataDeleteController);

    SuscriberDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'SuscriberData'];

    function SuscriberDataDeleteController($uibModalInstance, entity, SuscriberData) {
        var vm = this;

        vm.suscriberData = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SuscriberData.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
