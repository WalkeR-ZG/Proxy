package cn.xa.walkerzg.proxy;

import java.lang.reflect.Method;

public class LogInvocationHandler implements InvocationHandler {
	private Object target;

	public LogInvocationHandler(Object t) {
		this.target = t;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("Enter " + method.getName());
		Object rst = method.invoke(target, args);
		System.out.println("Exit " + method.getName());
		return rst;
	}
}
