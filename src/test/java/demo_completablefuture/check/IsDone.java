package demo_completablefuture.check;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * isDone 检查 CompletableFuture 是否已完成
 *
 * true: 无论是正常完成、还是异常完成、或任务被取消
 * false: 还在等待完成
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 11:44:59
 */
public class IsDone extends Demo {

	@Test
	public void testIsDoneTrue() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.completedFuture("value");

		Assert.assertTrue(future.isDone());
		Assert.assertFalse(future.isCompletedExceptionally());
		Assert.assertFalse(future.isCancelled());
		Assert.assertEquals("value", future.get());
	}

	@Test
	public void testIsDoneFalse() {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));

		Assert.assertFalse(future.isDone());
		Assert.assertFalse(future.isCompletedExceptionally());
		Assert.assertTrue(future.isCancelled());
	}
}
