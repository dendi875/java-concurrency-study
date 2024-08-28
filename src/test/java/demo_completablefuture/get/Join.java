package demo_completablefuture.get;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-27 19:02:35
 */
public class Join extends Demo {

	@Test
	public void testJoin() {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));

		// join 抛出的是未检查的异常，即运行时的异常，所以很适合在 Stream 和 Lambda 中使用
		Assert.assertEquals("value", future.join());
	}
}
