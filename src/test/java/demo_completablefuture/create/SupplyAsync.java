package demo_completablefuture.create;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * supplyAsync 创建了一个不完整的 future，该 future 从给定的 Supply 中获取值后异步完成。
 * 用于异步执行一个 Supply 任务，这个任务将返回一个结果。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 10:38:30
 */
public class SupplyAsync extends Demo {

	@Test
	public void testSupplyAsync() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));
		Assert.assertEquals("value", future.get());
	}
}
