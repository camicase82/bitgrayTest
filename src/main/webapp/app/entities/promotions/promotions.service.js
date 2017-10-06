(function() {
    'use strict';
    angular
        .module('kiwiCellApp')
        .factory('Promotions', Promotions);

    Promotions.$inject = ['$resource', 'DateUtils'];

    function Promotions ($resource, DateUtils) {
        var resourceUrl =  'api/promotions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.configurationDate = DateUtils.convertLocalDateFromServer(data.configurationDate);
                        data.application = DateUtils.convertLocalDateFromServer(data.application);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.configurationDate = DateUtils.convertLocalDateToServer(copy.configurationDate);
                    copy.application = DateUtils.convertLocalDateToServer(copy.application);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.configurationDate = DateUtils.convertLocalDateToServer(copy.configurationDate);
                    copy.application = DateUtils.convertLocalDateToServer(copy.application);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
