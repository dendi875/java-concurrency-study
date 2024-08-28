package demo_completablefuture.misc;

import demo_completablefuture.common.Demo;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * obtrudeException 用于强制完成一个 CompletableFuture 并以一个异常结束。
 * 此方法将无视 CompletableFuture 的当前状态（无认其是否已完成，或是已经有一个值或另一个异常），
 * 并将其状态设置为异常完成，持有你提供的异常对象。
 *
 * 使用场景：
 * 此方法通常用在特定的测试场景或者需要强行终止某个操作并报告错误的情况。
 * 例如，你可能需要在确定某个长时间运行的任务由于外部原因需要立即停止并报错时使用这个方法。
 *
 * 注意事项：
 * 1. 使用 obtrudeException 可以在任何时候更改 CompletableFuture 的状态，这可能会导致不可预见的副作用，
 * 特别是在多线程环境中。它会破坏 CompletableFuture 的正常完成，可能会对依赖于其结果的其他部分产生连锁反应。
 *
 * 2. 此方法应谨慎使用，通常只在测试或特殊场景中使用，以避免代码逻辑混乱和难以debug的问题。
 *
 * obtrudeException 提供了一种强力手段来控制 CompletableFuture 的行为，但也带来了相应的风险，
 * 因此在实际应用中应当慎重考虑其使用场景。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-28 15:40:39
 */
public class ObtrudeException extends Demo {

	@Test
	public void testObtrudeException1() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.completedFuture("value");

		assertTrue(future.isDone());
		assertFalse(future.isCompletedExceptionally());
		assertEquals("value", future.get());

		future.obtrudeException(new RuntimeException("exception"));

		assertTrue(future.isDone());
		assertTrue(future.isCompletedExceptionally());
	}

	@Test
	public void testObtrudeException2() {
		CompletableFuture<String> future = new CompletableFuture<>();
		future.completeExceptionally(new RuntimeException("exception1"));

		assertTrue(future.isDone());
		assertTrue(future.isCompletedExceptionally());

		future.obtrudeException(new RuntimeException("exception2"));

		assertTrue(future.isDone());
		assertTrue(future.isCompletedExceptionally());
	}
}
