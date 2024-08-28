package demo_completablefuture.create;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletedFuture 它可以创建一个已正常完成的 future
 *
 * 用于创建一个已经完成的CompletableFuture实例。
 * 这个方法特别有用当你已经有一个立即可用的结果时，而你需要一个CompletableFuture来适配现有的API或维持代码的一致性。
 *
 * 简化代码：当你需要将一个同步计算的结果适配到一个返回CompletableFuture的方法时，
 * 可以使用completedFuture来包装结果，而不需要重新组织代码以使用异步方式。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 10:52:41
 */
public class CompletedFuture extends Demo {

	@Test
	public void testCompletedFuture() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.completedFuture("value");

		Assert.assertTrue(future.isDone());
		Assert.assertFalse(future.isCompletedExceptionally());
		Assert.assertEquals("value", future.get());
	}
}
