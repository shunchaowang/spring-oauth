package edu.osumc.bmi.oauth2.core.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimedAspect {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Around("@annotation(edu.osumc.bmi.oauth2.core.aspect.Timed) && execution(public * *(..))")
  public Object time(final ProceedingJoinPoint joinPoint) throws Throwable {

    long start = System.currentTimeMillis();

    try {
      return joinPoint.proceed();
    } catch (Throwable throwable) {
      throw throwable;
    } finally {
      long end = System.currentTimeMillis();

      logger.info(
          "{}.{} took {} ms",
          joinPoint.getSignature().getDeclaringType().getSimpleName(),
          joinPoint.getSignature().getName(),
          end - start);
    }
  }
}
