package demo_completablefuture.overview;

import demo_completablefuture.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 管道计算的方法
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 10:30:55
 */
public class MethodsToPipelineComputations extends Demo {

	// area = π * r^2
	@Test
	public void test() {
		CompletableFuture<Double> pi = CompletableFuture.supplyAsync(() -> Math.PI);
		CompletableFuture<Integer> radius = CompletableFuture.supplyAsync(() -> 1);

		CompletableFuture<Void> area = radius.thenApply(r -> r * r)
				.thenCombine(pi, (square, pai) -> pai * square)
				.thenAccept(a -> logger.info("area: {}", a))
				.thenRun(() -> logger.info("operation completed"));

		area.join(); // 它不抛出 InterruptedException，所以经常用在流式lambda中
	}
}
