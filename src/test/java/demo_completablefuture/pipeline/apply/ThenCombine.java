package demo_completablefuture.pipeline.apply;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 * thenCombine 它允许你将两个独立的CompletionStage任务的结果进行合并，并对这两个结果执行某个函数，返回一个新的CompletableFuture。
 *
 * thenCombine方法常用于以下场景：
 *
 * 结果合并：当你需要从两个独立的异步任务中获取结果，并将这两个结果合并为一个结果时。例如，从两个不同的数据源获取数据，然后将这些数据合并以提供一个统一的视图。
 *
 * 依赖任务：有些情况下，单个任务的执行可能依赖于两个或多个先前任务的结果。使用thenCombine，你可以确保只有在所有相关任务都完成后，才执行依赖的操作。
 *
 * 资源优化：在处理需要多个资源的场景时，可以并行地调用异步任务来获取这些资源，然后使用thenCombine来整合结果，从而优化响应时间和资源使用。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 18:27:35
 */
public class ThenCombine extends Demo {

	@Test
	public void testThenCombine() throws ExecutionException, InterruptedException {
		CompletableFuture<String> stage1 = CompletableFuture.supplyAsync(() -> sleepAndGet("one"));
		CompletableFuture<String> stage2 = CompletableFuture.supplyAsync(() -> sleepAndGet("two"));

		CompletableFuture<String> stage = stage1.thenCombine(stage2, (s1, s2) -> ( s1 + " " + s2).toUpperCase());

		Assert.assertEquals("ONE TWO", stage.toCompletableFuture().get());
	}
}
