package cn.xa.walkerzg.proxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ProxyTest {
    private PrintStream console = null;
    private ByteArrayOutputStream bytes = null;
    private Sender sender;

    @Before
    public void setUp() throws Exception{
        sender = (Sender)Proxy.newProxyInstance(Sender.class.getClassLoader(), Sender.class, new LogInvocationHandler(new ArchaicSender()));;
        bytes = new ByteArrayOutputStream();
        console = System.out;
        System.setOut(new PrintStream(bytes));
    }

    @After
    public void tearDown(){
        System.setOut(console);
    }

    @Test
    public void testProxy() {
		sender.sendMsg("新皇登基...");
        String result = bytes.toString();
        assertTrue(result.contains("Enter sendMsg") && result.contains("八百里加急: 新皇登基...") && result.contains("Exit sendMsg"));
	}
}
