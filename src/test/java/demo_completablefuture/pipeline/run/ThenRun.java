package demo_completablefuture.pipeline.run;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * thenRun 用于在一个CompletableFuture完成后执行一个不需要输入参数、也不产生输出结果的Runnable任务。
 * 这个方法通常用于执行一些不依赖于前一个任务结果的后续操作，如资源清理、通知等。
 *
 * thenRun方法可以在多种情况下使用，特别是在与异步操作的最终阶段相关的场景中：
 *
 * 触发结束操作：例如，当一系列异步计算完成后，你可能需要关闭资源、记录日志或通知其他系统。
 * 执行独立的后续操作：在不需要前一个任务的结果来执行后续操作的场景中，如发送一个统计报告的信号、启动另一个独立的异步过程等。
 * 清理资源：在异步操作结束后执行清理或释放资源的操作，确保系统资源得到妥善管理。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 19:03:34
 */
public class ThenRun extends Demo {

	@Test
	public void testThenRun() throws ExecutionException, InterruptedException {
		CompletableFuture<String> stage1 = CompletableFuture.supplyAsync(() -> sleepAndGet("one"));

		CompletableFuture<Void> stage = stage1.thenRun(() -> logger.info("runs after two"));

		Assert.assertNull(stage.toCompletableFuture().get());
	}
}
