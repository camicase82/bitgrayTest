(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .controller('SysParamsController', SysParamsController);

    SysParamsController.$inject = ['SysParams'];

    function SysParamsController(SysParams) {

        var vm = this;

        vm.sysParams = [];

        loadAll();

        function loadAll() {
            SysParams.query(function(result) {
                vm.sysParams = result;
                vm.searchQuery = null;
            });
        }
    }
})();
