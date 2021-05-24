package com.hong.treedict.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 树形字典
 * </p>
 *
 * @author Hongxy
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tree_dict")
public class TreeDictPO {

    private static final long serialVersionUID = 1L;

    // 主键
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 上级 id
     */
    private String parentId;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 数据代码
     */
    private String code;

    /**
     * 数据描述
     */
    private String description;

    /**
     * 左值
     */
    private Long leftValue;

    /**
     * 右值
     */
    private Long rightValue;

    /**
     * 深度
     */
    private Long depth;

    /**
     * 路径
     */
    private String path;

    // 授权访问用户
    private String authUserId;

    // 授权访问角色
    private String authRoleId;

    // 授权访问部门
    private String authDeptId;

    // 授权访问机构
    private String authOrgId;

    // 上次修改用户
    @TableField(value = "last_modified_by", fill = FieldFill.INSERT_UPDATE)
    private String lastModifiedBy;

    // 上次修改时间
    @TableField(value = "last_modified_time", fill = FieldFill.INSERT_UPDATE)
    private String lastModifiedTime;

    // 创建用户
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    // 创建时间
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private String createdTime;

    // 逻辑删除标志位 逻辑删除值：now() 未删除值：0
    @TableLogic(value = "0", delval = "now()")
    @TableField(value = "deleted_flag", fill = FieldFill.INSERT)
    private Long deletedFlag;

    // 版本
    @Version
    @TableField(value = "version", fill = FieldFill.INSERT)
    private Long version;

}
