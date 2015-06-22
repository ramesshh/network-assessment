(function() {
	var as = angular.module('apicemApp.controllers', [ 'smart-table', 'ui.utils', 'ui.select' ]);

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

	as.controller('ApicEMLoginController', function($scope, $rootScope, $http, base64, $location, DeviceData, $window) {

		$scope.selectedApicem = '';
		$scope.apicUsername = '';
		$scope.apicPassword = '';
		$scope.version = '';
		$scope.allApicEms = [];

		load = function() {
			$http.get('api/apicem').success(function(data) {
				angular.forEach(data, function(apicEm) {
					$scope.allApicEms.push(apicEm);
					$scope.selectedApicem = apicEm.apicemIP;
				});

			});
		}

		load();

		$scope.apicemLogin = function() {

			DeviceData.setSelectedApicEm($scope.selectedApicem);
			$scope.version = '';
			angular.forEach($scope.allApicEms, function(apicEm) {
				if (apicEm.apicemIP == $scope.selectedApicem) {
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
				$window.sessionStorage.setItem('token', data);
				$window.sessionStorage.setItem('username', $scope.apicUsername);
				$window.sessionStorage.setItem('password', $scope.apicPassword);
				$window.sessionStorage.setItem('version', $scope.version);
				$window.sessionStorage.setItem('apicem', $scope.selectedApicem);
				$http.defaults.headers.common['X-Access-Token'] = data;
				$http.defaults.headers.common['apicem'] = $scope.selectedApicem;
				$http.defaults.headers.common['version'] = $scope.version;
				$location.url("/devices");
			}).error(function(data) {
				console.log("Failure data Data is " + data);
				alert("Server unavailable.. Please check your IP address and try again.");
			});
		};

		$scope.onboardApicEm = function() {

			validateIp = function(ip) {
				var pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]).){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/g;
				/*
				 * use javascript's test() function to execute the regular
				 * expression and then store the result - which is either true
				 * or false
				 */
				var bValidIP = pattern.test(ip);
				if (!bValidIP) {
					alert("You have entered an invalid IP address!");
					return;
				} else if ($scope.newApicVersion == "" || $scope.newApicVersion == "undefined" || $scope.newApicVersion == undefined) {
					alert("Please select APIC EM Version");
					return;
				} else {
					var data = {
						"apicemIP" : $scope.newApicIP,
						"version" : $scope.newApicVersion,
						"location" : $scope.location
					};
					var actionURL = "api/apicem";
					$http.post(actionURL, data).success(function(data) {
						console.log("Success Data is " + data);
						$scope.newApicIP = "";
						$scope.newApicVersion = "";
						$scope.location = "";
						alert("APIC EM Onboarded successfully");
						load();
					}).error(function(data) {
						alert("You have entered an invalid IP address!");
					});
				}
			}
			validateIp($scope.newApicIP);
		}

	});

	as.controller('SearchController', function($scope, $http, i18n, $location, DeviceData, $window) {
		$scope.currentDate = Date.now();
		DeviceData.setCurrentDate($scope.currentDate);
		$scope.originalData = '';
		$scope.deviceCategory = 'all';
		$scope.itemsPerPage = "10";
		$scope.groupBy = 'groupBy_deviceType';
		var groupType = $scope.groupBy;

		$http.defaults.headers.common['X-Access-Token'] = $window.sessionStorage.getItem('token');
		$http.defaults.headers.common['apicem'] = $window.sessionStorage.getItem('apicem');
		$http.defaults.headers.common['version'] = $window.sessionStorage.getItem('version');
		var actionUrl = 'api/discovery/search';
		load = function() {
			$http.get(actionUrl).success(function(data) {
				console.log("Data is " + data);
				$scope.originalData = data;
				DeviceData.setDeviceData(data);
				$window.sessionStorage.setItem('devices', data);
				$scope.devices = groupByData(data, groupType);
				// }
			}).error(function(data) {
				alert("Internal server error.Please try again.");
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

		$scope.order = '+platformId';

		$scope.orderBy = function(property) {
			$scope.order = ($scope.order[0] === '+' ? '-' : '+') + property;
		};

		$scope.orderIcon = function(property) {
			return property === $scope.order.substring(1) ? $scope.order[0] === '+' ? 'glyphicon glyphicon-chevron-up' : 'glyphicon glyphicon-chevron-down' : '';
		};

	});

	as.controller('ReplaceCtrl', function($scope, $http, $routeParams, i18n, $location, DeviceData, $filter, $window) {

		$scope.path = '/' + $routeParams.platformId;

		$scope.itemsPerPage = "10";
		$scope.platformId = decodeURIComponent($routeParams.platformId);
		$scope.allDevices = DeviceData.getDeviceData();
		$scope.selectedCount = 0;
		$scope.type = '';
		DeviceData.setPlatformId($routeParams.platformId);
		$scope.deviceType = $routeParams.type;
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
				$location.url('/products/' + $scope.selectedCount + '/' + $scope.deviceType + '/' + encodeURIComponent($scope.platformId));
			}
		}

		$scope.order = '+platformId';

		$scope.orderBy = function(property) {
			$scope.order = ($scope.order[0] === '+' ? '-' : '+') + property;
		};

		$scope.orderIcon = function(property) {
			return property === $scope.order.substring(1) ? $scope.order[0] === '+' ? 'glyphicon glyphicon-chevron-up' : 'glyphicon glyphicon-chevron-down' : '';
		};

	});

	as.controller('QuestionCtrl', function($scope, $http, $routeParams, i18n, $location, DeviceData) {
		$scope.itemsPerPage = "10";
		$scope.platformId = decodeURIComponent($routeParams.platformId);
		$scope.qty = $routeParams.count;
		$scope.currDate = DeviceData.getCurrentDate();
		$scope.deviceType = $routeParams.type;
		load = function() {
			$scope.productCatalog = [];
			$scope.replacableProducts = [];
			$scope.allProducts = [];
			$http.get('product-catalog.json').success(function(data) {
				$scope.productCatalog = data.products;
				angular.forEach(data.products, function(prodCatalog) {
					if (prodCatalog.type == $scope.deviceType) {
						$scope.replacableProducts.push(prodCatalog);
						$scope.allProducts.push(prodCatalog);
					}
				});
			});
			console.log("Products::" + $scope.replacableProducts);
		}

		$scope.allTags = [];
		questions = function() {
			$scope.questions = [];
			$http.get('questions.json').success(function(data) {
				angular.forEach(data.questions, function(question) {
					if (question.deviceType == $scope.deviceType) {
						$scope.questions.push(question);
						$scope.allTags.push(question.name);
					}
				});
			});
		}

		$scope.questionSelected = function(id) {
			$scope.tags = [];
			angular.forEach($scope.questions, function(question) {
				if (question.checked) {
					var text = {
						"text" : question.name
					};
					$scope.tags.push(text);
				}else{
					question.selectedOtion = "";
				}
			});

			filterTheProducts();
		}

		$scope.tagRemoved = function(tag) {
			angular.forEach($scope.questions, function(question) {
				if (question.name == tag.text) {
					question.checked = false;
					question.selectedOtion = "";
				}
			});

			filterTheProducts();
		}

		// Clear all questions
		$scope.clearQuestions = function() {
			angular.forEach($scope.questions, function(question) {
				question.checked = false;
				question.selectedOtion = "";
			});

			$scope.tags = [];

			$scope.replacableProducts = [];
			angular.forEach($scope.allProducts, function(product) {
				$scope.replacableProducts.push(product);
			});
		}

		questionSelected = function() {
			var selected = false;
			angular.forEach($scope.questions, function(question) {
				if (question.checked) {
					selected = true;
				}
			});
			return selected;
		}

		contains = function(array, str) {
			var hasValue = false;
			angular.forEach(array, function(item) {
				if (item == str) {
					hasValue = true;
				}
			});
			return hasValue;
		}

		// Filter the products based on the current question set
		filterTheProducts = function() {
			$scope.replacableProducts = [];
			var selected = questionSelected();
			angular.forEach($scope.allProducts, function(product) {
				$scope.pushProduct = 0;
				if (selected) {
					angular.forEach($scope.questions, function(question) {
						if (question.checked && $scope.pushProduct >= 0) {
							if (product[question.id].toLowerCase() == "Y".toLowerCase() && (question.selectedOtion == "" || contains(product.addlParams, question.selectedOtion))) {
								$scope.pushProduct = 1;
							} else {
								$scope.pushProduct = -1;
							}
						}
					});
				}
				if ($scope.pushProduct == 1 || !selected) {
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

}());