//Creacion de hilo
//EL metodo isAlive() dara true si el hilo ocupa algun proceso del CPU, y dara false si ocurre un error o si termina antes su ejecucion.
//El metodo isInterrupted() dara true cuando se activa la bandera de el hilo por el metodo interrupt() y el hilo sigue activo.
//Y dara false si nunca se interrumpio, si el hilo tiene un estado TERMINATED antes de verificarlo y si se limpia la bandera solo.
class PrimeGenerator extends Thread {
    @Override
    public void run() {
        // Calculo de numeros primos
        long number = 1L;
        while (true) {
            if (isPrime(number)) {
                System.out.printf("El numero %d es primo \n", number);
            }

            // Funcion nativa, verifica si el hilo se interrumpe
            if (isInterrupted()) {
                System.out.printf("The Prime Generator has been Interrupted");
                return;
            }
            number++;
        }
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

class Main {
    public static void main(String[] args) {
        Thread task = new PrimeGenerator();
        task.start();

        // Luego de 5 segundos interrumpe el hilo
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // Metodo que imprime el error completo
            e.printStackTrace();
        }
        task.interrupt();

        // Esta salida dependera de si el hilo finaliza o no
        System.out.printf("Main: Estado del hilo: %s\n", task.getState());
        System.out.printf("Main: isInterrupted: %s\n", task.isInterrupted());
        // isAlive(), funcion qe verifica si el hilo esta activo
        System.out.printf("Main: isAlive: %s\n", task.isAlive());
    }
}