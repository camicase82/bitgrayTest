(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('SuscriberDataDialogController', SuscriberDataDialogController);

    SuscriberDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SuscriberData'];

    function SuscriberDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SuscriberData) {
        var vm = this;

        vm.suscriberData = entity;
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
            if (vm.suscriberData.id !== null) {
                SuscriberData.update(vm.suscriberData, onSaveSuccess, onSaveError);
            } else {
                SuscriberData.save(vm.suscriberData, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kiwiCellApp:suscriberDataUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
