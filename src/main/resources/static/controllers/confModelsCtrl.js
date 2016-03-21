'use strict';

angular.module('chimpalotApp.controllers').controller('ConfModelsController', ['$scope',
	'$http',
	function($scope, $http) {
		$http.get('labels/confModelsLab.json').success(function(data) {
			$scope.labels = data;
		});
		$http.get('/requester/configfile').success(function(data) {
			$scope.confModels = data;
		});

		$scope.delete = function(confId) {
			console.log('delete own template!');
			$http.delete('/requester/configfile/' + confId).then(
				function successCallback() {
					window.alert(confId + ' deletet!');
					var index = $scope.confModels.indexOf(confId);
					$scope.confModels.splice(index, 1);
				}, 
				function errorCallback(msg) {
					console.log('Could not delete own template!');
					console.log(angular.toJson(msg));
				});
		};

	}
]);