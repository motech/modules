package org.motechproject.dhis2.web;

import org.motechproject.dhis2.dto.DataElementDto;
import org.motechproject.dhis2.dto.OrgUnitDto;
import org.motechproject.dhis2.dto.ProgramDto;
import org.motechproject.dhis2.dto.TrackedEntityAttributeDto;
import org.motechproject.dhis2.dto.TrackedEntityDto;
import org.motechproject.dhis2.service.Dhis2WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * The controller that handles requests for information about the DHIS2 schema.
 */
@Controller
public class DhisSchemaController {

    @Autowired
    private Dhis2WebService dhis2WebService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/programs", method = RequestMethod.GET)
    @ResponseBody
    public List<ProgramDto> getPrograms() {
        return dhis2WebService.getPrograms();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/trackedEntityAttributes", method = RequestMethod.GET)
    public List<TrackedEntityAttributeDto> getAttributes() {
        return dhis2WebService.getAttributes();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/trackedEntities", method = RequestMethod.GET)
    public List<TrackedEntityDto> getTrackedEntities() {
        return dhis2WebService.getTrackedEntities();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/orgUnits", method = RequestMethod.GET)
    public List<OrgUnitDto> getOrgUnits() {
        return dhis2WebService.getOrgUnits();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/dataElements", method = RequestMethod.GET)
    public List<DataElementDto> getDataElements() {
        return dhis2WebService.getDataElements();
    }
}
