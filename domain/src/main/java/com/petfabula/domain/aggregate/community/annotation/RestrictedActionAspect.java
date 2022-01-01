package com.petfabula.domain.aggregate.community.annotation;

import com.petfabula.domain.aggregate.community.guardian.service.RestrictionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class RestrictedActionAspect {

    @Autowired
    private RestrictionService restrictionService;

    @Before("@annotation(RestrictedAction)")
    public void restrict(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();

        Method method = signature.getMethod();
        RestrictedAction restrictedAction = method.getAnnotation(RestrictedAction.class);
        String name = restrictedAction.idName();

        int idx = Arrays.asList(signature.getParameterNames()).indexOf(name);
        if (idx < 0) {
            throw new RuntimeException("can not find param: " + name);
        }

        Long participatorId = (Long)args[idx];
        restrictionService.restrict(participatorId);
    }
}
