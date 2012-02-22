/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.mobile.hriv.aspect;

import javax.annotation.Resource;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

/**
 * Aspect handling caching of KivwsSearchService#searchUnits(java.lang.String, int, java.util.List<java.lang.String>).
 */
@Aspect
public class KivwsSearchServiceCacheAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(KivwsSearchServiceCacheAspect.class);

    @Resource(name = "kivwsSearchServiceCache")
    private Ehcache cache;

    public void setCache(Ehcache cache) {
        this.cache = cache;
    }

    /**
     * Pointcut around KivwsSearchService#searchUnits(java.lang.String, int, java.util.List<java.lang.String>) method.
     * <p/>
     * Using Ehcache to cache entries, cache settings (time to live, max elements etc.) are defiend in ehcache.xml.
     *
     * @param joinPoint Used to get method parameters value(s)
     * @return method return value
     * @throws Throwable If something goes wrong
     */
    @Around("execution(* se.vgregion.mobile.hriv.service.KivwsSearchService.searchUnits(java.lang.String,int,java.util.List))")
    public Object cacheKivwsSearches(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] arguments = joinPoint.getArgs();
        // Convert to list since lists implement the equals method so the cache.get() will work
        List<Object> argumentsAsList = Arrays.asList(arguments);
        Element element = cache.get(argumentsAsList);
        if (null == element) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("No cached element found.");
            }
            Object result = joinPoint.proceed();
            if (null != result) {
                element = new Element(argumentsAsList, result);
                cache.put(element);
            }
            return result;
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cached element found.");
            }
            return element.getValue();
        }
    }
}
