(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('RechargesController', RechargesController);

    RechargesController.$inject = ['Recharges'];

    function RechargesController(Recharges) {

        var vm = this;

        vm.recharges = [];

        loadAll();

        function loadAll() {
            Recharges.query(function(result) {
                vm.recharges = result;
                vm.searchQuery = null;
            });
        }
    }
})();
