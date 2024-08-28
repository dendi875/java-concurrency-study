package demo_completablefuture.misc;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * getNumberOfDependents() 它用来返回当前 CompletableFuture 对象有多少个依赖。这些依赖是指那些在当前
 * CompletableFuture 完成时需要被执行的其它 CompletableFuture 实例。
 *
 * 使用场景：此方法主要用于调试和监控目的，了解某个 CompletableFuture 对象在异步流中的影响范围，
 * 或者检测是否存在意外的依赖，可能导致内存泄漏或性能问题。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-28 15:28:37
 */
public class GetNumberOfDependents extends Demo {

	@Test
	public void testGetNumberOfDependents() {
		CompletableFuture<String> originalFuture = CompletableFuture.supplyAsync(() -> sleepAndGet("Hello"));

		// 创建依赖于 originalFuture 的其它 CompletableFuture 对象
		CompletableFuture<Void> dependentFuture1 = originalFuture.thenAccept(System.out::println);
		CompletableFuture<String> dependentFuture2 = originalFuture.thenApply(v -> v + " World");

		Assert.assertEquals(2, originalFuture.getNumberOfDependents());
	}
}
