## Dormir y Retomar un Hilo
Aveces es necesario dormir y reanudar los hilos, por ejemplo uno que mide un sensor por un tiempo y luego el resto del tiempo esta apagado hasta que se lo necesite nuevamente. Vimos un ejemplo de esto en la parte 3, durmiendo el hilo main usando la funcion sleep() de la clase Thread, recibe un numero en milisegundos durante los cuales el hilo estara dormido.
Otra opcion es usar el metodo sleep() de un elemento de la enumeacion TimeUnit, Este metodo usa el metodo sleep() de la clase Thread para poner a dormir el hilo actual pero recibe el parametro en la unidad que representa y lo convierte a milisegundos.

## Funcion Run()
Toda esta prueba es para ver como se va creando un objeto Date, el cual se va imprimiendo en pantalla, a los 5 segundos (un total de 5 objetos creados) el hilo principal Main se pausara, deteniendo todo el proceso, y al instante en que despierta este se interrumpe. Por lo que solo se habran creado 5 objetos Date.
