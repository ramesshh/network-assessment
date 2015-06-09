(function () {
    var as = angular.module('exampleApp.controllers', ['smart-table']);

    as.controller('MainController', function ($q, $scope, $rootScope, $http, i18n, $location) {
        var load = function () {
        };

        load();

        $scope.language = function () {
            return i18n.language;
        };
        $scope.setLanguage = function (lang) {
            i18n.setLanguage(lang);
        };
        $scope.activeWhen = function (value) {
            return value ? 'active' : '';
        };

        $scope.path = function () {
            return $location.url();
        };

// $scope.login = function() {
// console.log('username:password @' + $scope.username + ',' + $scope.password);
// $scope.$emit('event:loginRequest', $scope.username, $scope.password);
// //$('#login').modal('hide');
// };
        $scope.logout = function () {
            $rootScope.user = null;
            $scope.username = $scope.password = null;
            $scope.$emit('event:logoutRequest');
            $location.url('/login');
        };

    });

    as.controller('LoginController', function ($scope, $rootScope, $http, base64, $location) {

        $scope.login = function () {
            console.log('username:password @' + $scope.username + ',' + $scope.password);
            $scope.$emit('event:loginRequest', $scope.username, $scope.password);
            // $('#login').modal('hide');
        };
    });

    as.controller('PasswordController', function ($scope, $rootScope, $http, base64, $location) {
        var actionUrl = 'api/self?action=CHANGE_PWD';
        $scope.data = {};

        $scope.changePwd = function () {
            var username = $rootScope.user.username;
            var newpwd = $scope.data.newPassword;
            console.log('username@' + username);
            console.log('password new @' + newpwd);
            $http.put(actionUrl, $scope.data)
                    .success(function (data) {
                        console.log(data);
                        $rootScope.user.password = newpwd;
                        $http.default.headers.common['Authorization'] = 'Basic ' + base64.encode(username + ':' + newpwd);
                        $location.url('/user/home');
                    })
                    .error(function (data) {
                        console.log(data);
                    });

        }

        $scope.cancel = function () {
            $location.url('/user/home');
        }

    });

    as.controller('ProfileController', function ($scope, $rootScope, $http, base64, $location) {
        var actionUrl = 'api/self?action=UPDATE_PROFILE',
                load = function () {
                    $scope.data = {};
                    $scope.data.displayName = $rootScope.user.displayName;
                };

        load();

        $scope.updateProfile = function () {
            var displayName = $scope.data.displayName;
            console.log("displaye Name is @" + displayName);
            $http.put(actionUrl, $scope.data)
                    .success(function (data) {
                        console.log(data);
                        $rootScope.user.displayName = displayName;
                        $location.url('/user/home');
                    })
                    .error(function (data) {
                        console.log(data);
                    });

        };

        $scope.cancel = function () {
            $location.url('/user/home');
        };

    });


    as.controller('UserAdminController', function ($scope, $http, i18n) {
        $scope.p = 1;
        var actionUrl = 'api/mgt/users/',
                load = function () {
                    $http.get(actionUrl + '?page=' + ($scope.p - 1)).success(function (data) {
                        $scope.users = data.content;
                        $scope.totalItems=data.totalElements;
                    });
                };

        load();

        $scope.roleOpts = ['USER', 'ADMIN'];
        $scope.user = {};
        
        $scope.search=function(){
            load();
        };

        $scope.delete = function (idx) {
            console.log('delete index @' + idx + ', id is@' + $scope.users[idx].id);
            if (confirm($.i18n.prop('confirm.delete'))) {
                $http.delete(actionUrl + $scope.users[idx].id).success(function () {
                    load();
                });
            }

        };

        $scope.initAdd = function () {
            $scope.user = {};
            $('#userDialog').modal('show');
        };

        $scope.save = function () {
            $http.post(actionUrl, $scope.user).success(function () {
                $('#userDialog').modal('hide');
                load();
            });
        };

        $scope.order = '+username';

        $scope.orderBy = function (property) {
            $scope.order = ($scope.order[0] === '+' ? '-' : '+') + property;
        };

        $scope.orderIcon = function (property) {
            return property === $scope.order.substring(1) ? $scope.order[0] === '+' ? 'glyphicon glyphicon-chevron-up' : 'glyphicon glyphicon-chevron-down' : '';
        };
    });

    as.controller('NewPostController', function ($scope, $http, i18n, $location) {
        var actionUrl = 'api/posts/';

        $scope.save = function () {
            $http.post(actionUrl, $scope.newPost).success(function () {
                $location.path('/posts');
            });
        };


        $scope.cancel = function () {
            $location.path('/posts');
        };

    });
    
    as.controller('DetailsController', function ($scope, $http, $routeParams, $q) {
        $scope.p = 1;
        var actionUrl = 'api/posts/',
                loadComments = function () {
                    $http.get(actionUrl + $routeParams.id + '/comments')
                            .success(function (data) {
                                $scope.comments = data.content;
                                $scope.totalItems = data.totalElements;
                            });
                },
                load = function () {
                    $q.all([
                        $http.get(actionUrl + $routeParams.id),
                        $http.get(actionUrl + $routeParams.id + '/comments')
                    ])
                            .then(function (result) {
                                $scope.post = result[0].data;
                                $scope.comments = result[1].data.content;
                                $scope.totalItems = result[1].data.totalElements;
                            });
                };

        load();

        $scope.newComment = {};

        $scope.save = function () {
            $http.post(actionUrl + $routeParams.id + '/comments', $scope.newComment).success(function () {
                $('#commentDialog').modal('hide');
                loadComments();
                $scope.newComment = {};
            });
        };

        $scope.delComment = function (idx) {
            $http.delete('api/comments/' + $scope.comments[idx].id).success(function () {
                $scope.comments.splice(idx, 1);
            });
        };

        $scope.addComment = function () {
            $('#commentDialog').modal('show');
        };

        $scope.search = function () {
            loadComments();
        };

    });

    as.controller('PostsController', function ($scope, $http, i18n) {
        $scope.p = 1;
        $scope.q = '';
        $scope.statusOpt = {'label': $.i18n.prop('ALL'), 'value': 'ALL'};
        $scope.statusOpts = [
            {'label': 'ALL', 'value': 'ALL'},
            {'label': 'DRAFT', 'value': 'DRAFT'},
            {'label': 'PUBLISHED', 'value': 'PUBLISHED'}
        ];

        var actionUrl = 'api/posts/',
                load = function () {
                    $http.get(actionUrl + '?q=' + $scope.q
                            + '&status=' + ($scope.statusOpt.value == 'ALL' ? '' : $scope.statusOpt.value)
                            + '&page=' + ($scope.p - 1))
                            .success(function (data) {
                                $scope.posts = data.content;
                                $scope.totalItems = data.totalElements;
                            });
                };

        load();

        $scope.search = function () {
            load();
        };

        $scope.toggleStatus = function (r) {
            $scope.statusOpt = r;
        };

        $scope.delPost = function (idx) {
            console.log('delete index @' + idx + ', id is@' + $scope.users[idx].id);
            if (confirm($.i18n.prop('confirm.delete'))) {
                $http.delete(actionUrl + $scope.posts[idx].id)
                        .success(function () {
                            $scope.posts.splice(idx, 1);
                        });
            }
        };

    });
    
    as.controller('SearchController', function ($scope, $http, i18n,$location,DeviceData) {
    		$scope.itemsByPage=5;
    		$scope.originalData ='';
    		var groupType ='platformId';
        	 var actionUrl = 'api/discovery/search';
	         load = function () {
	        		 $http.get(actionUrl+"?q="+$scope.q).success(function (data) {
	             	  console.log("Data is "+data);
	             	   $scope.originalData = data;
	             	   DeviceData.setDeviceData(data);
	                   $scope.devices = groupByData(data,groupType);
	               });
        	 }
        	 load();
        	 
        	 groupByData=function(data,groupBy){
          	   var groupByUrl = 'api/discovery/'+groupBy+'/groupby';
        	   $http.post(groupByUrl,data).success(function (response) {
        		   $scope.devices = response;
        		   
        	   });
           }
        	
        	
        	/*
			 * $scope.goToReplace = function(platformId){ var replaceUrl =
			 * 'api/discovery/'+platformId+'/replace';
			 * $http.post(replaceUrl,$scope.originalData).success(function
			 * (response) { $scope.replaceDevices = response;
			 * DeviceData.set(response);
			 * $location.path('/discovery/'+platformId+'/replace'); }); }
			 */

    });
    
    as.controller('ReplaceCtrl', function ($scope, $http, $routeParams, i18n, $location,DeviceData) {
    		$scope.platformId=$routeParams.platformId;
    		var actionUrl = 'api/discovery/'+$routeParams.platformId+'/replace';
    		var fullData = DeviceData.getDeviceData();
    		DeviceData.setPlatformId($routeParams.platformId);
    		var replaceItemData = [];
    		angular.forEach(fullData, function(device) {
    		  if(device.platformId ==$scope.platformId){
    			  replaceItemData.push(device);
    		  }
    		}, replaceItemData);
    		$scope.devices = replaceItemData;
    });
    
    as.controller('QuestionCtrl', function ($scope, $http, $routeParams, i18n, $location) {
    	$scope.platformId=$routeParams.platformId;
    	$scope.family=$routeParams.family;
    	
    	$scope.deviceFamily=[];
    	$scope.deviceConfig=[];
    	$scope.productCatalog=[];
    	
    	$scope.replacableProducts=[];
    	
    	$http.get('device-family-mapping.json').success(function(data) {
    		$scope.deviceFamily = data.deviceFamilyMapping;
		$http.get('device-config-mapping.json').success(function(data){ 
			$scope.deviceConfig = data.deviceConfigMapping;
		$http.get('product-catalog.json').success(function(data){ 
			$scope.productCatalog= data.products;
	    	angular.forEach($scope.deviceFamily, function(device) {
	  		  if(device.family ==$scope.family){
	  			angular.forEach($scope.deviceConfig, function(config) {
	  				if(device.family ==config.family){
	  					angular.forEach(config.products, function(prod) {
		  					angular.forEach($scope.productCatalog, function(prodCatalog) {
		  						if(prodCatalog.productId=prod.productId){
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
    	
    	console.log("Products::"+$scope.replacableProducts)
    });
    
    as.controller('ProductCtrl', function ($scope, $http, $routeParams, i18n, $location) {
    	  $scope.myInterval = 5000;
    	  var slides = $scope.slides = [];
    	  $scope.addSlide = function() {
    	    var newWidth = 600 + slides.length + 1;
    	    slides.push({
    	      image: 'http://placekitten.com/' + newWidth + '/300',
    	      text: ['More','Extra','Lots of','Surplus'][slides.length % 4] + ' ' +
    	        ['Cats', 'Kittys', 'Felines', 'Cutes'][slides.length % 4]
    	    });
    	  };
    	  for (var i=0; i<4; i++) {
    	    $scope.addSlide();
    	  }
    	});

}());