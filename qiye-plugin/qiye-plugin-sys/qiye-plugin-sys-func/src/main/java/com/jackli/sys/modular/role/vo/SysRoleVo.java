package com.jackli.sys.modular.role.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysRoleVo {
    /** id */
    @ApiModelProperty(value = "id", position = 1)
    private String id;

    /** 租户id */
    @ApiModelProperty(value = "租户id", position = 2)
    private String tenantId;

    /** 组织id */
    @ApiModelProperty(value = "组织id", position = 3)
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String orgId;

    /** 名称 */
    @ApiModelProperty(value = "名称", position = 4)
    private String name;

    /** 编码 */
    @ApiModelProperty(value = "编码", position = 5)
    private String code;

    /** 分类 */
    @ApiModelProperty(value = "分类", position = 6)
    private String category;

    /** 排序码 */
    @ApiModelProperty(value = "排序码", position = 7)
    private Integer sortCode;
    /** 用户状态 */
    @ApiModelProperty(value = "校色状态(ENABLE登录页显示，DISABLED登录页不显示)", position = 46)
    private String roleStatus;
    /** 扩展信息 */
    @ApiModelProperty(value = "扩展信息", position = 8)
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String extJson;

    @ApiModelProperty(value = "角色包含用户", position = 9)
    private Integer userCount;
}
