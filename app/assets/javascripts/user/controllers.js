/**
 * User controllers.
 */
define([], function() {
  'use strict';

  var LoginCtrl = function($scope, $location, userService) {
    $scope.credentials = {};

    $scope.login = function(credentials) {
      userService.loginUser(credentials).then(function(/*user*/) {
        $location.path('/dashboard');
      }, function() {
        alert("Incorrect username/password");
      });
    };
  };
  LoginCtrl.$inject = ['$scope', '$location', 'userService'];

  var UserCtrl = function($scope, $routeParams, $window, $http) {
    $scope.setUserPasswd = function () {
      //alert("In submit form "+ $scope.contact.Id);
      if ($scope.credentials.password !== $scope.credentials.cnfpassword) {
        $window.alert("Passwords do not match, please enter again!");
      } else if ($scope.credentials.password.length < 8) {
        $window.alert("Password length should be minimum 8 characters long, please enter again!");
      } else {
        var data = {
          id : $scope.contact.Id,
          email : $scope.contact.Email,
          password : $scope.credentials.password
        };
        $http.put('/setuserpasswd', JSON.stringify(data))
        .success(function(response) {
          $scope.success = response.success;
          //alert("Result is " + $scope.success);
          if ($scope.success) {
            $window.location.href = '/#/actconf';
          } else {
            $window.location.href = '/#/actfail';
          }
        });
      }
    };

    $scope.retriveContact = function () {
      //$window.alert("Retrive contact, with :"+$routeParams.token);
      $http.get('/retrieveuser?token='+$routeParams.token).success(function(data) {
        $scope.contact = data;
        if ($scope.contact.length === 0) {
          $scope.validToken = false;
          $scope.message = "ERROR: The token '"+$routeParams.token+"' is not valid !";
        } else {
          $scope.validToken = true;
        }
      });
    };

    $scope.reqResetPasswd = function() {
      $http.get('/reqResetPasswd?email='+$scope.credentials.email).success(function(response) {
        $scope.success = response.success;
        $scope.result = true;
      });
    };
  };
  UserCtrl.$inject = ['$scope', '$routeParams', '$window', '$http'];

  return {
    LoginCtrl: LoginCtrl,
    UserCtrl: UserCtrl
  };

});
