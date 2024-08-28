package demo_completablefuture.misc;

import demo_completablefuture.common.Demo;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * obtrudeValue 用来强制完成一个 CompletableFuture 并以一个指定的值结束。
 * 这个方法将无视 CompletableFuture 的当前状态（无认其是否已完成，或是已经有一个值或异常），并将其状态设置为已完成，并持有你指定的值。
 *
 * 使用场景：
 * 这个方法通常在测试或特定的错误恢复场景中使用，当你需要覆盖 CompletableFuture 的当前结果或异常状态，
 * 并强制它返回一个特定的值时。例如，在某些情况下，可能需要强制一个操作返回成功的结果，以便进行后续处理。
 *
 * 注意事项
 * 1. 与 obtrudeException 类似，obtrudeValue 可以在任何时间点更改 CompletableFuture 的状态，
 * 这可能会导致不可预见的副作用，尤其是在多线程环境中。
 *
 * 2. 此方法应该谨慎使用，通常只在测试或非常特殊的场景中使用，以避免代码逻辑混乱和难以调试的问题。
 *
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-28 16:01:28
 */
public class ObtrudeValue extends Demo {

	@Test
	public void testObtrudeValue1() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.completedFuture("value");

		assertTrue(future.isDone());
		assertFalse(future.isCompletedExceptionally());
		assertEquals("value", future.get());

		future.obtrudeValue("value1");

		assertTrue(future.isDone());
		assertFalse(future.isCompletedExceptionally());
		assertEquals("value1", future.get());
	}

	@Test
	public void testObtrudeValue2() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = new CompletableFuture<>();
		future.completeExceptionally(new RuntimeException("exception"));

		assertTrue(future.isDone());
		assertTrue(future.isCompletedExceptionally());

		future.obtrudeValue("value");

		assertTrue(future.isDone());
		assertFalse(future.isCompletedExceptionally());
		assertEquals("value", future.get());
	}
}
