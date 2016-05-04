(function () {

    'use strict';

    /* Controllers */

    var controllers = angular.module('cmslite.controllers', []);

    controllers.controller('CmsResourceCtrl', function ($scope, $rootScope, $http, Resources, $location, ModalFactory, LoadingModal) {
        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
        }, {
            show: true,
            button: '#cmslite-resource-filters'
        });

        $scope.select = {};
        $scope.mode = 'read';
        $scope.resourceType = 'string';
        $scope.usedLanguages = [];

        $scope.getLanguages = function () {
            $http.get('../cmsliteapi/resource/all/languages').success(function (data) {
                $scope.usedLanguages = data;
                $rootScope.usedLanguages = $scope.usedLanguages;
            });
        };

        $scope.changeResourceType = function (type) {
            $scope.resourceType = type;
        };

        $scope.changeMode = function(mode) {
            $scope.mode = mode;
        };

        $scope.showNewResourceModal = function () {
            $scope.resourceType = 'string';
            $scope.mode = 'read';
            $scope.select = { value: '' };
            $('#newResourceForm').resetForm();
            $('#newResourceModal').modal('show');
        };

        $scope.getResourceUrl = function(language, name, type){
            var resourceUrl=  $location.absUrl() ;
            resourceUrl =  resourceUrl.substring(0, resourceUrl.length - 31) ;
            resourceUrl =  resourceUrl + '/cmsliteapi/' + type + '/' +language + '/' + name + '/';
            return resourceUrl;
        };

        $scope.showResource = function(type, language, name) {
            switch (type) {
            case 'string':
                $scope.select = Resources.get({ type: type, language: language, name: name}, function () {
                    $scope.resourceUrl = $scope.getResourceUrl($scope.select.language, $scope.select.name, type);
                    $('#stringResourceModal').modal('show');
                });
                break;
            case 'stream':
                $scope.select = Resources.get({ type: type, language: language, name: name}, function () {
                    $scope.resourceUrl = $scope.getResourceUrl($scope.select.language, $scope.select.name, type);
                    $('#streamResourceModal').modal('show');
                });
                break;
            }
        };

        $scope.editStringResource = function() {
            if ($scope.validateField('stringResourceForm', 'value')) {
                LoadingModal.open();

                $('#stringResourceForm').ajaxSubmit({
                    success: function () {
                        $scope.select = Resources.get({ type: 'string', language: $scope.select.language, name: $scope.select.name}, function () {
                            $scope.changeMode('read');
                            LoadingModal.close();
                        });
                    },
                    error: function (response) {
                        ModalFactory.showErrorWithStackTrace('cmslite.error.resource.save', 'cmslite.header.error', response);
                        LoadingModal.close();
                    }
                });
            }
        };

        $scope.editStreamResource = function() {
            if ($scope.validateFileField('streamResourceForm', 'fileInput')) {
                LoadingModal.open();

                $('#streamResourceForm').ajaxSubmit({
                    success: function () {
                        $scope.select = Resources.get({ type: 'stream', language: $scope.select.language, name: $scope.select.name}, function () {
                            $scope.changeMode('read');
                            LoadingModal.close();
                        });
                    },
                    error: function (response) {
                        ModalFactory.showErrorWithStackTrace('cmslite.error.resource.save', 'cmslite.header.error', response);
                        LoadingModal.close();
                    }
                });
            }
        };

        $scope.removeResource = function(type, resource) {
            ModalFactory.showConfirm({
                title: $scope.msg('cmslite.header.confirm'),
                message: $scope.msg('cmslite.header.confirm.remove'),
                type: 'type-warning',
                callback: function(result) {
                    if (result) {
                        $scope.select.$remove({ type: type, language: resource.language, name: resource.name}, function () {
                            $scope.select = {};
                            $('#cms-lite-table').trigger('reloadGrid');
                            $('#' + type + 'ResourceModal').modal('hide');
                            $scope.getLanguages();
                        }, function () {
                            ModalFactory.showErrorAlert('cmslite.error.removed', 'cmslite.header.error');
                        });
                    }
                }
            });
        };

        $scope.saveNewResource = function () {
            if ($scope.validateForm('newResourceForm')) {
                LoadingModal.open();
                $('#newResourceForm').ajaxSubmit({
                    success: function () {
                        $('#cms-lite-table').trigger('reloadGrid');
                        $('#newResourceModal').modal('hide');

                        $scope.getLanguages();
                        LoadingModal.close();
                    },
                    error: function (response) {
                        ModalFactory.showErrorWithStackTrace('cmslite.error.resource.save', 'cmslite.header.error', response);
                        LoadingModal.close();
                    }
                });
            }
        };

        $scope.validateForm = function (formId) {
            var name = $scope.validateField(formId, 'name'),
                language = $scope.validateField(formId, 'language'),
                value = $scope.validateField(formId, 'value'),
                contentFile = $scope.validateFileField(formId, 'fileInput');

            return name && language && ($scope.resourceType === 'string' ? value : contentFile);
        };

        $scope.validateField = function (formId, key) {
            var field = $('#' + formId + ' #' + key),
                hint = field.siblings('span.form-hint'),
                validate = field.val() !== undefined && field.val() !== '';

            if (validate) {
                hint.hide();
            } else {
                hint.show();
            }

            return validate;
        };

        $scope.validateFileField = function (formId, key) {
            var field = $('#' + formId + ' #' + key),
                hint = field.parent().siblings('span.form-hint'),
                validate = field.val() !== undefined && field.val() !== '';

            if (validate) {
                hint.hide();
            } else {
                hint.show();
            }

            return validate;
        };

        $scope.getLanguages();
    });

}());
