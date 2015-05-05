'use strict';

/**
 * @ngdoc function
 * @name fameFrontendApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the fameFrontendApp
 */
angular.module('fameFrontendApp')
    .controller('MainCtrl', function ($scope) {
        $scope.awesomeThings = [
            'HTML5 Boilerplate',
            'AngularJS',
            'Karma'
        ];
    });
