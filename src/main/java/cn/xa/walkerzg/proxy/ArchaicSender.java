package cn.xa.walkerzg.proxy;

import java.util.Random;

public class ArchaicSender implements Sender {
	public boolean sendMsg(String msg) {
		System.out.println("八百里加急: " + msg);
		try {
			Thread.sleep(new Random().nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
