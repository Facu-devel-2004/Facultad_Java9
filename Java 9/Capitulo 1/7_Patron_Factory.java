import java.util.concurrent.ThreadFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

//Implementacion de la interfaz ThreadFactory
class MyThreadFactory implements ThreadFactory {
  //Atributos: contador, nombre base y lista de estadisticas
  private int counter;
  private String name;
  private List<String> stats;

  //Constructor
  public MyThreadFactory(String name){
    counter = 0;
    this.name = name;
    stats = new ArrayList<String>();
  }

  //Sobrescribimos newThread() para personalizar la creacion de hilos
  @Override
  public Thread newThread(Runnable r) {
    //Creamos el hilo con un nombre personalizado
    Thread t = new Thread(r, name + "-Thread_" + counter);
    counter++;

    //Guardamos estadisticas de la creacion del hilo
    stats.add(String.format("Created thread %d with name %s on %s",
              t.getId(), t.getName(), new Date()));
    return t;
  }

  //Metodo para obtener las estadisticas recolectadas
  public String getStats(){
    StringBuffer buffer = new StringBuffer();
    Iterator<String> it = stats.iterator();
    while (it.hasNext()) {
      buffer.append(it.next());
      buffer.append("\n");
    }
    return buffer.toString();
  }
}

//Tarea que ejecutaran los hilos creados por la fabrica
class Task implements Runnable {
  @Override
  public void run() {
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

class Main {
  public static void main(String[] args){
    //Instanciamos nuestra fabrica
    MyThreadFactory factory = new MyThreadFactory("MyThreadFactory");

    //Instanciamos la tarea
    Task task = new Task();

    Thread thread;
    System.out.printf("Starting the Threads\n");

    //Usamos la fabrica para crear 10 hilos en vez de usar new Thread()
    for (int i = 0; i < 10; i++){
      thread = factory.newThread(task);
      thread.start();
    }

    //Imprimimos las estadisticas recolectadas por la fabrica
    System.out.printf("Factory stats:\n");
    System.out.printf("%s\n", factory.getStats());
  }
}
