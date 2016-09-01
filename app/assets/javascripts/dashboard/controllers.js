/**
 * Dashboard controllers.
 */
define([], function() {
  'use strict';

  /**
   * user is not a service, but stems from userResolve (Check ../user/services.js) object used by dashboard.routes.
   */
  var DashboardCtrl = function($scope, $http, user) {
    $scope.user = user;
    $scope.retriveUserProfile = function() {
      //alert("Functiona call "+$scope.user.id);
      $http.get('/contact/'+$scope.user.id).success(function(data) {
        $scope.contact = data.contact;
        $scope.account = data.account;
        //alert("Result:"+$scope.account.BillingAddress.street);
      });
    };
  };
  DashboardCtrl.$inject = ['$scope', '$http', 'user'];

  var AdminDashboardCtrl = function($scope, user) {
    $scope.user = user;
  };
  AdminDashboardCtrl.$inject = ['$scope', 'user'];

  return {
    DashboardCtrl: DashboardCtrl,
    AdminDashboardCtrl: AdminDashboardCtrl
  };

});
