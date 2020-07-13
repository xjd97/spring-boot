package com.cy.pj.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.cy.pj.common.annotation.RequestLog;
import com.cy.pj.common.util.IPUtils;
import com.cy.pj.common.util.ShiroUtil;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;

/**
 * @Aspect 注解描述的类型为一个切面类型
 * @Component 注解描述的类为spring中的一个bean对象类型(一般组件)
 * 说明:对于一个切面对象而言,可以理解为封装特定功能的一个扩展业务对象,此对象中通常会定义:
 * 1)切入点(Pointcut):织入扩展功能的一个点(通常为目标方法集合)
 * 2)通知(Advice):是用于进行功能拓展的业务方法
 */
@Aspect
@Component
public class SysLogAspect {
	//private Logger log = LoggerFactory.getLogger(SysLogAspect.class);
	//bean(bean的名字)为切入点表达式
	//@Pointcut("bean(sysUserServiceImpl)")
	@Pointcut("@annotation(com.cy.pj.common.annotation.RequestLog)")
	public void doPointCut() {}

	//@Around("bean(sysUserServiceImpl)")
	@Around("doPointCut()")
	public Object around(ProceedingJoinPoint jp) throws Throwable{
		long t1=System.currentTimeMillis();
		Object result=jp.proceed();//调用下一个切面或目标方法
		long t2=System.currentTimeMillis();
		//将用户行为信息存储到数据库中.
		saveLog(jp, (t2-t1));
		return result;
	}

	@Resource
	private SysLogService sysLogService;

	//将用户行为信息存储到数据库中.
	private void saveLog(ProceedingJoinPoint jp,long time)throws Exception {
		//1.获取用户行为日志(谁(ip+用户名)在什么时间访问了什么方法,传递什么参数,执行时长是多少等)
		//2.1获取目标方法
		//2.1.1获取方法签名(记录的是目标方法信息)
		MethodSignature ms=(MethodSignature)jp.getSignature();
		//2.1.2获取目标类对象的字节码对象
		Class<?> targetClass=jp.getTarget().getClass();
		//2.1.3获取目标方法对象
		Method targetMethod=targetClass.getDeclaredMethod(ms.getName(),ms.getParameterTypes());
		//2.1.4获取目标方法对象上的注解
		RequestLog rLog=targetMethod.getAnnotation(RequestLog.class);
		//2.1.5获取操作名
		String operation=rLog.operation();
		//2.1.6获取方法全名
		String dType=targetClass.getName();
		String methodName=ms.getName();
		String targetClassMethod=dType+"."+methodName;
		//2.2获取方法参数
		//值为json格式的字符串
		String params = new ObjectMapper().writeValueAsString(jp.getArgs());
		//3.对信息进行封装(SysLog)
		SysLog log=new SysLog();
		log.setIp(IPUtils.getIpAddr());
		log.setUsername(ShiroUtil.getUsername());
		log.setOperation(operation);
		log.setMethod(targetClassMethod);//targetClass.targetMethod
		log.setParams(params);
		log.setTime(time);
		log.setCreatedTime(new Date());
		//4.将信息写入到数据库.
//		new Thread() {
//			public void run() {
//				sysLogService.saveObject(log);
//			}
//		}.start();
		sysLogService.saveObject(log);
	}
}








