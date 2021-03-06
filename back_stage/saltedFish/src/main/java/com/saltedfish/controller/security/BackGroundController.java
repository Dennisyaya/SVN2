package com.saltedfish.controller.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.saltedfish.constants.Constants;
import com.saltedfish.constants.Url;
import com.saltedfish.constants.View;
import com.saltedfish.service.security.UserService;
import com.saltedfish.utils.VerifyCodeUtils;


/**
 * 该跳转层功能:
 * 1.用户登陆，退出登陆，403页面，获取验证码页面
 * @author lkd
 *
 */
@Controller
public class BackGroundController {

	protected final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	/*
	 * @Autowired private AuthenticationManager authenticationManager;
	 */
	/*
	 * @Autowired private SessionRegistry sessionRegistry;
	 */

	/**
	 * 跳转首页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = Url.INDEX, method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap map) {

		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession().getAttribute(Constants.SPRING_SECURITY_CONTEXT);

		// 访问首页的话，则看看是否记住我，如果是记住我的话，则可以直接访问...
		// // 登录名
		// System.out.println("登陆名-->:" + securityContextImpl.getAuthentication().getName());
		// // 登录密码，未加密的
		// System.out.println("登录密码，未加密的-->" +
		// securityContextImpl.getAuthentication().getCredentials());

		//
		// WebAuthenticationDetails details = (WebAuthenticationDetails)
		// securityContextImpl.getAuthentication().getDetails();
		// // 获得访问地址
		// System.out.println("RemoteAddress" + details.getRemoteAddress());
		// // 获得sessionid
		// System.out.println("SessionId-->" + details.getSessionId());
		// // 获得当前用户所拥有的权限
		// List<GrantedAuthority> authorities = (List<GrantedAuthority>)
		// securityContextImpl.getAuthentication().getAuthorities();
		//
		// String ip = request.getLocalAddr();
		//
		// for (GrantedAuthority grantedAuthority : authorities) {
		// System.out.println("Authority-->" + grantedAuthority.getAuthority());
		// }
		//
		// map.addAttribute("ip", ip);
		//
		// logger.info("=======================log日志登陆首页=============================");

		return View.INDEX;
	}

	/**
	 * 跳转登陆页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = Url.LOGIN, method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout,
			HttpServletRequest request, ModelMap map) {
		// 重新登录时销毁该用户的Session

		Object o = request.getSession().getAttribute(Constants.SPRING_SECURITY_CONTEXT);
		if (null != o) {
			request.getSession().removeAttribute(Constants.SPRING_SECURITY_CONTEXT);
		}

		if (logout != null) {
			map.addAttribute("msg", "You've been logged out successfully.");
		}

		return View.LOGIN;
	}

	/**
	 * 验证用户账号密码
	 * @param request
	 * @return
	 */
	/*
	 * @RequestMapping(value = Url.LOGIN_CHECK, method = RequestMethod.POST) public String
	 * loginCheck(HttpServletRequest request, String username, String password) { try { // 账号密码验证码
	 * 校验 checkUserNameAndPasswordAndCode(request, username, password); // 验证用户账号与密码是否正确 SysUsers
	 * user = userService.selectSysUserByName(username); if (user == null ||
	 * !user.getPassword().trim().equals(PasswordEncodeUtils.encode(password, username,
	 * Constants.PASSWORD_KEY))) { throw new AuthenticationServiceException("用户名或者密码不正确"); } //
	 * 判断用户是否已经登录 // System.err.println(sessionRegistry.getAllPrincipals() + "=================");
	 * // 认证.... Authentication authentication = myAuthenticationManager.authenticate(new
	 * UsernamePasswordAuthenticationToken(username, PasswordEncodeUtils.encode( password, username,
	 * Constants.PASSWORD_KEY.trim()))); SecurityContext securityContext =
	 * SecurityContextHolder.getContext(); securityContext.setAuthentication(authentication);
	 * HttpSession session = request.getSession(true);
	 * session.setAttribute(Constants.SPRING_SECURITY_CONTEXT, securityContext); //
	 * 当验证都通过后，把用户信息放在session里 request.getSession().setAttribute("userSession", user); // 每次用户登录
	 * 都会创建一个session, } catch (Exception e) { request.setAttribute("message", e.getMessage());
	 * return View.LOGIN; } return View.INDEX; }
	 */

	/**
	 * 校验用户名，密码，验证码
	 * @param request
	 * @param username
	 * @param password
	 */
	/*
	 * private void checkUserNameAndPasswordAndCode(HttpServletRequest request, String username,
	 * String password) { String requestCaptcha = request.getParameter("code"); String
	 * sessionCaptcha = (String) request.getSession().getAttribute("randomStr"); if
	 * (!request.getMethod().equals("POST")) { // 如果不是post请求，默认跳转到登录页面 throw new
	 * AuthenticationServiceException("请重新登录"); } if (CommonUtils.isEmpty(username) ||
	 * CommonUtils.isEmpty(password)) { throw new AuthenticationServiceException("用户名或者密码不能为空"); }
	 * if (StringUtils.isEmpty(requestCaptcha) || StringUtils.isEmpty(sessionCaptcha)) { throw new
	 * AuthenticationServiceException("验证码不能为空"); } if
	 * (!sessionCaptcha.equalsIgnoreCase(requestCaptcha)) { throw new
	 * AuthenticationServiceException("验证码不正确"); } }
	 */

	/**
	 * 
	 * <p>退出页面</p>
	 * @param request
	 * @return
	 * @author lk'd
	 */
	@RequestMapping(value = Url.LOING_OUT, method = RequestMethod.GET)
	public String loginOut(HttpServletRequest request) {
		return View.LOGIN;
	};

	/**
	 * 
	 * <p>session超时跳转也main</p>
	 * @return
	 * @author lkd
	 */
	@RequestMapping(value = Url.TIME_OUT_URL, method = RequestMethod.GET)
	public String timeOut() {
		System.out.println("=========超时跳转页面==============");
		return View.TIME_OUT_VIEW;
	};

	/**
	 * 
	 * <p>403页面</p>
	 * @param map
	 * @param request
	 * @return
	 * @author lkd
	 */
	@RequestMapping(value = Url._403)
	public String accessDenied(ModelMap map, HttpServletRequest request) {

		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession().getAttribute(Constants.SPRING_SECURITY_CONTEXT);
		if (securityContextImpl != null) {
			String userName = securityContextImpl.getAuthentication().getName();
			map.addAttribute("userName", userName);
		}

		return View._403;
	}

	/**
	 * 
	 * <p>获取验证码</p>
	 * @param response
	 * @param request
	 * @author lkd
	 */
	@RequestMapping(value = Url.RANDORM_CODE)
	public void getRandomCode(HttpServletResponse response, HttpServletRequest request) {
		Integer width = 130;// 初始化图片宽度
		Integer height = 40; // 初始化图片高度
		Integer randomStrNum = 4; // 初始化图片随机数个数
		try {
			request.setCharacterEncoding("UTF-8");
			// 获取HttpSession对象
			HttpSession session = request.getSession();
			// 获取随机字符串
			String randomStr = VerifyCodeUtils.generateVerifyCode(randomStrNum);
			if (null != session) {
				// 设置参数
				session.setAttribute("randomStr", randomStr);
				// 设置响应类型,输出图片客户端不缓存
				response.setDateHeader("Expires", 1L);
				response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
				response.addHeader("Pragma", "no-cache");
				response.setContentType("image/jpeg");
				// 输出到页面
				VerifyCodeUtils.outputImage(width, height, response.getOutputStream(), randomStr);
			}
		} catch (Exception e) {
			logger.info("获取验证码异常,原因:" + e.getMessage());
		}

	}

	@RequestMapping(value = Url.TURN_WELCOME, method = RequestMethod.GET)
	public String turnToWelcomPage() {
		return View.WELCOME_VIEW;
	}

}
