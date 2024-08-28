package demo_completablefuture.check;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 * isCompletedExceptionally 检查 CompletableFuture 是否异常完成。
 *
 * true: 如果 CompletableFuture 已完成，且完成时抛出了异常
 * false: 如果 CompletableFuture 还未完成，或者已完成但没有异常
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 12:07:51
 */
public class IsCompletedExceptionally {

	@Test
	public void testIsCompletedExceptionallyFalse() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.completedFuture("value");

		assertTrue(future.isDone());
		assertFalse(future.isCompletedExceptionally());
		assertFalse(future.isCancelled());
		assertEquals("value", future.get());
	}

	@Test
	public void testIsCompletedExceptionallyTrue() {
		CompletableFuture<String> future = new CompletableFuture<>();

		// 手动设置异常完成
		future.completeExceptionally(new RuntimeException("exception"));

		assertTrue(future.isDone());
		assertTrue(future.isCompletedExceptionally());
		assertFalse(future.isCancelled());

		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			assertTrue(e.getCause() instanceof RuntimeException);
			assertEquals("exception", e.getCause().getMessage());
		}
	}
}
