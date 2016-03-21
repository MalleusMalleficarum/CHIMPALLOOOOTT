'use strict';

angular.module('chimpalotApp.services').factory('modelToInputs', function() {
	function modelToStrategies(strategiesInput, strategiesModel) {
		angular.forEach(strategiesInput, function(strategy) {
			angular.forEach(strategy.implementations, function(implementation, j) {
				angular.forEach(strategiesModel, function(value, key) {
					if (key === strategy.class && value === implementation.id) {
						strategy.isUsed = true;
						strategy.usedImplementation = j;
					}
				});
				angular.forEach(implementation.params, function(param) {
					angular.forEach(strategiesModel, function(value, key) {
						if (key === param.id) {
							param.value = value;
							param.isUsed = true;
						}
					});
				});
			});
		});
	}

	return function(model, inputs) {
		console.log('model to inputs!');
		console.log(angular.toJson(model));
		angular.forEach(model, function(value, key) {
			console.log(angular.toJson(key));
			console.log(angular.toJson(inputs[key]));
			if(inputs[key] === undefined) {
				//nothing
			} else if (key !== 'strategy') {
				inputs[key].value = value;
			} else {
				modelToStrategies(value.options, model[key]);
			}
		});
	};
});