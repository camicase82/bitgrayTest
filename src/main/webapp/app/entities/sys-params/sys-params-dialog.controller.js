(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('SysParamsDialogController', SysParamsDialogController);

    SysParamsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SysParams'];

    function SysParamsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SysParams) {
        var vm = this;

        vm.sysParams = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sysParams.id !== null) {
                SysParams.update(vm.sysParams, onSaveSuccess, onSaveError);
            } else {
                SysParams.save(vm.sysParams, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kiwiCellApp:sysParamsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
