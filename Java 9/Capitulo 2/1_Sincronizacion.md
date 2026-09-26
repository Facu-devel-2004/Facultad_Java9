## Definicion
El metodo "synchronized()" usado en los hilos, sirve para controlar el acceso concurrente a un metodo o bloque de codigo, siempre usa una referencia a un objeto. Solo un hilo puede ejecutar un metodo o bloque de codigo protegido por la misma referencia a un objeto.

Cuando uso la palabra "synchronized" en uno o mas metodos de un objeto, solo un hilo de ejecucion tendra acceso a todos esos metodos. Si otro hilo intenta acceder a cualquier metodo declarado con la pablabra clave del mismo objeto, este sera suspendido hasta que el primer hilo termina la ejecucion del metodo.
En otras palabras, cada metodo declarado con la palabra "synchronized" es una seccion critica, y Java solo permite la ejecucion de una de las secciones criticas de un objeto a la vez.

Solo un hilo de ejecucion tendra acceso a uno de los metodos estaticos declarados con la palabra clave "synchronized", pero un hilo diferente puede acceder a otros metodos no estaticos de un objeto de esa clase.
!ADVERTENCIA¡, Si dos hilos pueden acceder a dos metodos synchronized si un metodo es estatico y el otro no, pero si ambos cambian los mismos datos pueden haber errores en la inconsistencia de datos.

## Otro uso
Cuando uso la palabra "synchronized" para proteger un bloque de codigo, tiene que pasar una referencia a un objeto como parametro. Normalmente se usaria la palabra "this" para referirme al objeto que ejecuta el metodo, pero tambien se pueden usar otras referencias.
Debo mantener privados los objetos usados para la sincronizacion. Por ejemplo, si tengo 2 atributos independientes en una clase compartida por multiples hilos, debo sincronizar el acceso a cada variable, sin embargo, no habria problema si un hilo accede a un atributo y otro hilo accede al 2do al mismo tiempo, tener en cuenta que si accedo a ambos con la referencia "this" puedo interferir en otro codigo sincronizado.

