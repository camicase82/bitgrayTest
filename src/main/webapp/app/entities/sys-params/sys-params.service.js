(function() {
    'use strict';
    angular
        .module('kiwiCellApp')
        .factory('SysParams', SysParams);

    SysParams.$inject = ['$resource'];

    function SysParams ($resource) {
        var resourceUrl =  'api/sys-params/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
