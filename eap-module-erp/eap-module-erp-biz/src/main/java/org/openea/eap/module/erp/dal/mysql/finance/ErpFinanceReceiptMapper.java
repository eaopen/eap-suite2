package org.openea.eap.module.erp.dal.mysql.finance;

import org.openea.eap.framework.common.pojo.PageResult;
import org.openea.eap.framework.mybatis.core.mapper.BaseMapperX;
import org.openea.eap.framework.mybatis.core.query.MPJLambdaWrapperX;
import org.openea.eap.module.erp.controller.admin.finance.vo.receipt.ErpFinanceReceiptPageReqVO;
import org.openea.eap.module.erp.dal.dataobject.finance.ErpFinanceReceiptDO;
import org.openea.eap.module.erp.dal.dataobject.finance.ErpFinanceReceiptItemDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * ERP 收款单 Mapper
 *
 */
@Mapper
public interface ErpFinanceReceiptMapper extends BaseMapperX<ErpFinanceReceiptDO> {

    default PageResult<ErpFinanceReceiptDO> selectPage(ErpFinanceReceiptPageReqVO reqVO) {
        MPJLambdaWrapperX<ErpFinanceReceiptDO> query = new MPJLambdaWrapperX<ErpFinanceReceiptDO>()
                .likeIfPresent(ErpFinanceReceiptDO::getNo, reqVO.getNo())
                .betweenIfPresent(ErpFinanceReceiptDO::getReceiptTime, reqVO.getReceiptTime())
                .eqIfPresent(ErpFinanceReceiptDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(ErpFinanceReceiptDO::getCreator, reqVO.getCreator())
                .eqIfPresent(ErpFinanceReceiptDO::getFinanceUserId, reqVO.getFinanceUserId())
                .eqIfPresent(ErpFinanceReceiptDO::getAccountId, reqVO.getAccountId())
                .eqIfPresent(ErpFinanceReceiptDO::getStatus, reqVO.getStatus())
                .likeIfPresent(ErpFinanceReceiptDO::getRemark, reqVO.getRemark())
                .orderByDesc(ErpFinanceReceiptDO::getId);
        if (reqVO.getBizNo() != null) {
            query.leftJoin(ErpFinanceReceiptItemDO.class, ErpFinanceReceiptItemDO::getReceiptId, ErpFinanceReceiptDO::getId)
                    .eq(reqVO.getBizNo() != null, ErpFinanceReceiptItemDO::getBizNo, reqVO.getBizNo())
                    .groupBy(ErpFinanceReceiptDO::getId); // 避免 1 对多查询，产生相同的 1
        }
        return selectJoinPage(reqVO, ErpFinanceReceiptDO.class, query);
    }

    default int updateByIdAndStatus(Long id, Integer status, ErpFinanceReceiptDO updateObj) {
        return update(updateObj, new LambdaUpdateWrapper<ErpFinanceReceiptDO>()
                .eq(ErpFinanceReceiptDO::getId, id).eq(ErpFinanceReceiptDO::getStatus, status));
    }

    default ErpFinanceReceiptDO selectByNo(String no) {
        return selectOne(ErpFinanceReceiptDO::getNo, no);
    }

}
