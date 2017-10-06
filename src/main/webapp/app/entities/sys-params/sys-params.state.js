(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sys-params', {
            parent: 'entity',
            url: '/sys-params',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kiwiCellApp.sysParams.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sys-params/sys-params.html',
                    controller: 'SysParamsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sysParams');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sys-params-detail', {
            parent: 'sys-params',
            url: '/sys-params/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kiwiCellApp.sysParams.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sys-params/sys-params-detail.html',
                    controller: 'SysParamsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sysParams');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SysParams', function($stateParams, SysParams) {
                    return SysParams.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sys-params',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sys-params-detail.edit', {
            parent: 'sys-params-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sys-params/sys-params-dialog.html',
                    controller: 'SysParamsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SysParams', function(SysParams) {
                            return SysParams.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sys-params.new', {
            parent: 'sys-params',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sys-params/sys-params-dialog.html',
                    controller: 'SysParamsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                sValue: null,
                                nValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sys-params', null, { reload: 'sys-params' });
                }, function() {
                    $state.go('sys-params');
                });
            }]
        })
        .state('sys-params.edit', {
            parent: 'sys-params',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sys-params/sys-params-dialog.html',
                    controller: 'SysParamsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SysParams', function(SysParams) {
                            return SysParams.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sys-params', null, { reload: 'sys-params' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sys-params.delete', {
            parent: 'sys-params',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sys-params/sys-params-delete-dialog.html',
                    controller: 'SysParamsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SysParams', function(SysParams) {
                            return SysParams.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sys-params', null, { reload: 'sys-params' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
