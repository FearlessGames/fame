'use strict';

/**
 * @ngdoc overview
 * @name fameFrontendApp
 * @description
 * # fameFrontendApp
 *
 * Main module of the application.
 */
angular
    .module('fameFrontendApp', [
        'ngCookies',
        'ngMessages',
        'ngRoute',
        'ngSanitize',
        'ngTouch'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/about', {
                templateUrl: 'views/about.html',
                controller: 'AboutCtrl'
            })
            .when('/createAccount', {
                templateUrl: 'views/createaccount.html',
                controller: 'CreateaccountCtrl'
            })
            .when('/resetPassword', {
                templateUrl: 'views/resetpassword.html',
                controller: 'ResetpasswordCtrl'
            })
            .when('/changePassword', {
                templateUrl: 'views/changepassword.html',
                controller: 'ChangepasswordCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
