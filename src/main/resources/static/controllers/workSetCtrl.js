'use strict';

angular.module('chimpalotApp.controllers').controller('WorkerSettingsCtrl', ['$scope',
	'$http',
	function($scope, $http) {

		$http.get('labels/workSetLab.json').then(
			function sucessCallback(response) {
				$scope.labels = response.data;
				console.log('labels loaded!');
				console.log(angular.toJson($scope.labels));
			},
			function errorCallback() {
				console.log('labels could not be loaded!');
			});
		$http.get('models/workSetModel.json').then(
			function sucessCallback(response) {
				$scope.model = response.data;
				$http.get('requester/automaticPayment').then(
					function sucessCallback(response) {
						$scope.model.automaticPayment = response.data;
						console.log('automatic payment loaded!');
					},
					function errorCallback() {
						console.log('automatic payment could not be loaded!');
					});
				$http.get('requester/calibrationquestion').then(
					function sucessCallback(response) {
						$scope.model.calibQuestions = response.data;
						console.log('calib questions loaded!');
					},
					function errorCallback() {
						console.log('calibquestions could not be loaded!');
					});
			},
			function errorCallback() {
				console.log('model could not be loaded!');
			});
		/**
		 * Adds an item to the a list input
		 * @param list: the list the item is added to
		 * @param item: the item that is added
		 */
		$scope.addPossibleAnswer = function(calibQuestion, parent) {
			var answer = {
				text: parent.item,
				istrue: false
			};
			calibQuestion.possibleAnswers.push(answer);
			parent.item = null;
		};
		/**
		 * Removes an item to the a list input
		 * @param list: the list the item is removed from
		 * @param item: the item that is removed
		 */
		$scope.removePossibleAnswer = function(calibQuestion, index) {
			console.log(angular.toJson(calibQuestion));
			var possibleAnswer = calibQuestion.possibleAnswers[index];
			calibQuestion.possibleAnswers.splice(index, 1);
			angular.forEach(calibQuestion.calibrationAnswers, function(ques, index) {
				if (ques === possibleAnswer) {
					calibQuestion.calibrationAnswers.splice(index, 1);
					return;
				}
			});
		};
		/**
		 * [changeStatus description]
		 * @param  {[type]} selected       [description]
		 * @param  {[type]} possibleAnswer [description]
		 * @param  {[type]} calibQuestion  [description]
		 * @return {[type]}                [description]
		 */
		$scope.changeStatus = function(possibleAnswer) {
			if (possibleAnswer.istrue) {
				possibleAnswer.istrue = false;
			} else {
				possibleAnswer.istrue = true;
			}
			/*var isNew = true;
			angular.forEach(calibQuestion.calibrationAnswers, function(ques, index) {
				if (ques === possibleAnswer) {
					calibQuestion.calibrationAnswers.splice(index, 1);
					isNew = false;
					return;
				}
			});
			if (isNew) {
				calibQuestion.calibrationAnswers.push(possibleAnswer);
			}*/
		};
		/**
		 * Adds an item to the a list input
		 * @param list: the list the item is added to
		 * @param item: the item that is added
		 */
		$scope.addCalibQuest = function(calibQuestions, parent) {
			var calibQuest = {
				question: parent.item,
				possibleAnswers: []
			};
			calibQuestions.push(calibQuest);
			parent.item = null;
		};
		/**
		 * Removes an item to the a list input
		 * @param list: the list the item is removed from
		 * @param item: the item that is removed
		 */
		$scope.removeCalibQuest = function(calibQuestions, index) {
			console.log(calibQuestions[index].id);
			if (calibQuestions[index].id === undefined) {
				calibQuestions.splice(index, 1);
			} else {
				$http.delete('/requester/calibrationquestion/' + calibQuestions[index].id,
					angular.toJson(calibQuestions[index])).then(
					function successCallback() {
						console.log('calib question removed!');
						calibQuestions.splice(index, 1);
					},
					function errorCallback(response) {
						console.log('calib question could not be removed!');
						console.log(angular.toJson(response.data));
					});
			}
		};
		/**
		 * [saveCalibrationQuestion description]
		 * @param  {[type]} calibQuestions [description]
		 * @param  {[type]} index          [description]
		 * @return {[type]}                [description]
		 */
		$scope.saveCalibrationQuestion = function(calibQuestions, index) {
			console.log(angular.toJson(calibQuestions[index]));
			$http.post('/requester/calibrationquestion',
				angular.toJson(calibQuestions[index])).then(
				function successCallback(response) {
					calibQuestions[index].id = response.data;
					console.log('calib question saved!');
					console.log(angular.toJson(response.data));
				},
				function errorCallback(response) {
					console.log('calib question could not be saved!');
					console.log(angular.toJson(response.data));
				});
		};

		$scope.saveBasicPayment = function(basicPayment) {
			$http.post('/requester/config/payment',
				angular.toJson(basicPayment)).then(
				function successCallback() {
					console.log('basic payment saved!');
				},
				function errorCallback(response) {
					console.log('basic payment could not be saved!');
					console.log(angular.toJson(response.data));
				});

		};
	}
]);