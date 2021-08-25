package com.petfabula.infrastructure.persistence.jpa.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;

@Aspect
@Component
@Slf4j
public class SoftDeleteFilterAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Around("@annotation(FilterSoftDelete)")
    public Object filterSoftDelete(ProceedingJoinPoint joinPoint) throws Throwable {

        Session session = null;

        // Get the Session from the entityManager in current persistence context
        session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("activeFilter");

        try {
            return joinPoint.proceed();
        }finally {
            // If session was available
            if (session != null) {
                // Disable the filter
                session.disableFilter("activeFilter");
            }
        }
    }
}
