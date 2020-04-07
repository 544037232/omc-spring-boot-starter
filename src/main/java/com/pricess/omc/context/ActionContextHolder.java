package com.pricess.omc.context;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;

/**
 * 请求上下文执行器，默认使用threadLocal
 * @author pricess.wang
 * @date 2019/12/31
 */
public class ActionContextHolder {

	public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
	public static final String SYSTEM_PROPERTY = "roletask.action.strategy";
	private static String strategyName = System.getProperty(SYSTEM_PROPERTY);
	private static ActionContextHolderStrategy<ActionContext> strategy;
	private static int initializeCount = 0;

	static {
		initialize();
	}

	public static void clearContext() {
		strategy.clearContext();
	}

	public static ActionContext getContext() {
		return strategy.getContext();
	}

	public static int getInitializeCount() {
		return initializeCount;
	}

	private static void initialize() {
		if (!StringUtils.hasText(strategyName)) {
			// Set default
			strategyName = MODE_THREADLOCAL;
		}

		if (strategyName.equals(MODE_THREADLOCAL)) {
			strategy = new ThreadLocalActionContextHolderStrategy();
		}
		else {
			// Try to load a custom strategy
			try {
				Class<?> clazz = Class.forName(strategyName);
				Constructor<?> customStrategy = clazz.getConstructor();
				strategy = (ActionContextHolderStrategy<ActionContext>) customStrategy.newInstance();
			}
			catch (Exception ex) {
				ReflectionUtils.handleReflectionException(ex);
			}
		}

		initializeCount++;
	}

	public static void setContext(ActionContext context) {
		strategy.setContext(context);
	}

	public static void setStrategyName(String strategyName) {
		ActionContextHolder.strategyName = strategyName;
		initialize();
	}

	public static ActionContextHolderStrategy<ActionContext> getContextHolderStrategy() {
		return strategy;
	}

	public static ActionContext createEmptyContext() {
		return strategy.createEmptyContext();
	}

	@Override
	public String toString() {
		return "AppContextHolder[strategy='" + strategyName + "'; initializeCount="
				+ initializeCount + "]";
	}
}
