package demo_completablefuture.pipeline.apply;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * thenCompose 方法允许你对两个连续的异步操作进行流水线处理，第一个操作的结果作为第二个操作的输入。
 * 它通常用于处理异步操作的结果，然后再次返回一个新的CompletableFuture。
 * 使用场景：当一个异步操作依赖于另一个异步操作的结果，且这些操作必须按顺序执行时使用。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 15:55:05
 */
public class ThenCompose extends Demo {

	@Test
	public void testThenCompose() throws ExecutionException, InterruptedException {
		CompletableFuture<String> stage1 = CompletableFuture.supplyAsync(() -> sleepAndGet("one"));

		CompletableFuture<String> stage = stage1.thenCompose(
				s -> CompletableFuture.supplyAsync(() -> (s + " " + sleepAndGet("two")).toUpperCase())
		);

		Assert.assertEquals("ONE TWO", stage.toCompletableFuture().get());
	}
}
