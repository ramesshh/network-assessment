(function() {
	var as = angular.module('apicemApp.i18n', []);

	as.service('i18n', function() {
		var self = this;
		this.setLanguage = function(language) {
			$.i18n.properties({
				name : 'messages',
				path : 'i18n/',
				mode : 'map',
				language : language,
				callback : function() {
					self.language = language;
				}
			});
		};
		this.setLanguage('en');
	});

	as.service('DeviceData', function() {

		var savedData = {};
		var replaceDevices = {};
		var platofrmId;
		var currentDate;
		var selectedApicEm;
		var token;
		var apicemVersion;
		var billProducts;

		function setDeviceData(deviceData) {
			savedData = deviceData;
		}
		function getDeviceData() {
			return savedData;
		}

		function setPlatformId(platformId) {
			platofrmId = platformId;
		}
		function getDeviceData() {
			return savedData;
		}

		function setCurrentDate(currentDate) {
			currentDate = currentDate;
		}
		function getCurrentDate() {
			return currentDate;
		}

		function getPlatformId() {
			return platofrmId;
		}

		function getSelectedApicEm() {
			return selectedApicEm;
		}

		function setSelectedApicEm(selectedApicEm) {
			selectedApicEm = selectedApicEm;
		}

		function getToken() {
			return token;
		}

		function setToken(token) {
			token = token;
		}

		function getApicemVersion() {
			return apicemVersion;
		}

		function setApicemVersion(apicemVersion) {
			apicemVersion = apicemVersion;
		}

		function getBillProducts() {
			return billProducts;
		}

		function setBillProducts(billProducts) {
			billProducts = billProducts;
		}

		return {
			setDeviceData : setDeviceData,
			getDeviceData : getDeviceData,
			setPlatformId : setPlatformId,
			getPlatformId : getPlatformId,
			setCurrentDate : setCurrentDate,
			getCurrentDate : getCurrentDate,
			getSelectedApicEm : getSelectedApicEm,
			setSelectedApicEm : setSelectedApicEm,
			getToken : getToken,
			setToken : setToken,
			getApicemVersion : getApicemVersion,
			setApicemVersion : setApicemVersion,
			setBillProducts : setBillProducts,
			getBillProducts : getBillProducts
		}
	});

	as.directive('msg', function() {
		return {
			restrict : 'EA',
			link : function(scope, element, attrs) {
				var key = attrs.key;
				if (attrs.keyExpr) {
					scope.$watch(attrs.keyExpr, function(value) {
						key = value;
						element.text($.i18n.prop(value));
					});
				}
				scope.$watch('language()', function(value) {
					element.text($.i18n.prop(key));
				});
			}
		};
	});
}());