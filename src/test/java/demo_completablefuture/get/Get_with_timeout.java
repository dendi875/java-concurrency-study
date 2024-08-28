package demo_completablefuture.get;

import demo_completablefuture.common.Demo;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * CompletableFuture.get(long timeout, TimeUnit unit) 方法允许你设置一个超时时间，用于获取 CompletableFuture 的结果。
 * 如果在指定的时间内 CompletableFuture 没有完成，这个方法将抛出一个 TimeoutException。
 * 这是一个非常有用的特性，因为它允许程序在等待异步操作结果的时候不会被无限期地阻塞。
 *
 * 1. 在使用超时功能时，始终要准备处理可能的 TimeoutException，并根据应用程序的需要决定如何最好地处理这种情况。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-27 20:31:55
 */
public class Get_with_timeout extends Demo {

	@Test
	public void testGetWithTimeoutSuccess() throws InterruptedException, ExecutionException, TimeoutException {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value"));

		assertEquals("value", future.get(3, TimeUnit.SECONDS));
	}

	@Test
	public void testGetWithTimeoutFailure() {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value"));

		try {
			future.get(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			assertTrue(true);
		}
	}
}
