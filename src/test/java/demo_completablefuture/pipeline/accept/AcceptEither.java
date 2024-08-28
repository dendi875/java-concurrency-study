package demo_completablefuture.pipeline.accept;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * AcceptEither 它允许你在两个 CompletableFuture 之间进行选择，只处理最先完成的一个的结果。
 * 当其中一个 CompletableFuture 完成时，你提供的消费者（Consumer）函数将被执行，对结果进行处理。
 *
 * acceptEither 方法通常用于以下场景：
 *
 * 快速响应：当你有两个异步操作，而你只关心哪个先完成，并且只需要处理先完成的那个操作的结果。
 * 冗余请求：适用于可能向多个服务或资源请求相同的数据，但只需从最快的响应中获得数据的情况。
 * 容错：可以从多个源获取数据，确保即使一个源失败，其他源仍可能成功。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 18:41:40
 */
public class AcceptEither extends Demo {

	@Test
	public void testAcceptEither() throws ExecutionException, InterruptedException {
		CompletableFuture<String> stage1 = CompletableFuture.supplyAsync(() -> sleepAndGet(1,"one"));
		CompletableFuture<String> stage2 = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "two"));

		CompletableFuture<Void> stage = stage1.acceptEither(stage2, s -> logger.info("consumer the value: {}", s));

		Assert.assertNull(stage.toCompletableFuture().get());
	}
}
