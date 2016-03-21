'use strict';

angular.module('chimpalotApp.services').factory('autofills', function() {
	function createAutofill(autofill, inputs, autofillIn) {
		var autofillOut;
		if (autofill === undefined) {
			autofillOut = function(lang) {
				inputs[autofillIn.filledField].value = autofillIn.prefixLabel[lang] + 
					inputs[autofillIn.fillingField].value + autofillIn.suffixLabel[lang];
			};
		} else {
			autofillOut = function(lang) {
				autofill(lang);
				inputs[autofillIn.filledField].value = autofillIn.prefixLabel[lang] +
					inputs[autofillIn.fillingField].value + autofillIn.suffixLabel[lang];
			};
		}
		return autofillOut;
	}

	return function(inputs, autofillsIn) {
		console.log('autofills will be created!');
		var autofillsOut = {};
		angular.forEach(autofillsIn, function(autofillIn) {
			autofillsOut[autofillIn.fillingField] = 
				createAutofill(autofillsOut[autofillIn.fillingField], inputs, autofillIn);
		});
		console.log(autofillsOut);
		return autofillsOut;
	};
});