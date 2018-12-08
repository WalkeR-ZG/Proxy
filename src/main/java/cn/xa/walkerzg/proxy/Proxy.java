package cn.xa.walkerzg.proxy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

public class Proxy {
	private static final String proxyClassName = "$Proxy0";
	public static Object newProxyInstance(ClassLoader loader, Class<?> intf, InvocationHandler handler) throws Exception {
		String fileName = System.getProperty("user.dir") + "/src/main/java/" + dotToSlash(getPkg(intf)) + "/" + proxyClassName + ".java";
		genProxyJavaSrc(intf, fileName);
		String classFilePath = System.getProperty("user.dir") + "/target/classes/";
		javac(fileName, classFilePath);

		Class<?> cls = loader.loadClass(getPkg(intf) + "." + proxyClassName);
		Constructor<?> constructor = cls.getConstructor(InvocationHandler.class);
		Object o = constructor.newInstance(handler);
		return o;
	}
	private static void javac(String fileName, String classFilePath) throws IOException {
		JavaCompiler comp = ToolProvider.getSystemJavaCompiler();
		try (StandardJavaFileManager fileMgr = comp.getStandardFileManager(null, null, null)) {
			fileMgr.setLocation(StandardLocation.CLASS_OUTPUT,  Arrays.asList(new File[] { new File(classFilePath)}));
			Iterable<? extends JavaFileObject> javaFileObjects = fileMgr.getJavaFileObjects(fileName);
			CompilationTask task = comp.getTask(null, fileMgr, null, null, null, javaFileObjects);
			task.call();
		}
	}
	private static void genProxyJavaSrc(Class<?> intf, String fileName) throws IOException {
		File f = new File(fileName);
		try(FileWriter fw = new FileWriter(f)){	
			fw.write(formProxyJavaSrc(intf));
			fw.flush();
		} 
	}
	private static String formProxyJavaSrc(Class<?> intf) {
		String nl = "\r\n";
		StringBuilder sb = new StringBuilder(
		"package " + getPkg(intf) + ";" + nl +
		"import java.lang.reflect.Method;" + nl +
		"public class $Proxy0 implements  " + intf.getName() + "{" + nl + 
		"	private InvocationHandler h;" + nl + 

		"	public $Proxy0(InvocationHandler h) {" + nl + 
		"		super();" + nl + 
		"		this.h = h;" + nl + 
		"	}" + nl); 

		for(Method m : intf.getMethods()) {
			sb.append( 
				"	public " + m.getReturnType() + " " + m.getName() + "(" + getMethodPara(m) + ") {" + nl +
				"       Object rst = null;" + nl + 
				"       try{" + nl + 
				"           Method method = " + intf.getName()  + ".class.getMethod(\""+ m.getName() + "\"," + getMethodParaType(m)+");" + nl +
				"           rst = h.invoke(this, method," + "new Object[]{" +getArgs(m) +"});" + nl +
				"       }catch(Throwable e){ e.printStackTrace();}" + nl +
				"       return ("+m.getReturnType()+")rst;" + nl +
				"	}" + nl
				); 			
		}
		
		sb.append("}");
		return sb.toString();
	}
	private static String getPkg(Class<?> intf) {
		return intf.getName().substring(0, intf.getName().lastIndexOf('.'));
	}
    private static String dotToSlash(String name) {
        return name.replace('.', '/');
    }
	private static String getMethodPara(Method m) {
		StringBuilder sb = new StringBuilder();
		for(Parameter p: m.getParameters()) {
			sb.append(p.getType().getTypeName()+ " " + p.getName() +",") ;
		}
		if(sb.length() > 0) sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	private static String getMethodParaType(Method m) {
		StringBuilder sb = new StringBuilder();
		for(Parameter p: m.getParameters()) {
			sb.append(p.getType().getTypeName()+ ".class" +",") ;
		}
		if(sb.length() > 0) sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	private static String getArgs(Method m) {
		StringBuilder sb = new StringBuilder();
		for(Parameter p: m.getParameters()) {
			sb.append(p.getName() +",") ;
		}
		if(sb.length() > 0) sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}