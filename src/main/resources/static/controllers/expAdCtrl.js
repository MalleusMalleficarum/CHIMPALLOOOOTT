'use strict';

angular.module('chimpalotApp.controllers').controller(
	'ExpAdCtrl', ['$scope', '$http',
		function($scope, $http) {

			$http.get('/requester/experiment').success(function(data) {
				$scope.experiments = data;
				$scope.orderProp = 'id';
			});
			$http.get('/labels/expAdLab.json').success(function(data) {
				$scope.labels = data;
			});

			$scope.exportAsTxt = function(expId) {
				console.log('try to export as txt!');
				$http.get('/requester/experiment/' + expId + '/TXT').then(
					function sucessCallback(data) {
						console.log('export as txt!');
						var anchor = angular.element('<a/>');
						anchor.attr({
							href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
							target: '_blank',
							download: 'filename.csv'
						})[0].click();
					},
					function errorCallback(data) {
						console.log(angular.toJson(data));
						console.log(data);
						console.log('export failed!');
					});
			};

			$scope.exportAsCsv = function(expId) {
				console.log('try to export as csv!');
				$http.get('/requester/experiment/' + expId + '/CSV').success(
					function sucessCallback(data) {
						console.log('export as csv!');
						var anchor = angular.element('<a/>');
						anchor.attr({
							href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
							target: '_blank',
							download: 'filename.csv'
						})[0].click();
					},
					function errorCallback() {
						console.log('export failed!');
					});
			};

			$scope.stopSubmission = function(expId) {
				$http.post('/requester/experiment/' + expId + '/stopcreativetasks').success(
					function() {
						window.alert('Submittions for ' + expId + ' stopped!');
					});
			};

			$scope.endExperiment = function(expId) {
				$http.post('/requester/experiment/' + expId + '/end').success(
					function() {
						window.alert(expId + ' ended!');
					});
			};

			$scope.deleteExperiment = function(expId) {
				console.log('try to delete experiment');
				$http.delete('/requester/experiment/' + expId).success(
					function() {
						window.alert(expId + ' deletet!');
					});
			};
		}
	]);