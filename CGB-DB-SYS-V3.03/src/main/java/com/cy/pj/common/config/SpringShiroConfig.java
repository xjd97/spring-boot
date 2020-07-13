package com.cy.pj.common.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration 注解描述的类为一个配置对象
 * 此对象也会交给spring管理
 *
 */
@Configuration
public class SpringShiroConfig {
	/**
	 * shiro框架的核心安全管理对象
	 */
	@Bean
	public SecurityManager securityManager(Realm realm,
			CacheManager cacheManager,
			RememberMeManager rememberMeManager,
			SessionManager sessionManager) {
		DefaultWebSecurityManager sManager = new DefaultWebSecurityManager();
		sManager.setRealm(realm);
		//将shiro中的cache管理器注入给SecurityManager
		sManager.setCacheManager(cacheManager);
		//设置记住我
		sManager.setRememberMeManager(rememberMeManager);
		//设置会话管理器对象
		sManager.setSessionManager(sessionManager);
		return sManager;
	}
	/**
	 * 配置过滤器工厂
	 * @param securityManager
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager) {
		ShiroFilterFactoryBean sfBean = new ShiroFilterFactoryBean();
		sfBean.setSecurityManager(securityManager);
		sfBean.setLoginUrl("/doLoginUI");
		//定义map指定请求过滤规则(哪些资源允许匿名访问,哪些必须认证访问)
		Map<String, String> map = new LinkedHashMap<>();
		//静态资源允许匿名访问:"anon"
		map.put("/bower_components/**","anon");
		map.put("/build/**","anon");
		map.put("/dist/**","anon");
		map.put("/plugins/**","anon");
		map.put("/videos/**","anon");
		map.put("/user/doLogin", "anon");
		map.put("/test", "anon");
		map.put("/doLogout","logout");//logout为退出时需要指定的规则
		//除了匿名访问的资源,其它都要认证("authc")后访问
		map.put("/**","user");//使用记住我功能时需要将authc修改为user
		sfBean.setFilterChainDefinitionMap(map);
		return sfBean;
	}
	
//	@Bean
//	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//			 return new LifecycleBeanPostProcessor();
//	}
	
//	@DependsOn("lifecycleBeanPostProcessor")
//	@Bean
//	public DefaultAdvisorAutoProxyCreator newDefaultAdvisorAutoProxyCreator() {
//			 return new DefaultAdvisorAutoProxyCreator();
//	}
	/**
	 * 配置授权的Advisor,通过此Advisor告诉框架底层,要为指定的对象创建代理对象,
	 * 然后对指定的业务方法进行授权检查
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor 
	newAuthorizationAttributeSourceAdvisor(
			SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor=
				new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}
	/**
	 * 配置shiro中的缓存管理器对象
	 * @return
	 */
	@Bean
	public CacheManager shiroCacheManager(){
		 return new MemoryConstrainedCacheManager();
	}
	/**
	 * 构建记住我管理器对象
	 * @return
	 */
	@Bean
	public RememberMeManager rememberMeManager() {
		CookieRememberMeManager cManager=
				new CookieRememberMeManager();
		SimpleCookie cookie=new SimpleCookie("rememberMe");
		cookie.setMaxAge(10*60);
		cManager.setCookie(cookie);
		return cManager;
	}
	/**
	 * shiro框架的会话管理对象
	 * @return
	 */
	@Bean
	public SessionManager sessionManager() {
		DefaultWebSessionManager sManager = new DefaultWebSessionManager();
		sManager.setGlobalSessionTimeout(60*60*1000);
		return sManager;
	}
	
}
