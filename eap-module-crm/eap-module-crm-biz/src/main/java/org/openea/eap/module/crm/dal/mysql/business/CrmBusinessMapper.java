package org.openea.eap.module.crm.dal.mysql.business;

import org.openea.eap.framework.common.pojo.PageResult;
import org.openea.eap.framework.mybatis.core.mapper.BaseMapperX;
import org.openea.eap.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.openea.eap.framework.mybatis.core.query.MPJLambdaWrapperX;
import org.openea.eap.module.crm.controller.admin.business.vo.business.CrmBusinessPageReqVO;
import org.openea.eap.module.crm.dal.dataobject.business.CrmBusinessDO;
import org.openea.eap.module.crm.dal.dataobject.contract.CrmContractDO;
import org.openea.eap.module.crm.enums.common.CrmBizTypeEnum;
import org.openea.eap.module.crm.util.CrmPermissionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 商机 Mapper
 *
 * @author ljlleo
 */
@Mapper
public interface CrmBusinessMapper extends BaseMapperX<CrmBusinessDO> {

    default int updateOwnerUserIdById(Long id, Long ownerUserId) {
        return update(new LambdaUpdateWrapper<CrmBusinessDO>()
                .eq(CrmBusinessDO::getId, id)
                .set(CrmBusinessDO::getOwnerUserId, ownerUserId));
    }

    default PageResult<CrmBusinessDO> selectPageByCustomerId(CrmBusinessPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmBusinessDO>()
                .eq(CrmBusinessDO::getCustomerId, pageReqVO.getCustomerId()) // 指定客户编号
                .likeIfPresent(CrmBusinessDO::getName, pageReqVO.getName())
                .orderByDesc(CrmBusinessDO::getId));
    }

    default PageResult<CrmBusinessDO> selectPageByContactId(CrmBusinessPageReqVO pageReqVO, Collection<Long> businessIds) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmBusinessDO>()
                .in(CrmBusinessDO::getId, businessIds) // 指定商机编号
                .likeIfPresent(CrmBusinessDO::getName, pageReqVO.getName())
                .orderByDesc(CrmBusinessDO::getId));
    }

    default PageResult<CrmBusinessDO> selectPage(CrmBusinessPageReqVO pageReqVO, Long userId) {
        MPJLambdaWrapperX<CrmBusinessDO> query = new MPJLambdaWrapperX<>();
        // 拼接数据权限的查询条件
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_BUSINESS.getType(),
                CrmBusinessDO::getId, userId, pageReqVO.getSceneType(), Boolean.FALSE);
        // 拼接自身的查询条件
        query.selectAll(CrmBusinessDO.class)
                .likeIfPresent(CrmBusinessDO::getName, pageReqVO.getName())
                .orderByDesc(CrmBusinessDO::getId);
        return selectJoinPage(pageReqVO, CrmBusinessDO.class, query);
    }

    default Long selectCountByStatusTypeId(Long statusTypeId) {
        return selectCount(CrmBusinessDO::getStatusTypeId, statusTypeId);
    }

    default List<CrmBusinessDO> selectListByCustomerIdOwnerUserId(Long customerId, Long ownerUserId){
        return selectList(new LambdaQueryWrapperX<CrmBusinessDO>()
                .eq(CrmBusinessDO::getCustomerId, customerId)
                .eq(CrmBusinessDO::getOwnerUserId, ownerUserId));
    }

}
