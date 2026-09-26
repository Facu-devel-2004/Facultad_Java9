import java.util.concurrent.TimeUnit;
// =====================================================================
// CLASE ParkingCash - La caja registradora del estacionamiento
// =====================================================================
// Administra el dinero recaudado. Cada vehiculo paga $2 al salir.
class ParkingCash {
    private static final int cost = 2; // Costo fijo por vehiculo
    private long cash = 0;             // Dinero total recaudado

    // "synchronized" en el metodo completo: solo un hilo a la vez puede
    // ejecutar este metodo. Si dos sensores detectan salidas al mismo
    // tiempo, uno espera a que el otro termine de sumar.
    // Sin esto, dos hilos podrian leer el mismo valor de "cash" y al
    // escribir, uno sobreescribiria la suma del otro (race condition).
    public synchronized void vehiclePay() {
        cash += cost;
    }

    // En close() no se sincroniza todo el metodo, sino solo el bloque
    // critico: la lectura y reseteo de "cash". La impresion en consola
    // no necesita proteccion porque no accede a datos compartidos.
    public void close() {
        System.out.printf("Closing accounting\n");
        long totalAmmount;
        // Bloque synchronized(this): usa la misma referencia que
        // vehiclePay() (que es "this" implicitamente), asegurando que
        // nadie sume dinero mientras leemos y reseteamos.
        synchronized (this) {
            totalAmmount = cash;
            cash = 0;
        }
        System.out.printf("The total amount is : %d\n", totalAmmount);
    }
}

// =====================================================================
// CLASE ParkingStats - Estadisticas del estacionamiento
// =====================================================================
// Lleva la cuenta de cuantos autos y motos hay en el estacionamiento.
class ParkingStats {
    private long numberCars;
    private long numberMotorcycles;
    private ParkingCash cash;

    // Se crean DOS objetos candado separados, uno para autos y otro
    // para motos. Esto es clave: como son datos independientes, si
    // usara un solo candado (ej: "this"), un hilo que modifica motos
    // bloquearia a otro que quiere modificar autos innecesariamente.
    // Con candados separados, pueden operar EN PARALELO.
    private final Object controlCars;
    private final Object controlMotorcycles;

    public ParkingStats(ParkingCash cash) {
        numberCars = 0;
        numberMotorcycles = 0;
        controlCars = new Object();
        controlMotorcycles = new Object();
        this.cash = cash;
    }

    // --- Metodos para AUTOS (protegidos por controlCars) ---

    public void carComeIn() {
        // Solo un hilo a la vez puede incrementar numberCars
        synchronized (controlCars) {
            numberCars++;
        }
    }

    public void carGoOut() {
        // Solo un hilo a la vez puede decrementar numberCars
        synchronized (controlCars) {
            numberCars--;
        }
        // vehiclePay() tiene su propia sincronizacion, no necesita
        // estar dentro del bloque synchronized de controlCars
        cash.vehiclePay();
    }

    // --- Metodos para MOTOS (protegidos por controlMotorcycles) ---
    // Un hilo puede modificar motos MIENTRAS otro modifica autos,
    // porque usan candados diferentes.

    public void motoComeIn() {
        synchronized (controlMotorcycles) {
            numberMotorcycles++;
        }
    }

    public void motoGoOut() {
        synchronized (controlMotorcycles) {
            numberMotorcycles--;
        }
        cash.vehiclePay();
    }

    // Los getters tambien deben sincronizarse con su candado
    // correspondiente para leer un valor consistente.
    public long getNumberCars() {
        synchronized (controlCars) {
            return numberCars;
        }
    }

    public long getNumberMotorcycles() {
        synchronized (controlMotorcycles) {
            return numberMotorcycles;
        }
    }
}

// =====================================================================
// CLASE Sensor - Simula el trafico de vehiculos (es el hilo/tarea)
// =====================================================================
// Implementa Runnable: su metodo run() define lo que hace cada hilo.
// Cada sensor simula 10 ciclos de: entran 2 autos + 1 moto, salen todos.
class Sensor implements Runnable {
    private ParkingStats stats;

    public Sensor(ParkingStats stats) {
        this.stats = stats;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            // Entran 2 autos
            stats.carComeIn();
            stats.carComeIn();
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Entra 1 moto
            stats.motoComeIn();
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Salen los 3 vehiculos (1 moto + 2 autos)
            // Cada salida cobra $2 via vehiclePay()
            stats.motoGoOut();
            stats.carGoOut();
            stats.carGoOut();
        }
    }
}

// =====================================================================
// CLASE Main - Punto de entrada
// =====================================================================
public class Main {
    public static void main(String[] args) {
        ParkingCash cash = new ParkingCash();
        ParkingStats stats = new ParkingStats(cash);

        System.out.printf("Parking Simulator\n");

        // Se crean el doble de sensores que nucleos disponibles.
        // En un procesador de 4 nucleos = 8 sensores (hilos).
        int numberSensors = 2 * Runtime.getRuntime().availableProcessors();
        Thread threads[] = new Thread[numberSensors];

        // Se crean e inician todos los hilos
        for (int i = 0; i < numberSensors; i++) {
            Sensor sensor = new Sensor(stats);
            Thread thread = new Thread(sensor);
            thread.start();
            threads[i] = thread;
        }

        // Se espera a que TODOS los hilos terminen con join()
        for (int i = 0; i < numberSensors; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Resultado esperado (con 8 sensores):
        // - Autos: 0 (todos salieron)
        // - Motos: 0 (todas salieron)
        // - Total: 8 sensores * 10 iteraciones * 3 vehiculos * $2 = $480
        System.out.printf("Number of cars: %d\n", stats.getNumberCars());
        System.out.printf("Number of motorcycles: %d\n", stats.getNumberMotorcycles());
        cash.close();
    }
}
