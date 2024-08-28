package demo_completablefuture.complete;

import org.junit.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * 用于尝试取消执行当前的 CompletableFuture。
 * 此方法的行为类似于 Future 接口中的 cancel 方法，但有一些特定的行为和注意事项。
 *
 * 1. 参数 mayInterruptIfRunning 指示正在运行的任务是否可以被中断。然而，对于 CompletableFuture 的实现来说，
 * 这个标志实际上没有被使用，因为 CompletableFuture 本身并不管理或执行任务。任务的执行通常是由提供给它的 Executor 管理的。
 *
 * 2. 方法返回一个布尔值，表示取消操作是否成功。成功的取消将会使 CompletableFuture 进入“已完成”状态，
 * 但它将被视为是异常完成的。
 *
 * 注意事项
 * 1. cancel() 调用实际上是通过内部调用 completeExceptionally(new CancellationException()) 来实现的。
 * 2. 一旦 CompletableFuture 被取消，它就不能再被完成或再次取消。
 * 3. 取消是不可逆的操作，一旦取消，任何试图获取结果的操作都会抛出 CancellationException。
 * 4. cancel 方法的 mayInterruptIfRunning 参数在 CompletableFuture 中实际上是无效的，
 * 因为 CompletableFuture 本身不负责执行任务。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 20:46:18
 */
public class Cancel {

	@Test
	public void testCancel() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = new CompletableFuture<>();

		assertFalse(future.isDone());
		assertFalse(future.isCompletedExceptionally());
		assertFalse(future.isCancelled());

		future.cancel(false);

		assertTrue(future.isDone());
		assertTrue(future.isCompletedExceptionally());
		assertTrue(future.isCancelled());

		try {
			future.get();
		} catch (CancellationException e) {
			assertTrue(true);
		}
	}
}
