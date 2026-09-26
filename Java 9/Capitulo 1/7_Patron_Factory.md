## Definicion
Es un patron creacional y su objetivo es desarrollar un objeto cuya mision sea crear otros objetos de una o varias clases. Con esto si quiero crear un objeto de una de esas clases, simplemente uso un operador Factory en vez de un new. En otras palabras podemos decir que encapsula la logica de creacion de objetos.
Facilita la creacion y limitacion de objetos a crear (si tenemos la limitante de recursos en el sistema). Podriamos crear na fabrica de hilos (Thread Factory).

## Ejemplo Codigo
Si implementamos la interfaz Thread Factory, esta nos permite centralizar la creacion de hilos y personalizar sus características (como asignarles nombres, guardar estadisticas de su creacion (lista de arreglo, registra su informacion y hora de creacion), limitar su prioridad, etc) en lugar de instanciarla de forma dispersa en el codigo usando "new Thread()".
Dentro de la implementacion, se sobrescribe la funcion "newThread()" para recibir la tarea Runnable, y dentro modificamos su constructor para asignarle la tarea, cambiar el nombre y que cambie su contador (o IP).

### Task
Fuera de la implementacion del ThreadFactory, implementamos la funcion Task con Runnable, ahi sobrescribimos el metodo run() y le asignamos la tarea al hilo que creamos dentro de la Factory. Podrian ser mas tareas e implementar mas Runnables.
En el main instanciamos Factory y Task.

"thread = factory.newThread(task)"
