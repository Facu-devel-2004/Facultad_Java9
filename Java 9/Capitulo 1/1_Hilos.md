# Que es un Hilo
Las tareas Concurrentes (tareas ejecutadas al mismo tiempo) se ejecutan dentro de un proceso llamado hilo. Concurrencia es cuando se ejecuta una aplicacion con multiples hilos en un procesador de un solo nucleo. Se habla de paralelismo cuando se ejecuta una aplicacion con multiples hilos en un procesador multinucleo o en una computadora con mas de un procesador.

## Creacion de un Hilo
Existen dos metodos, usaremos el segundo, se implementa la interfaz Runnable y el metodo run(), luego crear un objeto de la clase Thread pasandole el objeto Runnable como parametro. Este metodo ofrece mayor flexibilidad. La clase Thread guarda ciertos atributos informativos que nos permiten identificar un hilo, conocer su estado o controlar su prioridad.

- **ID:** 
Este atributo almacena un identificador unico para cada hilo.

- **Nombre:** 
Atributo que almacena el nombre del hilo.

- **Prioridad:** 
Almacena la prioridad de los Thread, la prioridad 1 es la mas baja y la 10 es la mas alta. No se recomienda cambiar la prioridad de los hlos.

- **Estado:**
Los hilos pueden tener 8 estados distintos, Thread.state, los cuales pueden ser NEW (El hilo se creo pero aun no comenzo), RUNNABLE (El hilo se esta ejecutando en la Java Virtual Machine), BLOCKED (Hilo que intento acceder a un bloque de codigo o metodo marcado como synchronized pero el candado ya esta siendo usado por otro hilo), WAITING (El hilo esta en una sala de espera de forma indefinida), TIMED_WAITING (Es una pausa calculada, se detiene por un tiempo maximo especifico) TERMINATED (El hilo termino su ciclo de vida. Su metodo run() llego a la ultima linea de codigo o lanzo una excepcion). Otros aparte son READY TO RUN (El hilo esta a la espera de un nucleo de procesador), SCHEDULER (Es un modulo que decide constantemente que hilo entra en RUNNING sacandolos de la cola READY TO RUN).

## Interfaz Runnable
Separa la definicion de la tarea que se va a ejecutar del mecanismo que la ejecuta (el hilo). Un objeto Runnable representa simplemente "un trabajo a realizar", permitiendo un diseño mas limpio y flexible.

### start() vs run()
El metodo start() crea y pone en marcha un nuevo hilo de ejecucion independiente en el sistema operativo, en cambio run() solo contiene el codigo que se va a ejecutar, si lo ejecuto directamente este corre el codigo de manera secuencial en el hilo actual sin crear uno nuevo.