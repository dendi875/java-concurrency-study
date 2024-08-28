package demo_completablefuture.complete;

import demo_completablefuture.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * complete 它显式地将给定的值作为结果设置到 CompletableFuture 对象中。
 * 这个方法的调用会将 CompletableFuture 的状态改变为完成状态，
 * 并且之后的所有调用 get() 或者 join() 方法将会立即返回这个之前通过 complete 方法设置的结果。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 20:20:55
 */
public class Complete extends Demo {

	@Test
	public void testComplete() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = new CompletableFuture<>();

		assertFalse(future.isDone());

		boolean hasCompleted = future.complete("value");

		assertTrue(hasCompleted);
		assertTrue(future.isDone());
		assertEquals("value", future.get());
	}
}
