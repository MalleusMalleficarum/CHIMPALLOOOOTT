'use strict';

angular.module('chimpalotApp.services').factory('inputsToModel', function() {
	function actualiseStrategies(model, strategies) {
		angular.forEach(strategies.options, function(strategy) {
			if (strategy.isUsed) {
				var implementation = strategy.implementations[strategy.usedImplementation];
				model.strategy[strategy.class] = implementation.id;
				angular.forEach(implementation.params, function(param) {
					if (param.isUsed) {
						model.strategy[param.id] = param.value;
					}
				});
			}
		});
	}

	return function(model, inputs) {
		console.log('inputs to model!');
		angular.forEach(inputs, function(value, key) {
			if (value.isCore) {
				if (inputs[key].class !== 'complex') {
					model[key] = inputs[key].value;
				} else if (key === 'strategies') {
					actualiseStrategies(model, inputs[key]);
				}
			}
		});
	};
});