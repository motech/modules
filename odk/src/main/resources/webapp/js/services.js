(function () {
    'use strict';

    /* Services */

    var services = angular.module('odk.services', ['ngResource']);

    services.factory('Config', function ($resource) {
        return $resource('../odk/configs/:name');
    });

    services.factory('Import', function ($resource) {
        return $resource('../odk/import/:name');
    });

    services.factory('FormDefinition', function ($resource) {
        return $resource('../odk/formDefinitions/:name', {name: '@name', id: '@id'}, {
            findById: {
                method: 'GET',
                params: {
                    id: '@id'
                }
            }
        });
    });


    services.factory('Verify', function ($resource) {
        return $resource('../odk/verify', {}, {
            kobo: {
                method: 'POST',
                url: "../odk/verify/kobo"
            },

            ona: {
                method: 'POST',
                url: "../odk/verify/ona"
            },

            odk: {
                method: 'POST',
                url: "../odk/verify/odk"
            }
        });
    });


}());
