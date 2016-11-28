package org.motechproject.dhis2.web;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.OrgUnit;
import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.service.DataElementService;
import org.motechproject.dhis2.service.OrgUnitService;
import org.motechproject.dhis2.service.ProgramService;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.motechproject.dhis2.service.TrackedEntityService;
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
    private ProgramService programService;
    @Autowired
    private TrackedEntityAttributeService trackedEntityAttributeService;
    @Autowired
    private TrackedEntityService trackedEntityService;
    @Autowired
    private OrgUnitService orgUnitService;
    @Autowired
    private DataElementService dataElementService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/programs", method = RequestMethod.GET)
    @ResponseBody
    public List<Program> getPrograms() {
        return programService.findAll();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/trackedEntityAttributes", method = RequestMethod.GET)
    public List<TrackedEntityAttribute> getAttributes() {
        return trackedEntityAttributeService.findAll();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/trackedEntities", method = RequestMethod.GET)
    public List<TrackedEntity> getTrackedEntities() {
        return trackedEntityService.findAll();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/orgUnits", method = RequestMethod.GET)
    public List<OrgUnit> getOrgUnits() {
        return orgUnitService.findAll();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/dataElements", method = RequestMethod.GET)
    public List<DataElement> getDataElements() {
        return dataElementService.findAll();
    }
}
