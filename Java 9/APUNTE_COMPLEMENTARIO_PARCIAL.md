# Apunte Complementario: Faltantes y Repaso Oficial del Libro (Java 9 Concurrency Cookbook)

¡Éxitos en el parcial de mañana! Este apunte contiene **exactamente lo que te falta del Capítulo 2** y las **explicaciones basadas en el libro para los temas del Capítulo 4** que toca tu resumen. Está diseñado para una lectura rápida y al grano.

---

## PARTE 1: Lo que te falta del Capítulo 2 (Sincronización Básica)

Tu resumen cubría `synchronized` y `ReentrantLock`, pero le faltaba cómo hacer que los hilos **se comuniquen y esperen** ciertas condiciones de forma eficiente.

### 1. Condiciones en código sincronizado (`wait`, `notify`, `notifyAll`)
En lugar de hacer que un hilo gire infinitamente en un ciclo `while` gastando CPU porque una condición aún no se cumple, Java permite suspenderlo y despertarlo.

*   **`wait()`:** Se llama **dentro de un bloque `synchronized`**. El hilo libera el cerrojo inmediatamente y se va a dormir hasta que alguien lo despierte.
*   **`notifyAll()` / `notify()`:** Despierta a los hilos que están dormidos esperando en ese objeto.
*   **Regla de oro del libro:** Siempre llama a `wait()` dentro de un ciclo `while` que verifique la condición, ¡nunca en un `if`! (Para evitar despertares espurios).

```java
public synchronized void consumir() {
    while (buffer.isEmpty()) { // SIEMPRE en un while
        wait(); // Libera el lock y duerme. Al despertar, vuelve a evaluar el while.
    }
    // ... consumir dato ...
    notifyAll(); // Avisa a los productores que hay espacio
}
```

### 2. Múltiples condiciones en un Lock (`Condition`)
Es la versión mejorada y moderna de `wait`/`notify`. Un `ReentrantLock` puede tener **múltiples condiciones** asociadas, lo que permite despertar *solamente* a los hilos que te interesan (ej. despertar solo a productores o solo a consumidores), mejorando el rendimiento.

*   Se crea con: `Condition myCondition = lock.newCondition();`
*   **`await()`:** Equivale a `wait()`. Libera el lock y duerme al hilo en esa condición específica.
*   **`signal()` / `signalAll()`:** Equivale a `notify()`. Despierta a los hilos esperando en *esa* condición.

```java
Lock lock = new ReentrantLock();
Condition bufferLleno = lock.newCondition();
Condition bufferVacio = lock.newCondition();

public void producir() {
    lock.lock();
    try {
        while (buffer.isFull()) {
            bufferLleno.await(); // El productor se duerme
        }
        // ... producir ...
        bufferVacio.signalAll(); // Despierta SOLO a los consumidores
    } finally {
        lock.unlock();
    }
}
```

### 3. Bloqueos Avanzados: `StampedLock` (Introducido en Java 8)
El libro menciona este candado especial que es más rápido que el `ReadWriteLock`. 
Tiene 3 modos:
1.  **Escritura (Write):** Bloqueo exclusivo tradicional.
2.  **Lectura (Read):** Bloqueo compartido tradicional.
3.  **Lectura Optimista (Optimistic Read):** ¡No bloquea! Devuelve un "sello" (`stamp`, un número `long`). El hilo lee los datos sin bloquear a nadie, y luego usa el método `lock.validate(stamp)` para preguntar: *"¿Alguien modificó los datos mientras yo leía?"*. Si la respuesta es sí, entonces se reintenta adquiriendo un bloqueo de lectura real. Es súper eficiente cuando hay muchísimas lecturas y muy pocas escrituras.

---

## PARTE 2: Explicaciones Oficiales del Libro para el Capítulo 4 (Thread Executors)

Tu resumen toca este tema, pero aquí tienes los conceptos tal cual los enfoca el *Cookbook* para asegurar que respondas la teoría exacta.

### 1. El framework `Executor` y la separación de conceptos
El libro hace mucho énfasis en esto: el framework separa la **creación de la tarea** (implementar `Runnable` o `Callable`) de la **ejecución de la tarea**. Ya no haces `new Thread(tarea).start()`, sino que delegas la tarea a un `ExecutorService`, y él decide qué hilo la corre, cuándo y cómo. Mejora el rendimiento porque **reutiliza hilos** en lugar de crear y destruir uno por cada tarea (lo cual es carísimo para el SO).

### 2. Callable y Future (Resultados de tareas)
A diferencia de `Runnable` (cuyo método `run()` es `void`), el libro explica que `Callable<T>` tiene un método `call()` que **sí retorna un valor** o lanza una excepción.
Cuando envías un `Callable` al ejecutor con `submit()`, este te devuelve un objeto `Future<T>`.
*   **`future.get()`:** Bloquea al hilo que lo llama hasta que la tarea termine y devuelve el resultado.
*   **`future.isDone()` / `future.isCancelled()`:** Para verificar el estado sin bloquearse.

### 3. Tipos de Thread Pools (Según la clase `Executors`)
El libro clasifica los pools prefabricados que más se usan:
*   **FixedThreadPool:** Número fijo de hilos. Si llegan más tareas, se encolan. Ideal para servidores estables.
*   **CachedThreadPool:** Crea hilos nuevos si no hay inactivos, y destruye los que lleven 60s sin uso. Ideal para ráfagas de muchas tareas cortas.
*   **SingleThreadExecutor:** Un solo hilo. Garantiza ejecución secuencial (una tras otra).
*   **ScheduledThreadPool:** Sirve para tareas diferidas (ej. "ejecutar en 5 segundos") o repetitivas (ej. "ejecutar cada 1 minuto").

### 4. Apagado (Shutdown)
El libro advierte fuertemente: **un programa Java no termina si el Executor sigue vivo**.
*   **`shutdown()`:** El ejecutor deja de aceptar tareas nuevas, pero termina ordenadamente de ejecutar las que ya estaban en cola.
*   **`shutdownNow()`:** Intenta cancelar forzosamente (`interrupt`) las tareas en ejecución y devuelve una lista de las tareas que no llegaron a ejecutarse.

### 5. Rechazo de Tareas (RejectedExecutionHandler)
Cuando usas un FixedThreadPool con una cola de tamaño limitado y la cola se llena, o cuando envías una tarea después de haber llamado a `shutdown()`, el ejecutor la rechaza. El libro enseña a implementar la interfaz `RejectedExecutionHandler` para decidir qué hacer con esa tarea (ej. guardarla en un log, descartarla en silencio, o lanzar un error).
