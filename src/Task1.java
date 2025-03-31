import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main1 {
    public static void main(String[] args) {
        double a = 3.0, b = 4.0, c = 10.0, d = 16.0;

        CompletableFuture<Double> sumOfSquares = CompletableFuture.supplyAsync(() -> {
            sleep(5);
            double result = Math.pow(a, 2) + Math.pow(b, 2);
            System.out.println("Calculating sum of squares: " + result);
            return result;
        });
        CompletableFuture<Double> sqrtD = CompletableFuture.supplyAsync(() -> {
            sleep(10);
            double result = Math.sqrt(d);
            System.out.println("Calculating sqrt(d): " + result);
            return result;
        });
        CompletableFuture<Double> logC = CompletableFuture.supplyAsync(() -> {
            sleep(15);
            double result = Math.log(c);
            System.out.println("Calculating log(c): " + result);
            return result;
        });

        CompletableFuture<Double> finalResult = sumOfSquares.thenCombine(logC, (sum, log) -> sum * log)
                .thenCombine(sqrtD, (multiplied, sqrt) -> multiplied / sqrt);

        System.out.println("Final result of the formula: " + finalResult.join());
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
