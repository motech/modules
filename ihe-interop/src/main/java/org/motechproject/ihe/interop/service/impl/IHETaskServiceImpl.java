package org.motechproject.ihe.interop.service.impl;

import org.motechproject.ihe.interop.domain.HL7Recipient;
import org.motechproject.ihe.interop.service.HL7RecipientsService;
import org.motechproject.ihe.interop.service.IHETaskService;
import org.motechproject.ihe.interop.service.IHETemplateDataService;
import org.motechproject.ihe.interop.task.IHEChannelRequestBuilder;
import org.motechproject.ihe.interop.domain.CdaTemplate;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.service.ChannelService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link IHETaskService}.
 */
@Service("iheTaskService")
public class IHETaskServiceImpl implements IHETaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IHETaskServiceImpl.class);

    @Autowired
    private ChannelService channelService;

    @Autowired
    private BundleContext bundleContext;

    @Autowired
    private HL7RecipientsService hl7RecipientsService;

    @Autowired
    private IHETemplateDataService iheTemplateService;

    @PostConstruct
    public void init() {
        updateChannel();
    }

    @Override
    public void templateChanged(CdaTemplate cdaTemplate) {
        LOGGER.debug("Template with {} name was created/updated", cdaTemplate.getTemplateName());
        updateChannel();
    }

    @Override
    public void templateRemoved(CdaTemplate cdaTemplate) {
        updateChannel();
    }

    @Override
    public void preTemplateRemoved(CdaTemplate cdaTemplate) {
        LOGGER.debug("Template with {} name was removed", cdaTemplate.getTemplateName());
    }

    private void updateChannel() {
        Collection<HL7Recipient> hl7Recipients = hl7RecipientsService.getAllRecipients();
        List<CdaTemplate> cdaTemplates = iheTemplateService.retrieveAll();

        IHEChannelRequestBuilder iheChannelRequestBuilder = new IHEChannelRequestBuilder(bundleContext, cdaTemplates, hl7Recipients);

        LOGGER.debug("Updating IHE Interop task channel...");
        ChannelRequest channelRequest = iheChannelRequestBuilder.build();
        if (channelRequest.getActionTaskEvents().size() > 0) {
            channelService.registerChannel(channelRequest);
        } else {
            LOGGER.debug("No IHE Interop Task Actions, cannot register IHE Interop Task Channel");
            // We must unregister channel if it exist
            channelService.unregisterChannel(bundleContext.getBundle().getSymbolicName());
        }
    }
}
