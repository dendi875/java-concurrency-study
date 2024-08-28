package demo_completablefuture.get;

import demo_completablefuture.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.*;

/**
 * get 用于同步获取异步计算的结果。当调用 get 方法时，
 * 当前线程会阻塞，直到 CompletableFuture 完成计算并获取得结果，或者直到发生异常
 *
 * 注意事项
 * 1. 阻塞操作：get() 是一个阻塞调用，它会停止调用线程，直到任务完成。这可能会导致线程资源的浪费，特别是在多线程应用程序中。
 * 2. 中断处理：get() 方法会抛出 InterruptedException，这表明等待过程中线程被中断。
 * 通常需要在捕获该异常后重新设置中断状态或进行相应的中断处理。
 * 3. 异常封装：计算过程中抛出的异常会被封装在 ExecutionException 中，需要通过 getCause() 方法来获取实际的异常。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 20:56:03
 */
public class Get extends Demo {

	@Test
	public void testGet() {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));

		try {
			assertEquals("value", future.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
