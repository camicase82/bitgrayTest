(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('RechargesDialogController', RechargesDialogController);

    RechargesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Recharges', 'SuscriberData', 'Promotions'];

    function RechargesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Recharges, SuscriberData, Promotions) {
        var vm = this;

        vm.recharges = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.suscribers = SuscriberData.query({filter: 'recharges-is-null'});
        $q.all([vm.recharges.$promise, vm.suscribers.$promise]).then(function() {
            if (!vm.recharges.suscriber || !vm.recharges.suscriber.id) {
                return $q.reject();
            }
            return SuscriberData.get({id : vm.recharges.suscriber.id}).$promise;
        }).then(function(suscriber) {
            vm.suscribers.push(suscriber);
        });
        vm.promotions = Promotions.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.recharges.id !== null) {
                Recharges.update(vm.recharges, onSaveSuccess, onSaveError);
            } else {
                Recharges.save(vm.recharges, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kiwiCellApp:rechargesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
