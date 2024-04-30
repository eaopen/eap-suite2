package org.openea.eap.module.crm.service.receivable.listener;

import org.openea.eap.module.bpm.event.BpmProcessInstanceStatusEvent;
import org.openea.eap.module.bpm.event.BpmProcessInstanceStatusEventListener;
import org.openea.eap.module.crm.service.receivable.CrmReceivableService;
import org.openea.eap.module.crm.service.receivable.CrmReceivableServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 回款审批的结果的监听器实现类
 *
 * @author HUIHUI
 */
@Component
public class CrmReceivableStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private CrmReceivableService receivableService;

    @Override
    public String getProcessDefinitionKey() {
        return CrmReceivableServiceImpl.BPM_PROCESS_DEFINITION_KEY;
    }

    @Override
    public void onEvent(BpmProcessInstanceStatusEvent event) {
        receivableService.updateReceivableAuditStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
