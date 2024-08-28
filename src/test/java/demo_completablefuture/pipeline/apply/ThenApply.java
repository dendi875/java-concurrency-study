package demo_completablefuture.pipeline.apply;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 * thenApply 方法接收一个Function类型的参数，这个参数将CompletableFuture完成时的结果作为输入，并返回一个新的结果。
 * 这个方法通常用于对异步操作的结果进行一些转换或处理。
 * 使用场景：当你需要对异步处理的结果进行进一步的处理或转换时使用。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 15:40:24
 */
public class ThenApply extends Demo {

	@Test
	public void testThenApply() throws ExecutionException, InterruptedException {
		CompletableFuture<String> stage1 = CompletableFuture.supplyAsync(() -> sleepAndGet("single"));

		CompletableFuture<String> stage = stage1.thenApply(s -> s.toUpperCase());

		Assert.assertEquals("SINGLE", stage.toCompletableFuture().get());
	}
}
