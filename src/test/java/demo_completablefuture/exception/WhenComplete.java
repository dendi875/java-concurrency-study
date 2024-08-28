package demo_completablefuture.exception;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * whenComplete 无论该计算是正常完成还是异常完成，该回调都会被执行。
 *
 * action: 一个BiConsumer，它接收两个输入参数：计算的结果和发生的异常。如果计算成功完成，
 * 异常将是null；如果计算抛出异常，结果将是null。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-23 18:31:22
 */
public class WhenComplete extends Demo {

	@Test
	public void testWhenCompleteSuccess() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.completedFuture("value")
				.whenComplete((value, t) -> {
					if (t == null) {
						logger.info("success: {}", value);
					} else {
						logger.warn("failure: {}", t.getMessage());
					}
				});

		Assert.assertTrue(future.isDone());
		Assert.assertFalse(future.isCompletedExceptionally());
		Assert.assertEquals("value", future.get());
	}

	@Test
	public void testWhenCompleteError() throws ExecutionException, InterruptedException {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 1 / 0)
				.whenComplete((value, t) -> {
					if (t == null) {
						logger.info("success: {}", value);
					} else {
						logger.warn("failure: {}", t.getMessage());
					}
				});

		Assert.assertTrue(future.isDone());
		Assert.assertTrue(future.isCompletedExceptionally());
	}
}
