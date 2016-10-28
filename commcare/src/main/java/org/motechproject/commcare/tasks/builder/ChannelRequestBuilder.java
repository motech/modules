package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>ChannelRequestBuilder</code> class is responsible for building the
 * {@link org.motechproject.tasks.contract.ChannelRequest}. Such request is later on
 * used to register the channel for the Commcare module. To build the necessary triggers,
 * we use classes implementing {@link TriggerBuilder}.
 */
public class ChannelRequestBuilder {

    private static final String DISPLAY_NAME = "commcare";

    private BundleContext bundleContext;
    private CommcareConfigService configService;
    private CommcareSchemaService schemaService;

    /**
     * Creates an instance of the {@link ChannelRequestBuilder} class, which is used for building {@link ChannelRequest}
     * instances. The given {@code configService}, {@code schemaService}, {@code bundleContext} will be use for
     * building new instances.
     *
     * @param configService  the configuration service
     * @param schemaService the schema service
     * @param bundleContext  the bundle context
     */
    public ChannelRequestBuilder(CommcareConfigService configService, CommcareSchemaService schemaService,
                                 BundleContext bundleContext) {
        this.configService = configService;
        this.schemaService = schemaService;
        this.bundleContext = bundleContext;
    }

    /**
     * Builds an object of the {@link ChannelRequest} class.
     *
     * @return the created instance
     */
    public ChannelRequest buildChannelRequest() {

        FormTriggerBuilder formTriggerBuilder = new FormTriggerBuilder(schemaService, configService);
        CaseTriggerBuilder caseTriggerBuilder = new CaseTriggerBuilder(schemaService, configService);
        ReportTriggerBuilder reportTriggerBuilder = new ReportTriggerBuilder(schemaService, configService);
        CommonTriggerBuilder commonTriggerBuilder = new CommonTriggerBuilder(configService);

        // Actions
        ImportFormActionBuilder importFormActionBuilder = new ImportFormActionBuilder(configService);
        QueryStockLedgerActionBuilder queryStockLedgerActionBuilder = new QueryStockLedgerActionBuilder(configService);
        CaseActionBuilder caseActionBuilder = new CaseActionBuilder(configService);
        FormActionBuilder formActionBuilder = new FormActionBuilder(schemaService, configService);
        ReportActionBuilder reportActionBuilder = new ReportActionBuilder(schemaService, configService);

        List<TriggerEventRequest> triggers = formTriggerBuilder.buildTriggers();
        triggers.addAll(caseTriggerBuilder.buildTriggers());
        triggers.addAll(reportTriggerBuilder.buildTriggers());
        triggers.addAll(commonTriggerBuilder.buildTriggers());

        List<ActionEventRequest> actions = new ArrayList<>();
        actions.addAll(importFormActionBuilder.buildActions());
        actions.addAll(queryStockLedgerActionBuilder.buildActions());
        actions.addAll(caseActionBuilder.buildActions());
        actions.addAll(formActionBuilder.buildActions());
        actions.addAll(reportActionBuilder.buildActions());

        return new ChannelRequest(DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, triggers, actions);
    }
}
