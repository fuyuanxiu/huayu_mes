package com.system.permission.dao;

import java.util.List;
import java.util.Map;

import com.system.role.entity.RolePermissionMap;
import com.system.role.entity.SysRole;
import com.system.user.entity.UserRoleMap;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.system.permission.entity.SysPermission;

/**
 * 菜单基础信息表
 */
public interface SysPermissionDao extends CrudRepository<SysPermission, Long>, JpaSpecificationExecutor<SysPermission> {

	
	public List<SysPermission> findByDelFlag(Integer delFlag);

	public List<SysPermission> findByDelFlagAndParentId(Integer delFlag,long pid);

	public SysPermission findByIdAndDelFlag(long id,Integer delFlag);

//	@Query(value = "select "+
//				   "  p.id, p.bs_name,p.parent_id pId, p.zindex, p.istype, p.bs_code, p.icon, p.page_url "+
//				   " from permission p "+
//				   " LEFT JOIN role_permission rp ON rp.permit_id=p.id "+
//				   " LEFT JOIN role r ON r.id=rp.role_id "+
//				   " LEFT JOIN user_role ur ON ur.role_id=r.id "+
//				   " WHERE ur.user_id=?1 and p.is_del=0 "+
//				   " GROUP BY p.id "+
//				   " order by p.zindex ", nativeQuery = true)
//	  public List<Map<String, Object>> getUserPerms(long id);
    @Query(value = "select distinct "+
            "  p.id ID, p.MENU_NAME,p.parent_id PID, p.ZINDEX , p.ISTYPE, p.MENU_CODE, p.MENU_ICON, p.PAGE_URL "+
            " from "+SysPermission.TABLE_NAME+" p "+
           " LEFT JOIN "+RolePermissionMap.TABLE_NAME+" rp ON rp.permit_id=p.id "+
            " LEFT JOIN "+SysRole.TABLE_NAME +" r ON r.id=rp.role_id "+
           " LEFT JOIN "+UserRoleMap.TABLE_NAME+" ur ON ur.role_id=r.id "+
           " WHERE ur.user_id=?1 and p.del_flag=0 and rp.del_flag=0 and ur.del_flag=0 and r.status=0 "+
           " order by p.zindex ", nativeQuery = true)
    public List<Map<String, Object>> getUserPerms(long id);
    
    @Query(value = "select t from SysPermission t left join RolePermissionMap m on m.permitId = t.id and m.roleId =:roleId")
	public List<SysPermission> findPermsByRoleId(@Param("roleId") Long roleId);
    
    @Query(value = "select t from SysPermission t left join RolePermissionMap m on m.permitId = t.id where m.roleId =:roleId and t.istype=1 and t.delFlag = 0 and m.delFlag = 0")
   	public List<SysPermission> findPermsByRoleIdAndBtn(@Param("roleId") Long roleId);
    
    @Query(value = "select t from SysPermission t where  t.delFlag=0 order by t.parentId,t.zindex ")
   	public List<SysPermission> findOrderByZindex();
    
}
