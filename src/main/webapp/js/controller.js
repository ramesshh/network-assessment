(function() {
	var as = angular.module('apicemApp.controllers', [ 'smart-table' ]);

	as.controller('MainController', function($q, $scope, $rootScope, $http, i18n, $location) {
		var load = function() {
		};

		load();

		$scope.language = function() {
			return i18n.language;
		};
		$scope.setLanguage = function(lang) {
			i18n.setLanguage(lang);
		};
		$scope.activeWhen = function(value) {
			return value ? 'active' : '';
		};

		$scope.path = function() {
			return $location.url();
		};

		// $scope.login = function() {
		// console.log('username:password @' + $scope.username + ',' +
		// $scope.password);
		// $scope.$emit('event:loginRequest', $scope.username, $scope.password);
		// //$('#login').modal('hide');
		// };
		$scope.logout = function() {
			$rootScope.user = null;
			$scope.username = $scope.password = null;
			$scope.$emit('event:logoutRequest');
			$location.url('/login');
		};

	});

	as.controller('LoginController', function($scope, $rootScope, $http, base64, $location) {

		$scope.login = function() {
			console.log('username:password @' + $scope.username + ',' + $scope.password);
			$scope.$emit('event:loginRequest', $scope.username, $scope.password);
			// $('#login').modal('hide');
		};
	});

	as.controller('ApicEMLoginController', function($scope, $rootScope, $http, base64, $location, DeviceData) {

		$scope.selectedApicem = '';
		$scope.apicUsername='';
		$scope.apicPassword='';
		$scope.version='';
		

		$scope.allApicEms = [];
		$http.get('apic-ems.json').success(function(data) {
			$scope.allApicEms = data.apicems;
			$scope.selectedApicem = $scope.allApicEms[0].apicemIp;
		});
		$scope.apicemLogin = function() {

			DeviceData.setSelectedApicEm($scope.selectedApicem);
			angular.forEach($scope.allApicEms, function(apicEm) {
				if (apicEm.apicemIp == $scope.selectedApicem) {
					DeviceData.setApicemVersion(apicEm.version);
					$scope.version=apicEm.version;
				}
			});

			var actionURL = "api/token";
			var data = {
				"username" : $scope.apicUsername,
				"password" : $scope.apicPassword,
				"apicemIP" : $scope.selectedApicem,
				"version" : $scope.version
			};
			
			$http.post(actionURL,data).success(function(data) {
				console.log("Success Data is " + data);
				DeviceData.setToken(data);
				$location.url("/discovery");
			}).error(function(data) {
				console.log("Failure data Data is " + data);
			});

		};
	});

	as.controller('SearchController', function($scope, $http, i18n, $location, DeviceData) {
		$scope.currentDate = Date.now();
		DeviceData.setCurrentDate($scope.currentDate);
		$scope.originalData = '';
		$scope.deviceCategory = 'all';
		$scope.groupBy = 'groupBy_deviceType';
		var groupType = $scope.groupBy;
		var actionUrl = 'api/discovery/search';
		load = function() {
			$http.get(actionUrl + "?q=" + $scope.q).success(function(data) {
				console.log("Data is " + data);
				$scope.originalData = data;
				DeviceData.setDeviceData(data);
				$scope.devices = groupByData(data, groupType);
			});
		}
		load();

		groupByData = function(data, groupBy) {
			var groupByUrl = 'api/discovery/' + groupBy + '/groupby';
			$http.post(groupByUrl, data).success(function(response) {
				$scope.devices = response;
			});
		}

		$scope.groupByChange = function() {
			$scope.devices = groupByData(DeviceData.getDeviceData(), groupType);
			$scope.deviceCategory = 'all';
		}

		$scope.deviceCategroryChange = function() {
			if ($scope.deviceCategory == 'all') {
				$scope.devices = groupByData(DeviceData.getDeviceData(), groupType);
			} else {
				$scope.filterDevices = [];
				angular.forEach(DeviceData.getDeviceData(), function(device) {

					if ($scope.deviceCategory == 'Cisco') {
						if (device.vendor == $scope.deviceCategory) {
							$scope.filterDevices.push(device);
						}
					} else {
						if (device.type == $scope.deviceCategory) {
							$scope.filterDevices.push(device);
						}
					}
				});

				$scope.devices = groupByData($scope.filterDevices, groupType);
			}
		}
	});

	as.controller('ReplaceCtrl', function($scope, $http, $routeParams, i18n, $location, DeviceData, $filter) {
		$scope.platformId = $routeParams.platformId;
		$scope.allDevices = DeviceData.getDeviceData();
		$scope.selectedCount = 0;
		DeviceData.setPlatformId($routeParams.platformId);
		load = function() {
			var replaceItemData = [];
			angular.forEach($scope.allDevices, function(device) {
				if (device.platformId == $scope.platformId) {
					replaceItemData.push(device);
				}
			}, replaceItemData);
			$scope.devices = replaceItemData;
		}

		load();

		$scope.platformIdChange = function() {
			load();
			$scope.selectedAll = false;
			angular.forEach($scope.devices, function(item) {
				item.selected = false;
			});
			$scope.selectedCount = 0;
		}

		$scope.checkAll = function() {
			if ($scope.selectedAll) {
				$scope.selectedAll = true;
			} else {
				$scope.selectedAll = false;
				$scope.selectedCount = 0;
			}
			angular.forEach($scope.devices, function(item) {
				item.selected = $scope.selectedAll;
				$scope.selectedCount = $scope.selectedCount + 1;
			});
		};

		$scope.checkDevice = function(device) {
			if (device.selected == false) {
				$scope.selectedAll = false;
				$scope.selectedCount = $scope.selectedCount - 1;
			} else {
				$scope.selectedCount = $scope.selectedCount + 1;
			}
		}

		$scope.replaceDevices = function() {
			if ($scope.selectedCount == 0) {
				alert("Please select atleast one device to replace");
			} else {
				$location.url('/discovery/' + $scope.platformId + '/questionare/' + $scope.selectedCount);
			}
		}

	});

	as.controller('QuestionCtrl', function($scope, $http, $routeParams, i18n, $location) {
		$scope.platformId = $routeParams.platformId;

		load = function() {
			$scope.deviceFamily = [];
			$scope.deviceConfig = [];
			$scope.productCatalog = [];
			$scope.replacableProducts = [];
			$http.get('device-family-mapping.json').success(function(data) {
				$scope.deviceFamily = data.deviceFamilyMapping;
				$http.get('device-config-mapping.json').success(function(data) {
					$scope.deviceConfig = data.deviceConfigMapping;
					$http.get('product-catalog.json').success(function(data) {
						$scope.productCatalog = data.products;
						angular.forEach($scope.deviceFamily, function(device) {
							if (device.family == $scope.platformId) {
								angular.forEach($scope.deviceConfig, function(config) {
									if (device.family == config.family) {
										angular.forEach(config.products, function(prod) {
											angular.forEach($scope.productCatalog, function(prodCatalog) {
												if (prodCatalog.productId == prod.productId) {
													$scope.replacableProducts.push(prodCatalog);
												}
											});
										});
									}
								});
							}
						});
					});
				});
			});
			console.log("Products::" + $scope.replacableProducts);
		}

		load();
	});

	as.controller('ProductCtrl', function($scope, $http, $routeParams, i18n, $location) {
		$scope.myInterval = 5000;
		var slides = $scope.slides = [];
		$scope.addSlide = function() {
			var newWidth = 600 + slides.length + 1;
			slides.push({
				image : 'http://placekitten.com/' + newWidth + '/300',
				text : [ 'More', 'Extra', 'Lots of', 'Surplus' ][slides.length % 4] + ' ' + [ 'Cats', 'Kittys', 'Felines', 'Cutes' ][slides.length % 4]
			});
		};
		for (var i = 0; i < 4; i++) {
			$scope.addSlide();
		}
	});

}());