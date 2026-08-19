# Controlar la Interrupcion de un Hilo
En el tema anterior se vio como se interrumpe y controla un hilo como un objeto. Pero si se implementa un algoritmo complejo dividido en varios metodos o tiene metodos con llamadas recursivas, necesitaremos un mecanismo mejor para controlar la interrupcion de un hilo. Java proporciona "InterruptedException" para este proposito. Se puede lanzar esta excepcion cuando se detecte la interrupcion de un hilo y se capture en el metodo run().

El ejemplo que se mostrara sera que, se buscara un archivo con un determinado nombre en una carpeta y sus subcarpetas. Esto mostrara como funciona la excepcion InterruptedException para controlar la interrupcion de un hilo.

## directoryProcess(File file)
Toma la ruta inicial "File = initPath" y busca, si encuentra un archivo normal lo manda el metodo fileProcess(), si encuentra una carpeta se llama a si mismo y revisa que hay dentro. En caso de no encontrar nada termina esa ejecucion y vuelve hacia atras.

## fileProcess(File file)
Recibe el archivo encontrado por directoryProcess() y compara los nombres, si hay coincidencia imprime el nombre del hilo y su direccion completa. En ambos casos si nunca encuentra nada, simplemete termina su ejecucion y el hilo muere. Y en ambos casos, en cada "accion" por buscar el archivo, el hilo pregunta si se interrumpio su tarea (InterruptedExcption), si es false continua buscando, caso contrario se detiene y lanza su mensaje.
Si hubiera usado un return en vez de una excepcion, solo lo hubiera sacado de la carpeta actual en vez de terminar todo el proceso.  

## TimeUnit.SECONDS.sleep(10);
Esto en teoria manda a dormir el hilo MAIN, el hilo que esta ejecutando tal accion en este mismo momento, lo que se traduce a que podria poner en pausa a una app entera por 10 segundos hasta poder continuar. Para este caso el main se pausa, y el buscador sigue buscando en segundo plano, luego de 10 segundos ejecuta el thread.interrupt() y si ese hilo sigue buscando se interrumpe y manda la excepcion, caso contrario deberia mandar el path del archivo.