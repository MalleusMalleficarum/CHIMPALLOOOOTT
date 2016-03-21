'use strict';

angular.module('chimpalotApp.services').factory('hideInputs', function() {
	return function(invisible, inputs) {
			angular.forEach(invisible, function(value) {
				inputs[value].isVisible = false;
			});
			console.log('some inputs are hidden now!');
		};
});