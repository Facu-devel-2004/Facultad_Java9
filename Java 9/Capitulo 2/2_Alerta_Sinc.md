## Problema y Metodos que lo Solucionan
Como un buffer es una estructura de datos compartida, tenemos que controlar el acceso a él usando un mecanismo de sincronización ("synchronized") pero aqui tenemos mas limitaciones. Un productor no puede guardar datos en el buffer si esta lleno, y un consumidor no puede sacar si esta vacio.
Tenemos metodos que nos ayudan a solucionar el problema wait(), notify() y notofyAll() implementados en la clase Object. Un hilo puede llamar al metodo "wait()" dentro de un bloque de codigo sincronizado, si se llama fuera del bloque se lanza una excepcion "IllegalMonitorStateException".

Cuando el hilo llama al metodo wait(), Java pone el hilo a dormir y libera el objeto que controla el bloque de codigo sincronizado que esta ejecutando, permitiendo que otros hilos ejecuten otros bloques de codigo sincronizado protegidos por este objeto. Para volver a despertar el hilo debo llamar a los metodos notify() o notifyAll() dentro de un bloque de codigo protegido por el mismo objeto.

## Ejemplo Simple - Patron Productor-Consumidor
La condicion de espera (lista vacia o llena) debe ir siempre en un "while" y NO en un "if". Esto es porque cuando el hilo se despierta con notify(), no tiene garantia de que la condicion haya cambiado: otro hilo pudo haberse despertado antes y modificado la lista, o puede ocurrir un "spurious wakeup" (la JVM despierta el hilo sin que nadie llame a notify()). Con "while", al despertarse vuelve a verificar la condicion. Con "if" asumiria que ya esta todo bien y podria fallar.

```java
// PRODUCTOR - agrega datos al buffer
synchronized (buffer) {
    while (buffer.size() == maxSize) { // mientras este lleno, esperar
        buffer.wait();
    }
    buffer.add(dato);
    buffer.notify(); // despertar al consumidor
}

// CONSUMIDOR - saca datos del buffer
synchronized (buffer) {
    while (buffer.isEmpty()) { // mientras este vacio, esperar
        buffer.wait();
    }
    dato = buffer.remove(0);
    buffer.notify(); // despertar al productor
}
```
