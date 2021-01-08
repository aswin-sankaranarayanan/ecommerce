package com.ecommerce.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LoggingAspect.class);
	
	@Before(value = "execution(* com.ecommerce.services..*.*(..))")
	public void logBefore(JoinPoint jp) {
		logger.info("---Inside the method -> {}",jp.getSignature());
		for (Object arg : jp.getArgs()) {
			logger.info("---Method Args -> {}",arg);
		}
	}
	
	@AfterReturning(value = "execution(* com.ecommerce.services..*.*(..))",returning = "result")
	public void logAferReturning(JoinPoint jp, Object result) {
		logger.info("---Return value -> {}", result);
	}
	
	@After(value = "execution(* com.ecommerce.services..*.*(..))")
	public void logAfer(JoinPoint jp) {
		logger.info("---Exiting the  method -> {}",jp.getSignature());
	}
	
}
