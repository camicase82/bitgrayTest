(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('PromotionsDialogController', PromotionsDialogController);

    PromotionsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Promotions', 'Recharges'];

    function PromotionsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Promotions, Recharges) {
        var vm = this;

        vm.promotions = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.recharges = Recharges.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.promotions.id !== null) {
                Promotions.update(vm.promotions, onSaveSuccess, onSaveError);
            } else {
                Promotions.save(vm.promotions, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kiwiCellApp:promotionsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.configurationDate = false;
        vm.datePickerOpenStatus.application = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
