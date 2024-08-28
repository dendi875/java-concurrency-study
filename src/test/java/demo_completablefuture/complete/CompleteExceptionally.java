package demo_completablefuture.complete;

import demo_completablefuture.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * completeExceptionally 用于显式得将一个 CompletableFuture 标记为异常完成。
 *
 * 如果 CompletableFuture 成功地被标记为异常完成，方法返回 true；
 * 如果 CompletableFuture 已经被完成（无论是正常完成、异常完成还是取消），则无法改变其状态，方法将返回 false。
 *
 * 使用场景
 * 1. 异步任务失败处理:
 * 当你在执行一个异步任务时，如果任务因为某些原因失败了，
 * 则可以使用 completeExceptionally 来通知未来的监听者（those waiting on the future）这一结果。
 * 这对于异常处理非常有用，允许调用者根据异常做出相应的反应。
 *
 * 2. 单元测试:
 * 在测试使用 CompletableFuture 的代码时，completeExceptionally 可以用来模拟失败的异步操作。
 * 这可以帮助确保程序能够正确处理异常情况。
 *
 * 3. 错误传播:
 * 在复杂的业务逻辑中，一个任务的失败可能需要取消或影响一系列的后续任务。
 * completeExceptionally 允许将错误传播到依赖于当前任务结果的其他 CompletableFuture。
 *
 * 4. 条件性异常触发:
 * 在某些业务场景中，可能需要基于特定的条件触发异常。
 * 则可以使用 completeExceptionally 来终止正常流程，转而处理异常流程。
 *
 * 注意事项
 * 1. completeExceptionally 一次成功调用后，后续的任何尝试（包括 complete, completeExceptionally, 或 cancel）都将无效，
 * CompletableFuture 的状态一旦设定就不可更改。
 *
 * 2. 异常完成的 CompletableFuture 将导致依赖它的任何进一步的链式操作（如 thenApply, thenAccept）不会被正常执行，
 * 除非在操作中显式处理了异常情况。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 20:30:30
 */
public class CompleteExceptionally extends Demo {

	@Test
	public void tesCompleteExceptionally() {
		CompletableFuture<String> future = new CompletableFuture<>();

		assertFalse(future.isDone());
		assertFalse(future.isCompletedExceptionally());

		boolean hasCompleted = future.completeExceptionally(new RuntimeException("exception"));

		assertTrue(hasCompleted);
		assertTrue(future.isDone());
		assertTrue(future.isCompletedExceptionally());

		try {
			future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			assertEquals(RuntimeException.class, e.getCause().getClass());
			assertEquals("exception", e.getCause().getMessage());
		}
	}
}
