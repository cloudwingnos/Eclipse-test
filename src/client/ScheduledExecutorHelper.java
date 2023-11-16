package client;
/** 

* @author  Donghao.F 

* @date 2021 Mar 14 12:24:00 

* 

*/
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.sg.common.ImdgLogger;

public class ScheduledExecutorHelper {

	
	public static void scheduleAtFixedRateWithTimeo(ScheduledExecutorService exec, Runnable task, long period,
			long timeout, TimeUnit unit) {
		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				runwithTimeout(exec, task, timeout,unit);
			}
		}, 0, period, unit);
	}
	
	
	
	public static void runwithTimeout(ScheduledExecutorService exec, Runnable task, long timeout, TimeUnit unit) {
		ScheduledFuture<?> f = exec.schedule(task, 0, unit);
		try {
			ImdgLogger.info("time out check:" + timeout + unit.name());
			
			f.get(timeout, unit);
		} catch ( TimeoutException e) {
			f.cancel(true);
			ImdgLogger.info("Time out : task is canceled");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
