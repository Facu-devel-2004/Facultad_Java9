## Hilo Daemon
Java tiene un hilo especial llamado "Hilo Daemon". Cuando los hilos Daemon son los unicos hilos ejecutandose en un programa, la JVM termina el programa despues de que estos hilos terminen. Con esta caracteristica estos hilos son usados normalmente como proveedores de servicios para hilos normales que se ejecutan en el mismo programa, usualmente tienen un bucle infinito que espera la solicitud de servicio o realiza tareas de un hilo.
Explicado de mejor manera podemos decir que, Java mantiene el codigo corriendo simpre y cuando hayan "hilos normales" activos, si estos terminan y aun hay hilos daemon activos o ejecutando alguna tarea, a java no le importa y los termina igual, por eso estos ejecutan tareas secundarias.

## Ejemplo de codigo no escrito
En el ejemplo de codigo (que no voy a poner) se crean 2 hilos, el primero implementando Runnable que tiene la tarea de Escribir y el hilo Daemon se lo crea heredando de la clase Thread y dentro del constructor de la tarea se coloca "setDaemon(true)" para indicarle al programa que es un Hilo Daemon.

