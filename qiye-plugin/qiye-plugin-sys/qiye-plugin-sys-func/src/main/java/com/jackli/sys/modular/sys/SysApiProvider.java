package com.jackli.sys.modular.sys;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jackli.auth.core.enums.SysUserStatusEnum;
import com.jackli.common.enums.SysBuildInEnum;
import com.jackli.common.util.CommonAvatarUtil;
import com.jackli.common.util.CommonCryptogramUtil;
import com.jackli.sys.api.SysApi;
import com.jackli.sys.modular.org.entity.SysOrg;
import com.jackli.sys.modular.org.enums.SysOrgCategoryEnum;
import com.jackli.sys.modular.org.service.SysOrgService;
import com.jackli.sys.modular.position.entity.SysPosition;
import com.jackli.sys.modular.position.enums.SysPositionCategoryEnum;
import com.jackli.sys.modular.position.service.SysPositionService;
import com.jackli.sys.modular.relation.entity.SysRelation;
import com.jackli.sys.modular.relation.enums.SysRelationCategoryEnum;
import com.jackli.sys.modular.relation.service.SysRelationService;
import com.jackli.sys.modular.resource.entity.SysButton;
import com.jackli.sys.modular.resource.entity.SysMenu;
import com.jackli.sys.modular.resource.entity.SysModule;
import com.jackli.sys.modular.resource.entity.SysSpa;
import com.jackli.sys.modular.resource.enums.SysResourceCategoryEnum;
import com.jackli.sys.modular.resource.enums.SysResourceMenuTypeEnum;
import com.jackli.sys.modular.resource.service.SysButtonService;
import com.jackli.sys.modular.resource.service.SysMenuService;
import com.jackli.sys.modular.resource.service.SysModuleService;
import com.jackli.sys.modular.resource.service.SysSpaService;
import com.jackli.sys.modular.role.entity.SysRole;
import com.jackli.sys.modular.role.enums.SysRoleCategoryEnum;
import com.jackli.sys.modular.role.enums.SysRoleDataScopeCategoryEnum;
import com.jackli.sys.modular.role.service.SysRoleService;
import com.jackli.sys.modular.user.entity.SysUser;
import com.jackli.sys.modular.user.service.SysUserService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统模块综合API接口实现类
 *
 * @author lijinchang
 * @date 2022/9/26 14:30
 **/
@Service
public class SysApiProvider implements SysApi {

    @Resource
    private SysRelationService sysRelationService;

    @Resource
    private SysOrgService sysOrgService;

    @Resource
    private SysPositionService sysPositionService;

    @Resource
    private SysModuleService sysModuleService;

    @Resource
    private SysSpaService sysSpaService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysButtonService sysButtonService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserService sysUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initTenDataForCategoryId(String tenId, String tenName) {
        // ==============给租户初始化组织=================
        SysOrg sysOrg = new SysOrg();
        sysOrg.setTenantId(tenId);
        sysOrg.setParentId("0");
        sysOrg.setName(tenName);
        sysOrg.setCode(RandomUtil.randomString(10));
        sysOrg.setCategory(SysOrgCategoryEnum.COMPANY.getValue());
        sysOrg.setSortCode(1);
        sysOrgService.save(sysOrg);

        // ==============给租户初始化职位=================
        SysPosition sysPosition = new SysPosition();
        sysPosition.setTenantId(tenId);
        sysPosition.setOrgId(sysOrg.getId());
        sysPosition.setName(SysBuildInEnum.BUILD_IN_ROLE_CODE.getName());
        sysPosition.setCode(RandomUtil.randomString(10));
        sysPosition.setCategory(SysPositionCategoryEnum.HIGH.getValue());
        sysPosition.setSortCode(1);
        sysPositionService.save(sysPosition);

        // ==============给租户初始化模块=================
        Map<String, String> moduleIdRelation = MapUtil.newHashMap();
        List<SysModule> sysModuleList = sysModuleService.list(new LambdaQueryWrapper<SysModule>().eq(SysModule::getCategory,
                SysResourceCategoryEnum.MODULE.getValue()).eq(SysModule::getCode,  SysBuildInEnum.BUILD_IN_MODULE_CODE.getValue()))
                .stream().peek(sysModule -> {
                    String oldId = sysModule.getId();
                    String newId = IdWorker.getIdStr();
                    sysModule.setId(newId);
                    sysModule.setTenantId(tenId);
                    moduleIdRelation.put(oldId, newId);
                }).collect(Collectors.toList());
        sysModuleService.saveBatch(sysModuleList);

        // ==============给租户初始化单页=================
        List<SysSpa> sysSpaList = sysSpaService.list(new LambdaQueryWrapper<SysSpa>().eq(SysSpa::getCategory,
                SysResourceCategoryEnum.SPA.getValue()).eq(SysSpa::getCode,  SysBuildInEnum.BUILD_IN_MODULE_CODE.getValue()))
                .stream().peek(sysSpa -> {
                    sysSpa.setId(null);
                    sysSpa.setTenantId(tenId);
                }).collect(Collectors.toList());
        sysSpaService.saveBatch(sysSpaList);

        // ==============给租户初始化模块菜单与按钮=================
        List<SysMenu> sysMenuAndButtonList = sysMenuService.list(new LambdaQueryWrapper<SysMenu>().in(SysMenu::getCategory,
                SysResourceCategoryEnum.MENU.getValue(), SysResourceCategoryEnum.BUTTON.getValue())
                .ne(SysMenu::getCode, SysBuildInEnum.BUILD_IN_NO_TEN_MENU_CODE.getValue())).stream().peek(sysMenu -> {
                    if(sysMenu.getCategory().equals(SysResourceCategoryEnum.MENU.getValue())) {
                        sysMenu.setModule(moduleIdRelation.get(sysMenu.getModule()));
                    }
        }).collect(Collectors.toList());

        List<TreeNode<String>> treeNodeList = sysMenuAndButtonList.stream().map(sysMenu ->
                new TreeNode<>(sysMenu.getId(), sysMenu.getParentId(), sysMenu.getTitle(), sysMenu.getSortCode())
                        .setExtra(JSONUtil.parseObj(sysMenu).set("originName", sysMenu.getName())))
                .collect(Collectors.toList());
        List<Tree<String>> treeList = TreeUtil.build(treeNodeList, "0");
        // 批量保存菜单与按钮
        sysMenuAndButtonList = CollectionUtil.newArrayList();
        execRecursionInsertMenuOrButton(sysMenuAndButtonList, "0", treeList, tenId);

        // ==============给租户初始化角色=================
        SysRole sysRole = new SysRole();
        sysRole.setTenantId(tenId);
        sysRole.setName(SysBuildInEnum.BUILD_IN_ROLE_CODE.getName());
        sysRole.setCode(SysBuildInEnum.BUILD_IN_ROLE_CODE.getValue());
        sysRole.setCategory(SysRoleCategoryEnum.GLOBAL.getValue());
        sysRole.setSortCode(1);
        sysRoleService.save(sysRole);

        // ==============给租户初始化用户=================
        SysUser sysUser = new SysUser();
        sysUser.setTenantId(tenId);
        sysUser.setName(SysBuildInEnum.BUILD_IN_USER_ACCOUNT.getName());
        sysUser.setAvatar(CommonAvatarUtil.generateImg(SysBuildInEnum.BUILD_IN_USER_ACCOUNT.getName()));
        sysUser.setAccount(SysBuildInEnum.BUILD_IN_USER_ACCOUNT.getValue());
        sysUser.setPassword(CommonCryptogramUtil.doHashValue("123456"));
        sysUser.setOrgId(sysOrg.getId());
        sysUser.setPositionId(sysPosition.getId());
        sysUser.setSortCode(1);
        sysUser.setUserStatus(SysUserStatusEnum.ENABLE.getValue());
        sysUserService.save(sysUser);

        // ==============给角色授权资源=================
        Map<String, List<SysMenu>> menuButtonMap = sysMenuAndButtonList.stream().filter(sysMenu -> sysMenu.getCategory()
                .equals(SysResourceCategoryEnum.BUTTON.getValue())).collect(Collectors.groupingBy(SysMenu::getParentId));
        List<SysMenu> sysMenuList = sysMenuAndButtonList.stream().filter(sysMenu -> sysMenu.getCategory()
                .equals(SysResourceCategoryEnum.MENU.getValue()) && !sysMenu.getMenuType()
                .equals(SysResourceMenuTypeEnum.CATALOG.getValue())).collect(Collectors.toList());
        List<SysRelation> sysRelationRoleHasResourceList = sysMenuList.stream().map(sysMenu -> {
            SysRelation sysRelationRoleHasResource = new SysRelation();
            sysRelationRoleHasResource.setTenantId(tenId);
            sysRelationRoleHasResource.setObjectId(sysRole.getId());
            sysRelationRoleHasResource.setTargetId(sysMenu.getId());
            sysRelationRoleHasResource.setCategory(SysRelationCategoryEnum.SYS_ROLE_HAS_RESOURCE.getValue());
            List<String> buttonInfo = CollectionUtil.newArrayList();
            if(ObjectUtil.isNotEmpty(menuButtonMap.get(sysMenu.getId()))) {
                buttonInfo = menuButtonMap.get(sysMenu.getId()).stream().map(SysMenu::getId).collect(Collectors.toList());
            }
            JSONObject extJsonObject = JSONUtil.createObj().set("menuId", sysMenu.getId()).set("buttonInfo", buttonInfo);
            sysRelationRoleHasResource.setExtJson(JSONUtil.toJsonStr(extJsonObject));
            return sysRelationRoleHasResource;
        }).collect(Collectors.toList());
        sysRelationService.saveBatch(sysRelationRoleHasResourceList);

        // ==============给角色授权权限=================
        List<SysRelation> sysRelationRoleHasPermissionList = sysRoleService.permissionTreeSelector().stream().map(apiUrl -> {
            apiUrl = StrUtil.split(apiUrl, StrUtil.BRACKET_START).get(0);
            SysRelation sysRelationRoleHasPermission = new SysRelation();
            sysRelationRoleHasPermission.setTenantId(tenId);
            sysRelationRoleHasPermission.setObjectId(sysRole.getId());
            sysRelationRoleHasPermission.setTargetId(apiUrl);
            sysRelationRoleHasPermission.setCategory(SysRelationCategoryEnum.SYS_ROLE_HAS_PERMISSION.getValue());
            JSONObject extJsonObject = JSONUtil.createObj().set("scopeCategory", SysRoleDataScopeCategoryEnum.SCOPE_ALL.getValue())
                    .set("scopeDefineOrgIdList", CollectionUtil.newArrayList()).set("apiUrl", apiUrl);
            sysRelationRoleHasPermission.setExtJson(JSONUtil.toJsonStr(extJsonObject));
            return sysRelationRoleHasPermission;
        }).collect(Collectors.toList());
        sysRelationService.saveBatch(sysRelationRoleHasPermissionList);

        // ==============给用户授权角色=================
        SysRelation sysRelationUserHasRole = new SysRelation();
        sysRelationUserHasRole.setTenantId(tenId);
        sysRelationUserHasRole.setObjectId(sysUser.getId());
        sysRelationUserHasRole.setTargetId(sysRole.getId());
        sysRelationUserHasRole.setCategory(SysRelationCategoryEnum.SYS_USER_HAS_ROLE.getValue());
        sysRelationService.save(sysRelationUserHasRole);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeTenDataForCategoryId(String tenId) {
        // ==============移除租户初始化的组织=================
        sysOrgService.remove(new LambdaQueryWrapper<SysOrg>().eq(SysOrg::getTenantId, tenId));

        // ==============移除租户初始化的职位=================
        sysPositionService.remove(new LambdaQueryWrapper<SysPosition>().eq(SysPosition::getTenantId, tenId));

        // ==============移除租户初始化的模块=================
        sysModuleService.remove(new LambdaQueryWrapper<SysModule>().eq(SysModule::getTenantId, tenId));

        // ==============移除租户初始化的单页=================
        sysSpaService.remove(new LambdaQueryWrapper<SysSpa>().eq(SysSpa::getTenantId, tenId));

        // ==============移除租户初始化的菜单=================
        sysMenuService.remove(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getTenantId, tenId));

        // ==============移除租户初始化的按钮=================
        sysButtonService.remove(new LambdaQueryWrapper<SysButton>().eq(SysButton::getTenantId, tenId));

        // ==============移除租户初始化的角色=================
        sysRoleService.remove(new LambdaQueryWrapper<SysRole>().eq(SysRole::getTenantId, tenId));

        // ==============移除租户初始化的用户=================
        sysUserService.remove(new LambdaQueryWrapper<SysUser>().eq(SysUser::getTenantId, tenId));
    }

    /**
     * 递归插入菜单或者按钮
     *
     * @author lijinchang
     * @date 2022/9/26 15:33
     **/
    private void execRecursionInsertMenuOrButton(List<SysMenu> resultList, String parentId, List<Tree<String>> treeList, String tenId) {
        treeList.forEach(tree -> {
            SysMenu sysMenu = JSONUtil.toBean(JSONUtil.parseObj(tree), SysMenu.class);
            sysMenu.setId(null);
            sysMenu.setName(ObjectUtil.isEmpty(tree.get("originName"))?null:Convert.toStr(tree.get("originName")));
            sysMenu.setParentId(parentId);
            sysMenu.setTenantId(tenId);
            sysMenuService.save(sysMenu);
            resultList.add(sysMenu);
            String menuId = sysMenu.getId();
            List<Tree<String>> children = tree.getChildren();
            if(ObjectUtil.isNotEmpty(children)) {
                execRecursionInsertMenuOrButton(resultList, menuId, children, tenId);
            }
        });
    }
}
