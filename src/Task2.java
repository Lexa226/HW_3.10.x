import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

public class Task2 {
    static class Transaction {
        final int fromId;
        final int toId;
        final int amount;

        Transaction(int fromId, int toId, int amount) {
            this.fromId = fromId;
            this.toId = toId;
            this.amount = amount;
        }
    }

    public static void main(String[] args) {
        int n = 4;
        int[] balances = {500, 200, 300, 400};
        List<Transaction> transactions = List.of(
                new Transaction(0, 1, 100),
                new Transaction(2, 3, 50),
                new Transaction(1, 3, 100)
        );

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<?>> futures = new ArrayList<>();
        Object[] locks = new Object[n];
        for (int i = 0; i < n; i++) {
            locks[i] = new Object();
        }

        for (Transaction transaction : transactions) {
            futures.add(executor.submit(() -> {
                synchronized (locks[Math.min(transaction.fromId, transaction.toId)]) {
                    synchronized (locks[Math.max(transaction.fromId, transaction.toId)]) {
                        if (balances[transaction.fromId] >= transaction.amount) {
                            balances[transaction.fromId] -= transaction.amount;
                            balances[transaction.toId] += transaction.amount;
                        }
                    }
                }
            }));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        }

        executor.shutdown();

        for (int i = 0; i < n; i++) {
            System.out.println("User " + i + " final balance: " + balances[i]);
        }
    }
}
