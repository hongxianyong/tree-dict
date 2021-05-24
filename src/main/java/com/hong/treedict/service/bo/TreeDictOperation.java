package com.hong.treedict.service.bo;

import lombok.Data;

/**
 * @author Hongxy
 * @description 树节点操作对象
 * @since 2021/5/24
 */
@Data
public class TreeDictOperation {
    // 主键
    private String id;
    // 上级主键
    private String parentId;
    // 左边旁系主键
    private String leftCollateralId;
    // 右边旁系主键
    private String rightCollateralId;
    // 数据代码
    private String code;
    // 数据含义
    private String description;
    // 命名空间
    private String nameSpace;

}
