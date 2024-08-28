package demo_completablefuture.pipeline.accept;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ThenApply_vs_ThenCompose extends Demo {

	@Test
	public void testThenApplyFat() throws ExecutionException, InterruptedException {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 2)
				.thenApply(i -> i + 3); // Function<Integer, Integer>

		Assert.assertEquals(5, future.get().intValue());
	}

	/**
	 * 这个方法演示了当使用thenApply返回一个新的CompletableFuture时的情况。thenApply仍然使用一个Function，
	 * 但这个函数返回的是一个CompletableFuture<Integer>。
	 *
	 * 这种情况下，结果是一个嵌套的CompletableFuture（即CompletableFuture<CompletableFuture<Integer>>）。
	 * 为了获得最终的结果，需要先解开外层的Future，这就导致了代码较为冗长和复杂。
	 */
	@Test
	public void testThenApplySlow() throws ExecutionException, InterruptedException {
		CompletableFuture<CompletableFuture<Integer>> future1 = CompletableFuture.supplyAsync(() -> 2)
				.thenApply(i -> CompletableFuture.supplyAsync(() -> i + 3)); // Function<Integer, CompletableFuture<Integer>>

		CompletableFuture<Integer> future2 = future1.get();

		Assert.assertEquals(5, future2.get().intValue());
	}

	/**
	 * thenCompose用于处理嵌套的CompletableFuture情况，它接受一个函数，这个函数返回一个新的CompletionStage。
	 * 这使得thenCompose能够平滑地处理连续的异步操作，不会产生嵌套的CompletableFuture。
	 *
	 * 在这个示例中，thenCompose有效地解决了嵌套的问题，让代码更加简洁，并且提高了可读性和易维护性。
	 */
	@Test
	public void testThenCompose() throws ExecutionException, InterruptedException {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 2)
				.thenCompose(i -> CompletableFuture.supplyAsync(() -> i + 3));// Function<Integer, CompletableFuture<Integer>>

		Assert.assertEquals(5, future.get().intValue());
	}
}
