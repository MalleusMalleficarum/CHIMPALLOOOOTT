'use strict';

/* App Module */
angular.module('chimpalotApp.services', []);
angular.module('chimpalotApp.controllers', ['ngRoute', 'chimpalotApp.services']);


var chimpalotApp = angular.module('chimpalotApp', ['ngRoute', 'chimpalotApp.controllers', 'chimpalotApp.services']);

chimpalotApp.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/expAd', {
		templateUrl: 'views/expAdView.html',
		controller: 'ExpAdCtrl'
	}).when('/workAd', {
		templateUrl: 'views/workAdView.html',
		controller: 'WorkerAdministrationCtrl'
	}).when('/workSet', {
		templateUrl: 'views/workSetView.html',
		controller: 'WorkerSettingsCtrl'
	}).when('/confModels', {
		templateUrl: 'views/confModelsView.html',
		controller: 'ConfModelsController'
	}).when('/expconf/:expId/:confId', {
		templateUrl: 'views/expConfView.html',
		controller: 'ExperimentConfigurationCtrl'
	}).otherwise({
		redirectTo: '/expAd'
	});
}]);