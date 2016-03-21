'use strict';

angular.module('chimpalotApp.services').factory('validateInputs', function() {
	return function(inputs) {
		if (inputs.taskQuestion.value === null ||
			inputs.taskQuestion.value === '') {
			window.alert('Task Question must not be empty!');
		} else if (inputs.ratingTaskQuestion.value === null ||
			inputs.ratingTaskQuestion.value === '') {
			window.alert('Rating Task Question must not be empty!');
		} else if (inputs.taskDescription.value === null ||
			inputs.taskDescription.value === '') {
			window.alert('Task Description must not be empty!');
		} else if (inputs.taskTitle.value === null ||
			inputs.taskTitle.value === '') {
			window.alert('Task Title must not be empty!');
		} else if (inputs.pictureURL.value === null &&
			inputs.taskSourceURL.value !== null ||
			inputs.pictureURL.value !== null &&
			inputs.taskSourceURL.value === null) {
			window.alert('Choose either Picture URL and Task Source URL ' +
				'or neither Picture URL nor Task Source URL!');
		} else if (inputs.maxCreativeTask.value <= 0) {
			window.alert('Max Creative Task must not be less equal 0!');
		} else if (inputs.maxRatingTask.value <= 0) {
			window.alert('Max Rating Task must not be less equal 0!');
		} else if (inputs.budget.value <= 0) {
			window.alert('Budget must not be less equal 0!');
		} else if (!inputs.sendCreativeToMTurk.value &&
			!inputs.sendCreativeToPyBossa.value) {
			window.alert('Either Send Creative to MTurk or ' +
				'Send Creative to Pybossa must be choosen');
		} else if (!inputs.sendRatingToMTurk.value &&
			!inputs.sendRatingToPyBossa.value) {
			window.alert('Either Send rating to MTurk or ' +
				'Send rating to Pybossa must be choosen');
		} else if (inputs.basicPaymentMTurk.value <= 0 &&
			(inputs.sendCreativeToMTurk.value ||
				inputs.sendRatingToMTurk.value)) {
			window.alert('Basic Payment MTurk must not be less equal 0!');
		} else if (inputs.basicPaymentPyBossa.value <= 0 &&
			(inputs.sendCreativeToPyBossa.value ||
				inputs.sendRatingToPyBossa.value)) {
			window.alert('Basic Payment Pybossa must not be less equal 0!');
		} else if (inputs.paymentPerTaskCrMTurk.value <= 0 &&
			inputs.sendCreativeToMTurk.value) {
			window.alert('Payment Per Creative Task MTurk must not be less equal 0!');
		} else if (inputs.paymentPerTaskRaMTurk.value <= 0 &&
			inputs.sendRatingToMTurk.value) {
			window.alert('Payment per rating task MTurk must not be less equal 0!');
		} else if (inputs.paymentPerTaskCrPyBossa.value <= 0 &&
			inputs.sendCreativeToPyBossa.value) {
			window.alert('Payment per creative task Pybossa must not be less equal 0!');
		} else if (inputs.paymentPerTaskRaPyBossa.value <= 0 &&
			inputs.sendRatingToPyBossa.value) {
			window.alert('Payment per rating task Pybossa must not be less equal 0!');
		} else if (inputs.evaluationType.value === null) {
			window.alert('Evaluation Type must not be empty!');
		} else {
			window.alert('Validation sucessfulll!');
			return true;
		}
		return false;
	};
});