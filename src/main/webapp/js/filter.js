(function() {
    var app = angular.module('apicemApp.filters', []);

    app.filter('range', function() {
        return function(input, total) {
            total = parseInt(total);
            for (var i = 0; i < total; i++)
                input.push(i);
            return input;
        };
    });
    
    app.filter('urlEncode', function() {
    	return window.encodeURIComponent;
    });
   
}());


