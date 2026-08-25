## Esperar Finalizacion del Hilo
En algunos casos vamos a necesitar esperar la finalizacion de un hilo (que el metodo run() termine su ejecucion). Por ejemplo podemos tener un programa que empiece a inicializar los recursos que necesita antes de continuar con el resto de la ejecucion. Podemos ejecutar las tareas de inicializacion como hilos y esperar a que terminan antes de seguir con el programa.
Por eso se usa el metodo join() de la clase Thread. Cuando llamamos a este metodo usando el objeto thread, suspende la ejecucion del hilo que lo llama, hasta que el hilo termina su propia ejecucion.

## run() y main()
El funcionamiento es que, el hilo de la clase DataSourcesLoader es la primera en terminar su ejecucion debido a los 4 segundos de pausa, en cambio la otra clase del hilo 2 terminara despues, a los 6 segundos y el metodo join los detecta al terminar. este ultimo esta por defecto a una espera de 
