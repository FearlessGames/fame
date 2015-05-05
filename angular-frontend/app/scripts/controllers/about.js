'use strict';

/**
 * @ngdoc function
 * @name fameFrontendApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the fameFrontendApp
 */
angular.module('fameFrontendApp')
    .controller('AboutCtrl', function ($scope) {
        $scope.awesomeThings = [
            'HTML5 Boilerplate',
            'AngularJS',
            'Karma'
        ];
    });
