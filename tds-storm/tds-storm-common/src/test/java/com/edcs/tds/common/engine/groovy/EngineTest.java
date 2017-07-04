package com.edcs.tds.common.engine.groovy;

import com.edcs.tds.common.engine.groovy.exception.GroovyCheckError;
import com.edcs.tds.common.engine.groovy.exception.GroovyException;

import groovy.lang.Binding;
import groovy.lang.Script;

public class EngineTest {

	public static void main(String[] args) throws GroovyCheckError, GroovyException {

		StringBuffer scriptText = new StringBuffer(
				"int c = a + b;System.out.println(c); if(c > 6){return 一级报警+'';}else{return '二级报警';};");

		ScriptCacheMapping scriptCacheMapping = new ScriptCacheMapping();
		scriptCacheMapping.addScript("1", "hashcode1", ScriptExecutor.getDefaultShell().parse(scriptText.toString()));
		
		for (int i = 0; i < 20; i++) {
			Binding shellContext = new Binding();
			shellContext.setProperty("a", i*3);
			shellContext.setProperty("b", i*2);

			ScriptExecutor executor = new ScriptExecutor();
			Script script2 = scriptCacheMapping.getScript("1");
			System.out.println(script2.toString());
			script2.setBinding(shellContext);

			long st = System.currentTimeMillis();

			String bool = executor.execute(script2);
			long et = System.currentTimeMillis();

			System.out.println("Calc time used:" + (et - st) + " ms, result:" + bool);
		}

	}

}
