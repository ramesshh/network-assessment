//Define a function scope, variables used inside it will NOT be globally visible.
(function () {

    var
            //the HTTP headers to be used by all requests
            httpHeaders,
            //the message to be shown to the user
            message,
            //Define the main module.
            //The module is accessible everywhere using "angular.module('angularspring')", therefore global variables can be avoided totally.
            as = angular.module('exampleApp', ['ngRoute', 'ngResource', 'ngCookies', 'ui.bootstrap', 'ngMessages', 'exampleApp.i18n', 'exampleApp.services', 'exampleApp.controllers', 'exampleApp.filters','smart-table']);

    as.config(function ($routeProvider, $httpProvider) {
        //configure the rounting of ng-view
        $routeProvider
                .when('/',
                        {templateUrl: 'partials/login.html',
                            publicAccess: true})
                .when('/login',
                        {templateUrl: 'partials/login.html',
                            publicAccess: true})
                 .when('/discovery/:platformId/replace',
                        {controller: 'ReplaceCtrl',
                		 templateUrl: 'partials/em/replace.html'})       
                .when('/discovery/:platformId/questionare/:family',
                        {controller: 'QuestionCtrl',
                		 templateUrl: 'partials/em/questionare.html'})
                .when('/discovery',
                		{controller: 'SearchController',
                			templateUrl: 'partials/em/search.html'});    

        //configure $http to catch message responses and show them
        $httpProvider.interceptors.push(function ($q) {
            var setMessage = function (response) {
                //if the response has a text and a type property, it is a message to be shown
                if (response.data.text && response.data.type) {
                    message = {
                        text: response.data.text,
                        type: response.data.type,
                        show: true
                    };
                }
            };
//            return function(promise) {
//                return promise.then(
//                        //this is called after each successful server request
//                                function(response) {
//                                    setMessage(response);
//                                    return response;
//                                },
//                                //this is called after each unsuccessful server request
//                                        function(response) {
//                                            setMessage(response);
//                                            return $q.reject(response);
//                                        }
//                                );
//                            };

            return {
                //this is called after each successful server request
                'response': function (response) {
                    // console.log('request:' + response);
                    setMessage(response);
                    return response || $q.when(response);
                },
                //this is called after each unsuccessful server request
                'responseError': function (response) {
                    //console.log('requestError:' + response);
                    setMessage(response);
                    return $q.reject(response);
                }

            };
        });

        //configure $http to show a login dialog whenever a 401 unauthorized response arrives
//        $httpProvider.responseInterceptors.push(function ($rootScope, $q) {
//            return function (promise) {
//                return promise.then(
//                    //success -> don't intercept
//                    function (response) {
//                        return response;
//                    },
//                    //error -> if 401 save the request and broadcast an event
//                    function (response) {
//                        if (response.status === 401) {
//                            var deferred = $q.defer(),
//                                req = {
//                                    config: response.config,
//                                    deferred: deferred
//                                };
//                            $rootScope.requests401.push(req);
//                            $rootScope.$broadcast('event:loginRequired');
//                            return deferred.promise;
//                        }
//                        return $q.reject(response);
//                    }
//                );
//            };
//        });

        $httpProvider.interceptors.push(function ($rootScope, $q) {

            return {
                'request': function (config) {
                    // console.log('request:' + config);
                    return config || $q.when(config);
                },
                'requestError': function (rejection) {
                    // console.log('requestError:' + rejection);
                    return rejection;
                },
                //success -> don't intercept
                'response': function (response) {
                    // console.log('response:' + response);
                    return  response || $q.when(response);
                },
                //error -> if 401 save the request and broadcast an event
                'responseError': function (response) {
                    console.log('responseError:' + response);
                    if (response.status === 401) {
                        var deferred = $q.defer(),
                                req = {
                                    config: response.config,
                                    deferred: deferred
                                };
                        $rootScope.requests401.push(req);
                        $rootScope.$broadcast('event:loginRequired');
                        return deferred.promise;
                    }
                    return $q.reject(response);
                }

            };
        });


        httpHeaders = $httpProvider.defaults.headers;
    });


    as.run(function ($rootScope, $http, $route, $location, base64) {
        //make current message accessible to root scope and therefore all scopes
        $rootScope.message = function () {
            return message;
        };

        /**
         * Holds all the requests which failed due to 401 response.
         */
        $rootScope.requests401 = [];

        $rootScope.$on('event:loginRequired', function () {
            //$('#login').modal('show');
            $location.path('/login');
        });

        /**
         * On 'event:loginConfirmed', resend all the 401 requests.
         */
        $rootScope.$on('event:loginConfirmed', function () {
            var i,
                    requests = $rootScope.requests401,
                    retry = function (req) {
                        $http(req.config).then(function (response) {
                            req.deferred.resolve(response);
                        });
                    };

            for (i = 0; i < requests.length; i += 1) {
                retry(requests[i]);
            }
            $rootScope.requests401 = [];



          //  $location.path('/posts');
            $location.path('/discovery');
        });

        /**
         * On 'event:loginRequest' send credentials to the server.
         */
        $rootScope.$on('event:loginRequest', function (event, username, password) {
            httpHeaders.common['Authorization'] = 'Basic ' + base64.encode(username + ':' + password);
            console.log('httpHeaders.common[\'Authorization\']@' + httpHeaders.common['Authorization'] + ':::' + username + ':' + password);
            $http.get('api/self')
                    .success(function (data) {
                        $rootScope.user = data;
                        $rootScope.$broadcast('event:loginConfirmed');
                    })
                    .error(function (data) {
                        console.log('login failed...');
                    });
        });

        /**
         * On 'logoutRequest' invoke logout on the server and broadcast 'event:loginRequired'.
         */
        $rootScope.$on('event:logoutRequest', function () {
            httpHeaders.common['Authorization'] = null;
        });

        var routesOpenToPublic = [];
        angular.forEach($route.routes, function (route, path) {
            // push route onto routesOpenToPublic if it has a truthy publicAccess value
            route.publicAccess && (routesOpenToPublic.push(path));
        });

        $rootScope.$on('$routeChangeStart', function (event, nextLoc, currentLoc) {
            //console.log('fire event@$routeChangeStart');
            var closedToPublic = (-1 === routesOpenToPublic.indexOf($location.path()));
            if (closedToPublic && !$rootScope.user) {
                //console.log('login required...');             
                $rootScope.$broadcast('event:loginRequired');
            } else if (!!$rootScope.user) {
                //console.log('already logged in...'); 
                if (!!nextLoc && nextLoc.templateUrl == 'partials/login.html') {
                    $location.path('/login');
                } else {
                    //do nothing...
                }
            }
        });
    });
}());