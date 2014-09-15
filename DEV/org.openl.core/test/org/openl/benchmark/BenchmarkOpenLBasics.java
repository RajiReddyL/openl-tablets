package org.openl.benchmark;

/**
 * Created Jul 7, 2007
 */



import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.openl.runtime.IRuntimeContext;
import org.openl.util.benchmark.Benchmark;
import org.openl.util.benchmark.BenchmarkInfo;
import org.openl.util.benchmark.BenchmarkUnit;
import org.openl.vm.IRuntimeEnv;
import org.openl.vm.SimpleVM;

/**
 * @author snshor
 * 
 */
public class BenchmarkOpenLBasics {


    private static class TestBooleanUtils1 extends BenchmarkUnit {

		@Override
		protected void run() throws Exception {
			Boolean b = BooleanUtils.toBoolean("y");
			if (!b)
				throw new Exception("y!");
		}
    }	

    private static class TestBooleanUtils2 extends BenchmarkUnit {

		@Override
		protected void run() throws Exception {
			Boolean b =BooleanUtils.toBoolean("True");
			if (!b)
				throw new Exception("True!");
		}
    }	
	
    private static class TestRuntimeCloning extends BenchmarkUnit {
        IRuntimeEnv env = new SimpleVM().getRuntimeEnv();


         TestRuntimeCloning() throws Exception {
        	env.pushLocalFrame(new Object[]{"aaa", "bbb"});
        	env.pushThis(this);
        	
        	env.pushContext(new Cxt());
        }

        @Override
        protected void run() throws Exception {
        	env.cloneEnvForMT();
        }
    }

    
    static class Cxt implements IRuntimeContext
    {
    	public IRuntimeContext clone()
    	{
    		return new Cxt();
    	}
    }


    public static void main(String[] args) throws Exception {
        
    	BenchmarkUnit[] bu = {new TestRuntimeCloning() 
    						  ,new TestBooleanUtils1()
		  						,new TestBooleanUtils2()
    	};
    	

        List<BenchmarkInfo> res = new Benchmark().measureAllInList(bu, 1000);

        for (BenchmarkInfo bi : res) {

            System.out.println(bi);

        }
    }

}
