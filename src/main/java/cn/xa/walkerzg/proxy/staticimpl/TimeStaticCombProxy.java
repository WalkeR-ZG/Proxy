package cn.xa.walkerzg.proxy.staticimpl;

import cn.xa.walkerzg.proxy.Sender;

public class TimeStaticCombProxy implements Sender {
	Sender sender;

	public TimeStaticCombProxy(Sender sender) {
		this.sender = sender;
	}

	public boolean sendMsg(String msg) {
		Long start = System.currentTimeMillis();
		boolean rst = sender.sendMsg(msg);
		System.out.println(getClass().getSimpleName() + ":  " + (System.currentTimeMillis() - start) + "ms");
		return rst;
	}
}
