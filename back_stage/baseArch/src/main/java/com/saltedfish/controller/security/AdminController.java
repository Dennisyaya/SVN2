package com.saltedfish.controller.security;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saltedfish.cmd.admin.AdminAddCmd;
import com.saltedfish.cmd.admin.AdminListQueryCmd;
import com.saltedfish.constants.Constants;
import com.saltedfish.constants.Url;
import com.saltedfish.constants.View;
import com.saltedfish.controller.base.BaseController;
import com.saltedfish.dto.BaseResultDTO;
import com.saltedfish.dto.security.UserListDTO;
import com.saltedfish.entity.security.SysRoles;
import com.saltedfish.entity.security.SysUsers;
import com.saltedfish.service.security.RoleService;
import com.saltedfish.service.security.UserService;


@Controller
public class AdminController extends BaseController {

	protected final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	/**
	 * 管理员列表页面
	 * @return
	 */
	@RequestMapping(value = Url.ADMIN_LIST_PAGE, method = RequestMethod.GET)
	public String turnToWelcomPage() {
		return View.ADMIN_LIST_VIEW;
	}

	/**
	 * 管理员列表数据加载
	 * @return
	 */
	@RequestMapping(value = Url.ADMIN_LIST_DATA, method = RequestMethod.GET)
	@ResponseBody
	public BaseResultDTO<List<UserListDTO>> adminDataLoad(AdminListQueryCmd cmd, Integer startPage, Integer pageSize) {
		logger.info("=====adminDataLoad====" + cmd.toString());
		BaseResultDTO<List<UserListDTO>> result = new BaseResultDTO<List<UserListDTO>>();
		List<UserListDTO> user = userService.queryUsers(cmd);

		Integer count = userService.queryUsersCount(cmd);
		result.setResult(user);
		result.setCount(count);
		result.setStatus(Constants.R_STATUS_SUCCESS);
		result.setMessage("query success!");
		return result;
	}

	/**
	 * 管理员添加页面
	 * @return
	 */
	@RequestMapping(value = Url.ADMIN_ADD_PAGE, method = RequestMethod.GET)
	public String turnToAdminAddPage(ModelMap map) {
		List<SysRoles> roles = roleService.queryAllRoles();
		map.put("roles", roles);
		return View.ADMIN_ADD_VIEW;
	}

	/**
	 * 管理员添加
	 * @return
	 */
	@RequestMapping(value = Url.ADMIN_ADD_DATA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResultDTO<String> addAdmin(AdminAddCmd cmd) {
		BaseResultDTO<String> result = new BaseResultDTO<String>();
		try {
			if (!(cmd.getOriginalPassword() != null && cmd.getConfirmPassword() != null && (cmd.getOriginalPassword().equals(cmd.getConfirmPassword())))) {
				result.setStatus(Constants.R_STATUS_FAILTURE);
				result.setMessage("原始密码和初始密码不正确!");
				return result;
			}
			
			if (cmd.getUserId()==null) {//添加用户
				userService.addAdminUser(cmd);
			}else{
				userService.updateAdminUser(cmd);
			}
			
		} catch (Exception e) {
			logger.debug("============添加管理员失败 :" + e.getMessage());
			result.setStatus(Constants.R_STATUS_FAILTURE);
		}
		result.setStatus(Constants.R_STATUS_SUCCESS);
		return result;
	}

	/**
	 * 管理员是否启动
	 * @return
	 */
	@RequestMapping(value = Url.ADMIN_ENABLED_UPDATE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResultDTO<String> endOrStartEnable(Integer userId, Boolean enabled) {
		BaseResultDTO<String> result = new BaseResultDTO<String>();
		try {
			userService.endOrStartEnable(userId, enabled);
		} catch (Exception e) {
			logger.debug("============启动或者禁用用户失败 :" + e.getMessage());
			result.setStatus(Constants.R_STATUS_FAILTURE);
		}
		result.setStatus(Constants.R_STATUS_SUCCESS);
		return result;
	}
	
	
	/**
	 * 管理员修改页面
	 * @return
	 */
	@RequestMapping(value = Url.ADMIN_UPDATE_PAGE, method = RequestMethod.GET)
	public String turnToAdminUpdatePage(Integer userId,ModelMap map) {
		List<SysRoles> roles = roleService.queryAllRoles();
		map.put("roles", roles);
		UserListDTO user=userService.queryUsersById(userId);
		map.put("user", user);
		return View.ADMIN_UPDATE_VIEW;
	}
	

	/**
	 * 删除管理员
	 * @return
	 */
	@RequestMapping(value = Url.ADMIN_DELETE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResultDTO<String> deleteUser(Integer userId, Boolean enabled) {
		BaseResultDTO<String> result = new BaseResultDTO<String>();
		try {
			userService.deleteUserByUserId(userId);
		} catch (Exception e) {
			logger.debug("============启动或者禁用用户失败 :" + e.getMessage());
			result.setStatus(Constants.R_STATUS_FAILTURE);
		}
		result.setStatus(Constants.R_STATUS_SUCCESS);
		return result;
	}

}
