'use strict';

angular.module('chimpalotApp.controllers').controller(
	'RootCtrl', [ '$scope',	'$http',
	function($scope, $http) {
		$scope.lang = 0;
		$http.get('labels/rootLab.json').success(function(data) {
			$scope.labels = data;
		});
}]);