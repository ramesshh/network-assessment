(function() {
	var as = angular.module('apicemApp.controllers', [ 'smart-table', 'ui.utils' ]);

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

	as.controller('LoginController', function($scope, $rootScope, $http, base64, $location, $window) {

		$scope.login = function() {
			console.log('username:password @' + $scope.username + ',' + $scope.password);
			$scope.$emit('event:loginRequest', $scope.username, $scope.password);
			// $('#login').modal('hide');
		};
	});

	as.controller('ApicEMLoginController', function($scope, $rootScope, $http, base64, $location, DeviceData) {

		$scope.selectedApicem = '';
		$scope.apicUsername = '';
		$scope.apicPassword = '';
		$scope.version = '';

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
					$scope.version = apicEm.version;
				}
			});

			var actionURL = "api/token";
			var data = {
				"username" : $scope.apicUsername,
				"password" : $scope.apicPassword,
				"apicemIP" : $scope.selectedApicem,
				"version" : $scope.version
			};

			$http.post(actionURL, data).success(function(data) {
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
		$scope.itemsPerPage = "10";
		$scope.groupBy = 'groupBy_deviceType';
		var groupType = $scope.groupBy;
		var actionUrl = 'api/discovery/search';
		load = function() {
			$http.get(actionUrl).success(function(data) {
				console.log("Data is " + data);
				/*
				 * if (data) {
				 * $http.get('device.json').success(function(response) {
				 * $scope.originalData = response.devices;
				 * DeviceData.setDeviceData(response.devices); $scope.devices =
				 * groupByData(response.devices, groupType); }); } else {
				 */
				$scope.originalData = data;
				DeviceData.setDeviceData(data);
				$scope.devices = groupByData(data, groupType);
				// }
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
		$scope.itemsPerPage = "10";
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

	as.controller('BomCtrl', function($scope, $http, $routeParams, i18n, $location, DeviceData, $window) {
		$scope.productId = $routeParams.productId;
		$scope.qty = $routeParams.qty;
		$scope.products = [];
		$http.get('product-catalog.json').success(function(data) {
			$scope.productCatalog = data.products;
			angular.forEach($scope.productCatalog, function(prodCatalog) {
				if (prodCatalog.productId == $scope.productId) {
					$scope.products.push(prodCatalog);
				}
			});
		});

		$scope.placeOrder = function() {
			$window.open('https://apps.cisco.com/ccw/cpc/concept/268437899', '_blank');
			$scope.generateBOM();
		}

		$scope.save = function() {
			$scope.generateBOM();
		}

		$scope.sendForApproval = function() {
			$scope.generateBOM();
			$window.open('https://www.cisco.com/go/commerceworkspace', '_blank');
		}

		$scope.generateBOM = function() {
			$scope.data = [];
			angular.forEach($scope.products, function(product) {
				$scope.data = [ {
					"Product" : product.productId,
					"Description" : product.description,
					"Qty" : $scope.qty,
				} ];
			});
			JSONToCSVConvertor($scope.data, $scope.productId + '_' + $scope.qty, true);
		}

		function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {
			// If JSONData is not an object then JSON.parse will parse the JSON
			// string in an Object
			var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;

			var CSV = '';
			// Set Report title in first row or line

			CSV += ReportTitle + '\r\n\n';

			// This condition will generate the Label/Header
			if (ShowLabel) {
				var row = "";

				// This loop will extract the label from 1st index of on array
				for ( var index in arrData[0]) {

					// Now convert each value to string and comma-seprated
					row += index + ',';
				}

				row = row.slice(0, -1);

				// append Label row with line break
				CSV += row + '\r\n';
			}

			// 1st loop is to extract each row
			for (var i = 0; i < arrData.length; i++) {
				var row = "";

				// 2nd loop will extract each column and convert it in string
				// comma-seprated
				for ( var index in arrData[i]) {
					row += '"' + arrData[i][index] + '",';
				}

				row.slice(0, row.length - 1);

				// add a line break after each row
				CSV += row + '\r\n';
			}

			if (CSV == '') {
				alert("Invalid data");
				return;
			}

			// Generate a file name
			var fileName = "BOM_";
			// this will remove the blank-spaces from the title and replace it
			// with an underscore
			fileName += ReportTitle.replace(/ /g, "_");

			// Initialize file format you want csv or xls
			var uri = 'data:text/xls;charset=utf-8,' + escape(CSV);

			// Now the little tricky part.
			// you can use either>> window.open(uri);
			// but this will not work in some browsers
			// or you will not get the correct file extension

			// this trick will generate a temp <a /> tag
			var link = document.createElement("a");
			link.href = uri;

			// set the visibility hidden so it will not effect on your
			// web-layout
			link.style = "visibility:hidden";
			link.download = fileName + ".csv";

			// this part will append the anchor tag and remove it after
			// automatic click
			document.body.appendChild(link);
			link.click();
			document.body.removeChild(link);
		}

	});

	as.controller('QuestionCtrl', function($scope, $http, $routeParams, i18n, $location, DeviceData) {
		$scope.itemsPerPage = "10";
		$scope.platformId = $routeParams.platformId;
		$scope.qty = $routeParams.count;
		$scope.currDate = DeviceData.getCurrentDate();

		load = function() {
			$scope.deviceFamily = [];
			$scope.deviceConfig = [];
			$scope.productCatalog = [];
			$scope.replacableProducts = [];
			$scope.allProducts = [];
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
													$scope.allProducts.push(prodCatalog);
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

		questions = function() {
			$scope.questions = [];
			$http.get('questions.json').success(function(data) {
				angular.forEach(data.questions, function(question) {
					if (question.family == $scope.platformId) {
						$scope.questions.push(question);
					}
				});
			});
		}

		$scope.questionSelected = function(type, value, inputType) {
			if (inputType == "radio") {
				angular.forEach($scope.questions, function(question) {
					if (question.id == type) {
						angular.forEach(question.options, function(option) {
							if (option.value != value) {
								option.isUserAnswer = "false";
							}
						});
					}
				});
			}

			filterTheProducts();
		}

		// Clear all questions
		$scope.clearQuestions = function() {
			angular.forEach($scope.questions, function(question) {
				angular.forEach(question.options, function(option) {
					option.isUserAnswer = "false";
				});
			});

			$scope.tags = [];
			
			$scope.replacableProducts = [];
			angular.forEach($scope.allProducts, function(product) {
				$scope.replacableProducts.push(product);
			});
		}

		// Filter the products based on the current question set
		filterTheProducts = function() {
			$scope.pushProduct = 0;
			$scope.replacableProducts = [];
			angular.forEach($scope.allProducts, function(product) {
				angular.forEach($scope.questions, function(question) {

					// if (question.type != "checkbox") {
					angular.forEach(question.options, function(option) {
						if ((option.isUserAnswer == true || option.isUserAnswer == "true") && 0 <= $scope.pushProduct) {
							if (product[question.id].toLowerCase() != option.value.toLowerCase()) {
								$scope.pushProduct = -1;
							} else {
								$scope.pushProduct = 1;
							}
						}
					});
					/*
					 * } else { $scope.checkboxGroup = [];
					 * angular.forEach(question.options, function(option) { if
					 * (option.isUserAnswer == true || option.isUserAnswer ==
					 * "true") { if (product[question.id].toLowerCase() !=
					 * option.value.toLowerCase()) {
					 * $scope.checkboxGroup.push(0); } else {
					 * $scope.checkboxGroup.push(1); } } });
					 * 
					 * angular.forEach($scope.checkboxGroup, function(group) {
					 * if (group == 1) { $scope.pushProduct = 1; } }); }
					 */
				});
				if ($scope.pushProduct == 1) {
					$scope.replacableProducts.push(product);
				}
			});
		}

		// Navigate to BOM page after clicking the product
		$scope.selectedProduct = function(product) {
			$scope.billProducts = [];
			$scope.billProducts.push(product);
			DeviceData.setBillProducts($scope.billProducts);
			$location.url('/discovery/' + product.productId + '/bom/' + $scope.qty);
		}

		load();
		questions();
		filterTheProducts();

		$scope.loadQuestions = function($query) {
			return $http.get('questions.json', {
				cache : true
			}).then(function(response) {
				var questions = response.data.questions;
				return questions.filter(function(question) {
					return question.name.toLowerCase().indexOf($query.toLowerCase()) != -1;
				});
			});
		};

	});

}());