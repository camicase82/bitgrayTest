(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('PromotionsController', PromotionsController);

    PromotionsController.$inject = ['Promotions'];

    function PromotionsController(Promotions) {

        var vm = this;

        vm.promotions = [];

        loadAll();

        function loadAll() {
            Promotions.query(function(result) {
                vm.promotions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
