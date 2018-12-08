package cn.xa.walkerzg.proxy.staticimpl;

import cn.xa.walkerzg.proxy.ArchaicSender;

public class TimeStaticInheritProxy extends ArchaicSender {
	@Override
	public boolean sendMsg(String msg) {
		Long start = System.currentTimeMillis();
		boolean rst = super.sendMsg(msg);
		System.out.println(getClass().getSimpleName() + ":  " +(System.currentTimeMillis() - start) + "ms");
		return rst;
	}
}
