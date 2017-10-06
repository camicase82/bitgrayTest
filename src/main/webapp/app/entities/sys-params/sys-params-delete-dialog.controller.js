(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('SysParamsDeleteController',SysParamsDeleteController);

    SysParamsDeleteController.$inject = ['$uibModalInstance', 'entity', 'SysParams'];

    function SysParamsDeleteController($uibModalInstance, entity, SysParams) {
        var vm = this;

        vm.sysParams = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SysParams.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
