'use strict';

angular.module('chimpalotApp.controllers').controller('WorkerOverviewCtrl', [ '$scope',
		'$http', function($scope, $http) {
	//TODO get WOrkerList, ich bekomme nur die WOerkIDs, muss die WOrkerDaten einzeln Nachladen
	//getWorker(ID)=Gibt mir alles was der Worker so an daten hat
			$http.get('requester/worker').success(function(data){
				$scope.workerA = data;
				
			});
			
			
			//Falsch bennant work_ers
			$http.get('resources/worksers.json').success(function(datas) {
				$scope.workers = datas;
			});
			$scope.test = false;
		    $scope.detailedData = [];
			
			/*$http.get('requester/worker').success(function(data){
				$scope.workersSmall = data;
				//workersSmall hat ID, name;
				
			}); 
			/* Vllt so(ich brauche alle Wokrerdaten pracktisch immer
			$http.get('requester/worker').success(function(data) {
				$scope.workersSmall = data;
			
			}
			
			*/
			$scope.loadWorker = function(id) {
				var url = 'requester/worker' + id;
				$http.get(url).success(function(data){
					
				}); 
			}
			$scope.inter = undefined;
			$scope.testing = "la";
			$scope.vol = false;
			$scope.per = false;
			$scope.workerData ="";
			$scope.workert = "halo";
			$scope.changeVolatile = function(id) {
				
				this.vol=!this.vol;
				this.visible = (this.vol || this.per);
				this.loadWorkerData(id);

			}
			$scope.changePermanent = function(id) {
				
				this.per=!this.per;
				this.visible = (this.vol || this.per);
				this.loadWorkerData(id);

			}
			$scope.loadWorkerData = function(id) {
				var url = '/requester/worker/' + id;
				
				$scope.inter = undefined;
				
				
				$http.get(url).success(function(data) {
					
					var b = data;
					var c = 'ARschLoich';
					
					$scope.detailedData.indexOf(b)
					if($scope.detailedData.indexOf(b) == -1) {
						$scope.detailedData.push(b);
					}
					
					
					

				})
				
				

				;
					
				
				this.testing = parseInt(id);
			}
			$scope.load = function(id) {
				
			}
			
			
			
			
			
			$scope.basicText = "Name oder ID von Workern";
			$scope.orderProp = 'age';
			//Amazon sendet keine Gutscheine unter 15 cent
			$scope.paymentThreshold = 15;
			$scope.payable = function(amount) {
				var x = parseInt(amount);
				return (x<$scope.paymentThreshold);
			}
		} ]);


