(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('SuscriberDataController', SuscriberDataController);

    SuscriberDataController.$inject = ['SuscriberData'];

    function SuscriberDataController(SuscriberData) {

        var vm = this;

        vm.suscriberData = [];

        loadAll();

        function loadAll() {
            SuscriberData.query(function(result) {
                vm.suscriberData = result;
                vm.searchQuery = null;
            });
        }
    }
})();
