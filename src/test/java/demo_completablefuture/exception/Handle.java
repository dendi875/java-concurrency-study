package demo_completablefuture.exception;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Handle extends Demo {

	@Test
	public void testHandleSuccess() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.completedFuture("value")
				.handle((value, t) -> {
					if (t == null) {
						return value.toUpperCase();
					} else {
						return t.getMessage();
					}
				});

		Assert.assertTrue(future.isDone());
		Assert.assertFalse(future.isCompletedExceptionally());
		Assert.assertEquals("VALUE", future.get());
	}

	@Test
	public void testHandleError() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->  1 / 0)
				.handle((value, t) -> {
					if (t == null) {
						return value.toString();
					} else {
						return "failure";
					}
				});

		Assert.assertTrue(future.isDone());
		Assert.assertFalse(future.isCompletedExceptionally());
		Assert.assertEquals("failure", future.get());
	}
}
