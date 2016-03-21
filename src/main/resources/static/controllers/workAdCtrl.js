'use strict';

angular.module('chimpalotApp.controllers').controller('WorkerAdministrationCtrl', ['$scope',
	'$http',
	function($scope, $http) {

		$scope.paymentThreshold = 15;
		$http.get('requester/worker').success(function(data) {
			$scope.workers = data;
		});
		/**
		 * try to load the necassary labels
		 */
		$http.get('/labels/workAdLab.json').then(
			function successCallback(response) {
				$scope.labels = response.data;
				console.log('labels loaded!');
			},
			function errorCallback() {
				console.log('labels could not be loaded!');
			});

		$scope.pay = function(id) {
			$http.post('requester/worker/' + id + '/pay').success(function(data) {
				$scope.workers = data;
			});
		};

		$scope.block = function(id) {
			$http.post('requester/worker/' + id + '/block').success(function(data) {
				$scope.workers = data;
			});
		};
	}
]);