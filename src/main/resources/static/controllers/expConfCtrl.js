'use strict';

/**
 * Controller of the experiment config control.
 * On bootstrap it loads the model with http-gets into the experiment config view.
 * Additionally it loads provides functions for the view to reach services and make
 * bigger changes in the model.
 */
angular.module('chimpalotApp.controllers').controller('ExperimentConfigurationCtrl', [
	'$scope',
	'$routeParams',
	'$http',
	'validateInputs',
	'hideInputs',
	'modelToInputs',
	'inputsToModel',
	'autofills',
	'$sce',
	'$templateRequest',
	'$compile',
	'$window',
	function($scope, $routeParams, $http, validateInputs, hideInputs, modelToInputs, 
		inputsToModel, autofills, $sce, $templateRequest, $compile, $window) {
		/**
		 * the choosen upload option
		 * if 0 => safe config model
		 * if 1 => start experiment
		 */
		$scope.uploadOption = 0;
		/**
		 * the run status of the experiment
		 * if 0 => new experiment
		 * if 1 => running experiment
		 */
		$scope.runStatus = 0;
		/**
		 * try to load the necassary labels
		 */
		$http.get('/labels/expConfLab.json').then(
			function successCallback(response) {
				$scope.labels = response.data;
				console.log('labels loaded!');
			},
			function errorCallback() {
				console.log('labels could not be loaded!');
			});
		/**
		 * try to load the necassary inputs
		 */
		$http.get('/inputs/expConfIn.json').then(function successCallback(response) {
			$scope.inputs = response.data;
			console.log('inputs loaded!');
			/**
			 * try to load the prototype of the model
			 */
			$http.get('/models/expConfModel.json').then(
				function successCallback(response) {
					$scope.model = response.data;
					console.log('model loaded!');
					$http.get('/requester/calibrationquestion').then(
						function successCallback(response) {
							$scope.inputs.calibQuestions.options = response.data;
							console.log('calibrationquestions loaded!');
						},
						function errorCallback() {
							console.log('calibrationquestions could not be loaded!');
						});
					/**
					 * decides which of the following casesis present
					 * case 1: new config model/experiment with no template used
					 * case 2: new config model/experiment with a predefined template in use
					 * case 3: new experiment but safed own template used
					 * case 4: running experiment
					 */
					if ($routeParams.confId === 'empty' && $routeParams.expId === 'new') {
						//case 1
						console.log('new and empty!');
					} else if ($routeParams.confId !== 'empty' && $routeParams.expId === 'new') {
						/**
						 * try to get the list of the predefined templates to see if confId is 
						 * in that list what means that a predefined template will be used
						 */
						$http.get('/templates/list.json').then(
							function successCallback(response) {
								if (response.data.indexOf($routeParams.confId) !== -1) {
									//case 2
									console.log('prefabricated template!');
									/**
									 * try to load predefined template
									 */
									$http.get('templates/' + $routeParams.confId + '.json').then(
										function successCallback(response) {
											$scope.inputs = Object.assign(response.data.extraInputs, $scope.inputs);
											modelToInputs(response.data.prefills, $scope.inputs);
											hideInputs(response.data.invisible, $scope.inputs);
											$scope.autofills = autofills($scope.inputs, response.data.autofills);
											console.log($scope.autofills);
											//Validations might be follow
											console.log('loaded prefabricated template!');
										},
										function errorCallback() {
											console.log('Prefaricated template could not be loaded!');
										});
								} else {
									//case 3
									console.log('own config file!');
									/**
									 * try to load own config file
									 */
									$http.get('/requester/configfile/' + $routeParams.confId).then(
										function successCallback(response) {
											$scope.model = response.data;
											modelToInputs($scope.model, $scope.inputs);
											$scope.confId = $routeParams.confId;
											console.log('loaded own config file!');
										},
										function errorCallback() {
											console.log('Own config file could not be loaded!');
										});
								}
							},
							function errorCallback() {
								console.log('Template list could not be loaded!');
							});
					} else if ($routeParams.expId !== 'new') {
						//case 4
						console.log('running experiment!');
						/**
						 * try to load configfile of the running experiment which id is descibed
						 * in the route params
						 */
						$http.get('/requester/configfile/' + $routeParams.confId).then(
							function successCallback(response) {
								modelToInputs(response.data, $scope.inputs);
								$scope.expId = $routeParams.expId;
								$scope.runStatus = 1;
								$scope.uploadOption = 1;
								console.log('loaded running experiment!');
							},
							function errorCallback() {
								console.log('Experiment config file could not be loaded!');
							});
					}
				},
				function errorCallback() {
					console.log('Model could not be loaded!');
				});
		}, function errorCallback() {
			console.log('Inputs could not be loaded!');
		});
		/**
		 * Adds an item to the a list input
		 * @param list: the list the item is added to
		 * @param item: the item that is added
		 */
		$scope.addItem = function(list, parent) {
			list.push(parent.newItem);
			parent.newItem = null;
		};
		/**
		 * Removes an item to the a list input
		 * @param list: the list the item is removed from
		 * @param item: the item that is removed
		 */
		$scope.removeItem = function(list, index) {
			list.splice(index, 1);
		};
		/**
		 * Adds an item to the a list input
		 * @param list: the list the item is added to
		 * @param item: the item that is added
		 */
		$scope.addValueTextItem = function(list, parent) {
			var newItem = {
				text: parent.newText,
				value: parent.newValue
			};
			list.push(newItem);
			parent.newText = null;
			parent.newValue = null;
		};
		/**
		 * [autofill description]
		 * @param  {[type]} id [description]
		 * @return {[type]}    [description]
		 */
		$scope.autofill = function(id, lang) {
			if ($scope.autofills[id] !== undefined) {
				console.log('autofill!');
				$scope.autofills[id](lang);
			}
		};
		/**
		 * [changeStatus description]
		 * @param  {[type]} values [description]
		 * @param  {[type]} option [description]
		 * @return {[type]}        [description]
		 */
		$scope.changeStatus = function(values, option) {
			console.log('change status!');
			console.log(values);
			var isNew = true;
			angular.forEach(values, function(value, index) {
				if (value === option.id) {
					values.splice(index, 1);
					console.log(angular.toJson(values));
					isNew = false;
					return;
				}
			});
			if (isNew) {
				values.push(option.id);
				console.log(angular.toJson(values));
			}
		};
		/**
		 * Adds an item to the a list input
		 * @param list: the list the item is added to
		 * @param item: the item that is added
		 */
		$scope.addPossibleAnswer = function(controlQuestion, parent) {
			var answer = {
				text: parent.item,
				istrue: false
			};
			controlQuestion.possibleAnswers.push(answer);
			parent.item = null;
		};
		/**
		 * Removes an item to the a list input
		 * @param list: the list the item is removed from
		 * @param item: the item that is removed
		 */
		$scope.removePossibleAnswer = function(controlQuestion, index) {
			console.log(angular.toJson(controlQuestion));
			var possibleAnswer = controlQuestion.possibleAnswers[index];
			controlQuestion.possibleAnswers.splice(index, 1);
			angular.forEach(controlQuestion.calibrationAnswers, function(ques, index) {
				if (ques === possibleAnswer) {
					controlQuestion.calibrationAnswers.splice(index, 1);
					return;
				}
			});
		};
		/**
		 * [changeStatus description]
		 * @param  {[type]} selected       [description]
		 * @param  {[type]} possibleAnswer [description]
		 * @param  {[type]} controlQuestion  [description]
		 * @return {[type]}                [description]
		 */
		$scope.changeControlStatus = function(possibleAnswer) {
			if (possibleAnswer.isTrue) {
				possibleAnswer.isTrue = false;
			} else {
				possibleAnswer.isTrue = true;
			}
		};
		/**
		 * Adds an item to the a list input
		 * @param list: the list the item is added to
		 * @param item: the item that is added
		 */
		$scope.addControlQuest = function(controlQuestions, parent) {
			var controlQuest = {
				question: parent.item,
				possibleAnswers: []
			};
			controlQuestions.push(controlQuest);
			parent.item = null;
		};

		/**
		 * Function that is called, when you want to save the config-file
		 * that you are editing.
		 */
		$scope.save = function() {
			if ($scope.model.id === null || $scope.model.id === '') {
				window.alert('Config Model ID must not be empty!');
				return;
			}
			inputsToModel($scope.model, $scope.inputs);
			//$scope.actualiseStrategies();
			/*window.alert(angular.toJson($scope.model.strategy));*/
			$http.post('/requester/configfile/' + $scope.model.id,
				angular.toJson($scope.model)).then(
				function successCallback(response) {
					window.alert('Configfile saved!');
					console.log(angular.toJson($scope.model));
					console.log(angular.toJson(response.data));
				},
				function errorCallback(response) {
					window.alert('Configfile could not be safed!');
					console.log(angular.toJson(response.data));
				});
		};

		/**
		 * Function that you call if you want to (re)start the experiment,
		 * that is configured.
		 */
		$scope.start = function() {
			inputsToModel($scope.model, $scope.inputs);
			console.log(angular.toJson($scope.model));
			//$scope.actualiseStrategies();
			console.log(angular.toJson($scope.model));
			if ($routeParams.expId === 'new') {
				$http.post('/requester/experiment/' + $scope.expId + '/start',
					angular.toJson($scope.model)).then(
					function successCallback(response) {
						window.alert('success!');
						window.alert(angular.toJson(response.data));
					},
					function errorCallback(response) {
						window.alert('error!');
						window.alert(angular.toJson(response.data));
					});
			} else {
				$http.post('/requester/experiment/' + $scope.expId + '/change',
					angular.toJson($scope.model)).success(
					function() {
						window.alert('experiment changed!');
					});
			}
		};

		$scope.validate = function() {
			if (validateInputs($scope.inputs)) {
				$scope.uploadOption = 1;
			} else {
				$scope.uploadOption = 0;
			}
		};

		$scope.creativePreview = function() {
			$http.post('/requester/preview/creative',
				angular.toJson($scope.model),
				{responseType:document}).error(
				function(response) {
					console.log(response);
					//$scope.preview = $sce.trustAsHtml(response.data);
				}
			);
			$window.open('/requester/preview/creative', 'creative preview');
		};

		$scope.ratingPreview = function() {
			$http.post('/requester/preview/rating',
				angular.toJson($scope.model),
				{responseType:document}).error(
				function(response) {
					console.log(response);
					//$scope.preview = $sce.trustAsHtml(response.data);
				}
			);
			$window.open('/requester/preview/rating', 'rating preview');
		};
	}
]);