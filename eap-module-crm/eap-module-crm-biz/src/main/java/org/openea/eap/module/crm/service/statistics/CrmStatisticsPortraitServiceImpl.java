package org.openea.eap.module.crm.service.statistics;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import org.openea.eap.framework.ip.core.Area;
import org.openea.eap.framework.ip.core.enums.AreaTypeEnum;
import org.openea.eap.framework.ip.core.utils.AreaUtils;
import org.openea.eap.module.crm.controller.admin.statistics.vo.portrait.*;
import org.openea.eap.module.crm.dal.mysql.statistics.CrmStatisticsPortraitMapper;
import org.openea.eap.module.system.api.dept.DeptApi;
import org.openea.eap.module.system.api.dept.dto.DeptRespDTO;
import org.openea.eap.module.system.api.user.AdminUserApi;
import org.openea.eap.module.system.api.user.dto.AdminUserRespDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.openea.eap.framework.common.util.collection.CollectionUtils.convertList;
import static org.openea.eap.framework.common.util.collection.CollectionUtils.convertMap;
import static org.openea.eap.framework.common.util.collection.MapUtils.findAndThen;

/**
 * CRM 客户画像 Service 实现类
 *
 * @author HUIHUI
 */
@Service
public class CrmStatisticsPortraitServiceImpl implements CrmStatisticsPortraitService {

    @Resource
    private CrmStatisticsPortraitMapper portraitMapper;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;

    @Override
    public List<CrmStatisticCustomerAreaRespVO> getCustomerSummaryByArea(CrmStatisticsPortraitReqVO reqVO) {
        // 1. 获得用户编号数组
        List<Long> userIds = getUserIds(reqVO);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        reqVO.setUserIds(userIds);

        // 2. 获取客户地区统计数据
        List<CrmStatisticCustomerAreaRespVO> list = portraitMapper.selectSummaryListGroupByAreaId(reqVO);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        // 3. 拼接数据
        List<Area> areaList = AreaUtils.getByType(AreaTypeEnum.PROVINCE, area -> area);
        areaList.add(new Area().setId(null).setName("未知")); // TODO @puhui999：是不是 65 find 的逻辑改下；不用 findAndThen，直接从 areaMap 拿；拿到就设置，不拿到就设置 null 和 未知；这样，58 本行可以删除掉完事了；这样代码更简单和一致
        Map<Integer, Area> areaMap = convertMap(areaList, Area::getId);
        return convertList(list, item -> {
            Integer parentId = AreaUtils.getParentIdByType(item.getAreaId(), AreaTypeEnum.PROVINCE);
            if (parentId == null) { // 找不到，归到未知
                return item.setAreaId(null).setAreaName("未知");
            }
            findAndThen(areaMap, parentId, area -> item.setAreaId(parentId).setAreaName(area.getName()));
            return item;
        });
    }

    @Override
    public List<CrmStatisticCustomerIndustryRespVO> getCustomerSummaryByIndustry(CrmStatisticsPortraitReqVO reqVO) {
        // 1. 获得用户编号数组
        List<Long> userIds = getUserIds(reqVO);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        reqVO.setUserIds(userIds);

        // 2. 获取客户行业统计数据
        return portraitMapper.selectCustomerIndustryListGroupByIndustryId(reqVO);
    }

    @Override
    public List<CrmStatisticCustomerSourceRespVO> getCustomerSummaryBySource(CrmStatisticsPortraitReqVO reqVO) {
        // 1. 获得用户编号数组
        List<Long> userIds = getUserIds(reqVO);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        reqVO.setUserIds(userIds);

        // 2. 获取客户行业统计数据
        return portraitMapper.selectCustomerSourceListGroupBySource(reqVO);
    }

    @Override
    public List<CrmStatisticCustomerLevelRespVO> getCustomerSummaryByLevel(CrmStatisticsPortraitReqVO reqVO) {
        // 1. 获得用户编号数组
        List<Long> userIds = getUserIds(reqVO);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        reqVO.setUserIds(userIds);

        // 2. 获取客户级别统计数据
        return portraitMapper.selectCustomerLevelListGroupByLevel(reqVO);
    }

    /**
     * 获取用户编号数组。如果用户编号为空, 则获得部门下的用户编号数组，包括子部门的所有用户编号
     *
     * @param reqVO 请求参数
     * @return 用户编号数组
     */
    private List<Long> getUserIds(CrmStatisticsPortraitReqVO reqVO) {
        // 情况一：选中某个用户
        if (ObjUtil.isNotNull(reqVO.getUserId())) {
            return ListUtil.of(reqVO.getUserId());
        }
        // 情况二：选中某个部门
        // 2.1 获得部门列表
        List<Long> deptIds = convertList(deptApi.getChildDeptList(reqVO.getDeptId()), DeptRespDTO::getId);
        deptIds.add(reqVO.getDeptId());
        // 2.2 获得用户编号
        return convertList(adminUserApi.getUserListByDeptIds(deptIds), AdminUserRespDTO::getId);
    }

}
