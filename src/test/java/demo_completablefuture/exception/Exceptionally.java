package demo_completablefuture.exception;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-23 19:13:00
 */
public class Exceptionally extends Demo {

	@Test
	public void testExceptionallySuccess() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.completedFuture("value")
				.exceptionally(t -> "failure: " + t.getMessage());

		Assert.assertTrue(future.isDone());
		Assert.assertFalse(future.isCompletedExceptionally());
		Assert.assertEquals("value", future.get());
	}

	@Test
	public void testExceptionallyError() throws ExecutionException, InterruptedException {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 1 / 0)
				.exceptionally((t) -> 1);

		Assert.assertTrue(future.isDone());
		Assert.assertFalse(future.isCompletedExceptionally());
		Assert.assertEquals(1, future.get().intValue());
	}
}
