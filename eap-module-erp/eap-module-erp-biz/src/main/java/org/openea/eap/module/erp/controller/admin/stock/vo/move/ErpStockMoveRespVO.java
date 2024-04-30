package org.openea.eap.module.erp.controller.admin.stock.vo.move;

import org.openea.eap.framework.excel.core.annotations.DictFormat;
import org.openea.eap.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.openea.eap.module.erp.enums.DictTypeConstants.AUDIT_STATUS;

@Schema(description = "管理后台 - ERP 库存调拨单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpStockMoveRespVO {

    @Schema(description = "调拨编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11756")
    @ExcelProperty("调拨编号")
    private Long id;

    @Schema(description = "调拨单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "S123")
    @ExcelProperty("调拨单号")
    private String no;

    @Schema(description = "调拨时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("调拨时间")
    private LocalDateTime moveTime;

    @Schema(description = "合计数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "15663")
    @ExcelProperty("合计数量")
    private BigDecimal totalCount;

    @Schema(description = "合计金额，单位：元", requiredMode = Schema.RequiredMode.REQUIRED, example = "24906")
    @ExcelProperty("合计金额")
    private BigDecimal totalPrice;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat(AUDIT_STATUS)
    private Integer status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "附件 URL", example = "https://www.iocoder.cn/1.doc")
    private String fileUrl;

    @Schema(description = "创建人", example = "芋道")
    private String creator;
    @Schema(description = "创建人名称", example = "芋道")
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "调拨项列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Item> items;

    @Schema(description = "产品信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("产品信息")
    private String productNames;

    @Data
    public static class Item {

        @Schema(description = "调拨项编号", example = "11756")
        private Long id;

        @Schema(description = "调出仓库编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3113")
        private Long fromWarehouseId;

        @Schema(description = "调入仓库编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "888")
        private Long toWarehouseId;

        @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3113")
        private Long productId;

        @Schema(description = "产品单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "100.00")
        private BigDecimal productPrice;

        @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100.00")
        private BigDecimal count;

        @Schema(description = "备注", example = "随便")
        private String remark;

        // ========== 关联字段 ==========

        @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "巧克力")
        private String productName;
        @Schema(description = "产品条码", requiredMode = Schema.RequiredMode.REQUIRED, example = "A9985")
        private String productBarCode;
        @Schema(description = "产品单位名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "盒")
        private String productUnitName;

        @Schema(description = "库存数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100.00")
        private BigDecimal stockCount; // 该字段仅仅在“详情”和“编辑”时使用

    }

}