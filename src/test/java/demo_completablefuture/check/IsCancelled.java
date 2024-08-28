package demo_completablefuture.check;

import demo_completablefuture.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;

/**
 * isCancelled 检查 CompletableFuture 是否已被取消。
 *
 * true: CompletableFuture 被取消。
 * false: CompletableFuture 还没有被取消，无论它是否已完成或异常结束。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 13:50:06
 */
public class IsCancelled extends Demo {

	@Test
	public void testIsCancelledFalse() {
		CompletableFuture<String> future = CompletableFuture.completedFuture("value");

		assertTrue(future.isDone());
		assertFalse(future.isCompletedExceptionally());
		assertFalse(future.isCancelled());
	}

	@Test
	public void testIsCancelledTrue() {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));

		assertFalse(future.isDone());
		assertFalse(future.isCompletedExceptionally());
		assertFalse(future.isCancelled());

		// 当 CompletableFuture 被取消时，它实际上是被异常完成了，通常关联一个 CancellationException
		future.cancel(true);

		assertTrue(future.isDone()); // 任务正常完成、异常完成、被取消都是完成
		assertTrue(future.isCompletedExceptionally()); // 已完成且抛出了异常
		assertTrue(future.isCancelled());
	}
}
