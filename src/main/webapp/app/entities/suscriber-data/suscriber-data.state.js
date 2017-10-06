(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('suscriber-data', {
            parent: 'entity',
            url: '/suscriber-data',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kiwiCellApp.suscriberData.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/suscriber-data/suscriber-data.html',
                    controller: 'SuscriberDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('suscriberData');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('suscriber-data-detail', {
            parent: 'suscriber-data',
            url: '/suscriber-data/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kiwiCellApp.suscriberData.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/suscriber-data/suscriber-data-detail.html',
                    controller: 'SuscriberDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('suscriberData');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SuscriberData', function($stateParams, SuscriberData) {
                    return SuscriberData.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'suscriber-data',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('suscriber-data-detail.edit', {
            parent: 'suscriber-data-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/suscriber-data/suscriber-data-dialog.html',
                    controller: 'SuscriberDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SuscriberData', function(SuscriberData) {
                            return SuscriberData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('suscriber-data.new', {
            parent: 'suscriber-data',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/suscriber-data/suscriber-data-dialog.html',
                    controller: 'SuscriberDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firebReference: null,
                                phoneNumber: null,
                                balance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('suscriber-data', null, { reload: 'suscriber-data' });
                }, function() {
                    $state.go('suscriber-data');
                });
            }]
        })
        .state('suscriber-data.edit', {
            parent: 'suscriber-data',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/suscriber-data/suscriber-data-dialog.html',
                    controller: 'SuscriberDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SuscriberData', function(SuscriberData) {
                            return SuscriberData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('suscriber-data', null, { reload: 'suscriber-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('suscriber-data.delete', {
            parent: 'suscriber-data',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/suscriber-data/suscriber-data-delete-dialog.html',
                    controller: 'SuscriberDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SuscriberData', function(SuscriberData) {
                            return SuscriberData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('suscriber-data', null, { reload: 'suscriber-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
