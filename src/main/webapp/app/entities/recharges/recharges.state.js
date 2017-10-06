(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('recharges', {
            parent: 'entity',
            url: '/recharges',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kiwiCellApp.recharges.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/recharges/recharges.html',
                    controller: 'RechargesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recharges');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('recharges-detail', {
            parent: 'recharges',
            url: '/recharges/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kiwiCellApp.recharges.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/recharges/recharges-detail.html',
                    controller: 'RechargesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recharges');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Recharges', function($stateParams, Recharges) {
                    return Recharges.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'recharges',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('recharges-detail.edit', {
            parent: 'recharges-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recharges/recharges-dialog.html',
                    controller: 'RechargesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Recharges', function(Recharges) {
                            return Recharges.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('recharges.new', {
            parent: 'recharges',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recharges/recharges-dialog.html',
                    controller: 'RechargesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                value: null,
                                date: null,
                                disccount: null,
                                awardedSecs: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('recharges', null, { reload: 'recharges' });
                }, function() {
                    $state.go('recharges');
                });
            }]
        })
        .state('recharges.edit', {
            parent: 'recharges',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recharges/recharges-dialog.html',
                    controller: 'RechargesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Recharges', function(Recharges) {
                            return Recharges.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('recharges', null, { reload: 'recharges' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('recharges.delete', {
            parent: 'recharges',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recharges/recharges-delete-dialog.html',
                    controller: 'RechargesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Recharges', function(Recharges) {
                            return Recharges.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('recharges', null, { reload: 'recharges' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
