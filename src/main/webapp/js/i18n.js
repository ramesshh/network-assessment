(function () {
    var as = angular.module('exampleApp.i18n',[]);

    as.service('i18n', function () {
        var self = this;
        this.setLanguage = function (language) {
            $.i18n.properties({
                name: 'messages',
                path: 'i18n/',
                mode: 'map',
                language: language,
                callback: function () {
                    self.language = language;
                }
            });
        };
        this.setLanguage('en');
    });
    
    as.service('DeviceData', function () {
    	var savedData = {};
    	var replaceDevices = {};
    	var platofrmId;
    	var currentDate;
    	 
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

    	 return {
    	  setDeviceData: setDeviceData,
    	  getDeviceData: getDeviceData,
    	  setPlatformId: setPlatformId,
    	  getPlatformId: getPlatformId,
    	  setCurrentDate:setCurrentDate,
    	  getCurrentDate:getCurrentDate
    	 }
    });

    as.directive('msg', function () {
        return {
            restrict: 'EA',
            link: function (scope, element, attrs) {
                var key = attrs.key;
                if (attrs.keyExpr) {
                    scope.$watch(attrs.keyExpr, function (value) {
                        key = value;
                        element.text($.i18n.prop(value));
                    });
                }
                scope.$watch('language()', function (value) {
                    element.text($.i18n.prop(key));
                });
            }
        };
    });
}());