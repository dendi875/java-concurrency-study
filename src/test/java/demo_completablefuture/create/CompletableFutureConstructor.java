package demo_completablefuture.create;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 无参构造器创建了一个不完整的 future
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 10:26:24
 */
public class CompletableFutureConstructor  {

	@Test
	public void testConstructor() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = new CompletableFuture<>();

		Assert.assertFalse(future.isDone());

		future.complete("value"); // 手动完成一个 CompletableFuture，设置它的结果

		Assert.assertTrue(future.isDone());

		Assert.assertEquals("value", future.get());
	}
}
