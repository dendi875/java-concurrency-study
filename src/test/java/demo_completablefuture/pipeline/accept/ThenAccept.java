package demo_completablefuture.pipeline.accept;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 * thenAccept 用于处理异步操作的结果。当CompletableFuture完成时，thenAccept方法提供的消费者（Consumer）函数会被自动调用，
 * 并接收CompletableFuture返回的结果作为输入。这个方法不返回任何值，通常用于执行一些侧效操作，如记录日志、发送通知等。
 *
 * thenAccept方法通常用于以下场景：
 *
 * 结果处理：当异步计算完成后，你需要对结果进行一些处理，例如更新UI、通知用户、写入日志等。
 *
 * 链式操作：可以在处理完结果之后继续进行其他的链式操作，如thenRun或者再次使用thenAccept。
 *
 * 无需返回值的操作：当你只需要执行一些操作，而不需要关心返回值时，thenAccept是一个很好的选择。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 18:37:05
 */
public class ThenAccept extends Demo {

	@Test
	public void testThenAccept() throws ExecutionException, InterruptedException {
		CompletableFuture<String> stage1 = CompletableFuture.supplyAsync(() -> sleepAndGet("one"));

		CompletableFuture<Void> stage = stage1.thenAccept(s -> logger.info("consumers the value: {}", s));

		Assert.assertNull(stage.toCompletableFuture().get());
	}
}
