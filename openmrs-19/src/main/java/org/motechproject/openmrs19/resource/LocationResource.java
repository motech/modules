package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.rest.HttpException;
import org.motechproject.openmrs19.resource.model.Location;
import org.motechproject.openmrs19.resource.model.LocationListResult;

public interface LocationResource {

    LocationListResult getAllLocations() throws HttpException;

    LocationListResult queryForLocationByName(String locationName) throws HttpException;

    Location getLocationById(String uuid) throws HttpException;

    Location createLocation(Location location) throws HttpException;

    void updateLocation(Location location) throws HttpException;

    void removeLocation(String locationId) throws HttpException;

}
