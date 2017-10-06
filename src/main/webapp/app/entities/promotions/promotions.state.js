(function() {
    'use strict';

    angular
        .module('kiwiCellApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('promotions', {
            parent: 'entity',
            url: '/promotions',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kiwiCellApp.promotions.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/promotions/promotions.html',
                    controller: 'PromotionsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('promotions');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('promotions-detail', {
            parent: 'promotions',
            url: '/promotions/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kiwiCellApp.promotions.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/promotions/promotions-detail.html',
                    controller: 'PromotionsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('promotions');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Promotions', function($stateParams, Promotions) {
                    return Promotions.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'promotions',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('promotions-detail.edit', {
            parent: 'promotions-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotions/promotions-dialog.html',
                    controller: 'PromotionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Promotions', function(Promotions) {
                            return Promotions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('promotions.new', {
            parent: 'promotions',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotions/promotions-dialog.html',
                    controller: 'PromotionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                configurationDate: null,
                                test: false,
                                module: null,
                                application: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('promotions', null, { reload: 'promotions' });
                }, function() {
                    $state.go('promotions');
                });
            }]
        })
        .state('promotions.edit', {
            parent: 'promotions',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotions/promotions-dialog.html',
                    controller: 'PromotionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Promotions', function(Promotions) {
                            return Promotions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('promotions', null, { reload: 'promotions' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('promotions.delete', {
            parent: 'promotions',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotions/promotions-delete-dialog.html',
                    controller: 'PromotionsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Promotions', function(Promotions) {
                            return Promotions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('promotions', null, { reload: 'promotions' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
