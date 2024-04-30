package org.openea.eap.module.crm.service.customer;

import org.openea.eap.framework.common.util.object.BeanUtils;
import org.openea.eap.module.crm.controller.admin.customer.vo.poolconfig.CrmCustomerPoolConfigSaveReqVO;
import org.openea.eap.module.crm.dal.dataobject.customer.CrmCustomerPoolConfigDO;
import org.openea.eap.module.crm.dal.mysql.customer.CrmCustomerPoolConfigMapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Objects;

import static org.openea.eap.module.crm.enums.LogRecordConstants.*;

/**
 * 客户公海配置 Service 实现类
 *
 * @author Wanwan
 */
@Service
@Validated
public class CrmCustomerPoolConfigServiceImpl implements CrmCustomerPoolConfigService {

    @Resource
    private CrmCustomerPoolConfigMapper customerPoolConfigMapper;

    @Override
    public CrmCustomerPoolConfigDO getCustomerPoolConfig() {
        return customerPoolConfigMapper.selectOne();
    }

    @Override
    @LogRecord(type = CRM_CUSTOMER_POOL_CONFIG_TYPE, subType = CRM_CUSTOMER_POOL_CONFIG_SUB_TYPE, bizNo = "{{#poolConfigId}}",
            success = CRM_CUSTOMER_POOL_CONFIG_SUCCESS)
    public void saveCustomerPoolConfig(CrmCustomerPoolConfigSaveReqVO saveReqVO) {
        // 1. 存在，则进行更新
        CrmCustomerPoolConfigDO dbConfig = getCustomerPoolConfig();
        CrmCustomerPoolConfigDO poolConfig = BeanUtils.toBean(saveReqVO, CrmCustomerPoolConfigDO.class);
        if (Objects.nonNull(dbConfig)) {
            customerPoolConfigMapper.updateById(poolConfig.setId(dbConfig.getId()));
            // 记录操作日志上下文
            LogRecordContext.putVariable("isPoolConfigUpdate", Boolean.TRUE);
            LogRecordContext.putVariable("poolConfigId", poolConfig.getId());
            return;
        }

        // 2. 不存在，则进行插入
        customerPoolConfigMapper.insert(poolConfig);
        // 记录操作日志上下文
        LogRecordContext.putVariable("isPoolConfigUpdate", Boolean.FALSE);
        LogRecordContext.putVariable("poolConfigId", poolConfig.getId());
    }

}
