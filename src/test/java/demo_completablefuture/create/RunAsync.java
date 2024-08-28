package demo_completablefuture.create;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * runAsync 创建了一个不完整的 future，该 future 在运行给定的 Runnable 任务后异步完成。
 *
 * runAsync 用于异步执行一个没有返回结果的任务（即一个 Runnable）。
 * 它适用于那些你只关心执行操作而不在意结果的场景，例如发送日志、清理资源或者其他不返回值的背景任务。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-26 10:44:43
 */
public class RunAsync extends Demo {

	@Test
	public void testRunAsync() throws ExecutionException, InterruptedException {
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> logger.info("action"));
		Assert.assertNull(future.get()); // get 抛出的是检查异常，需要显式处理
	}
}
