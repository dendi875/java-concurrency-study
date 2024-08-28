package demo_completablefuture.pipeline.apply;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 * applyToEither 它允许你处理两个异步计算的结果，并且只关心最先完成的那个计算的结果。
 *
 * applyToEither方法通常用于以下场景：
 *
 * 竞争资源获取：当你有多个相似的异步任务，但你只需要第一个完成的结果时，例如从多个源获取数据，哪个先返回就使用哪个。
 *
 * 备份策略：当主要的异步任务可能失败或延迟，你可以启动一个备份任务。使用applyToEither，无论是主任务还是备份任务先完成，你都可以获取到结果进行处理。
 *
 * 超时处理：可以将一个长时间运行的异步任务与一个延迟任务（作为超时）结合使用，哪个先完成就使用哪个的结果。
 * 
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 17:57:06
 */
public class ApplyToEither extends Demo {

	@Test
	public void testApplyToEither() throws ExecutionException, InterruptedException {
		CompletableFuture<String> stage1 = CompletableFuture.supplyAsync(() -> sleepAndGet(1, "one"));
		CompletableFuture<String> stage2 = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "two"));

		CompletableFuture<String> stage = stage1.applyToEither(stage2, s -> s.toUpperCase());

		Assert.assertEquals("ONE", stage.toCompletableFuture().get());

	}
}
