import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.State;

// Clase principal
class Main {
    public static void main(String[] args) {
        // Informacion de prioridad de los hilos
        System.out.printf("Prioridad Minima: %s\n", Thread.MIN_PRIORITY);
        System.out.printf("Prioridad Normal: %s\n", Thread.NORM_PRIORITY);
        System.out.printf("Prioridad Maxima: %s\n", Thread.MAX_PRIORITY);

        // Objeto Thread
        Thread threads[];
        Thread.State status[];
        // Creamos 10 hilos
        threads = new Thread[10];
        status = new Thread.State[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Calculator());
            if ((i % 2) == 0) {
                threads[i].setPriority(Thread.MAX_PRIORITY);
            } else {
                threads[i].setPriority(Thread.MIN_PRIORITY);
            }
            threads[i].setName("My Thread " + i);
        }

        // Archivo de escritura
        try (FileWriter file = new FileWriter(".\\data\\log.txt");
                PrintWriter pw = new PrintWriter(file);) {

            for (int i = 0; i < 10; i++) {
                pw.println("Main : Status of Thread " + i + " : " +
                        threads[i].getState());
                status[i] = threads[i].getState();
            }
            for (int i = 0; i < 10; i++) {
                threads[i].start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cambio de informacion cuando el hilo cambie de estado
        boolean finish = false;
        while (!finish) {
            for (int i = 0; i < 10; i++) {
                if (threads[i].getState() != status[i]) {
                    PrintWriter pw = null;
                    writeThreadInfo(pw, threads[i], status[i]);
                    status[i] = threads[i].getState();
                }
            }

            finish = true;
            for (int i = 0; i < 10; i++) {
                finish = finish && (threads[i].getState() == State.TERMINATED);
            }
        }

    }

    private static void writeThreadInfo(PrintWriter pw, Thread thread, State state) {
        pw.printf("Main : Id %d - %s\n", thread.getId(), thread.getName());
        pw.printf("Main : Priority: %d\n", thread.getPriority());
        pw.printf("Main : Old State: %s\n", state);
        pw.printf("Main : New State: %s\n", thread.getState());
        pw.printf("Main : ************************************\n");
    }
}

// Ejemplo
class Calculator implements Runnable {
    @Override
    public void run() {
        long current = 1L;
        long max = 20000L;
        long numPrimes = 0L;

        // currentThread() -> Metodo estatico que busca y devuelve una referencia al obj
        // del hilo que corre.
        System.out.printf("Thread '%s': START\n", Thread.currentThread().getName());

        while (current <= max) {
            if (isPrime(current)) {
                numPrimes++;
            }
            current++;
        }
        System.out.printf("Thread '%s': END. Number of Primes: %d\n", Thread.currentThread().getName(), numPrimes);
    }

    // Funcion de los primos
    private boolean isPrime(long number) {
        if (number <= 1) {
            return false;
        }
        for (long i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}