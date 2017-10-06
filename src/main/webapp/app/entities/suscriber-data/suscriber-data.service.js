(function() {
    'use strict';
    angular
        .module('kiwiCellApp')
        .factory('SuscriberData', SuscriberData);

    SuscriberData.$inject = ['$resource'];

    function SuscriberData ($resource) {
        var resourceUrl =  'api/suscriber-data/:id';

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
