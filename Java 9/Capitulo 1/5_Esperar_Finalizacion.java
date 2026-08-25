class DataSourcesLoader implements Runnable {
  @Override
  public void run(){
    //Inicio de la ejecucion
    System.out.printf("Beginning data sources loading: %s\n", new Date());

    //Suspendo 4 segundos
    try {
      TimeUnit.SECONDS.sleep(4);
    } catch (InterruptedException e){
      e.printStackTrace();
    }

    //Retomo ejecucion 
    System.out.printf("Data Sources loading has finished: %s\n", new Date());
  }
}

//Dentro de main se creara una clase NetworkConnectionsLoader que implementara su metodo run pero esta durara 6 segundos.
class Main {
  public static void main(String[] args){
    DataSourcesLoader dsLoader = new DataSourcesLoader();
    Thread thread1 = new Thread(dsLoader, "DataSourcesThread");

    //Objeto de la clase NetworkConnectionsLoader
    NetworkConnectionsLoader ncLoader = new NetworkConnectionsLoader();
    Thread thread2 = new Thread(ncLoader, "NetworkConnectionsThread");

    //Iniciamos los hilos
    thread1.start();
    thread2.start();

    //Esperamos a que finalicen
    try {
      thread1.join();
      thread2.join():
    } catch (InterruptedException e){
      e.printStackTrace();
    }

    //Mensaje de finalizacion del programa
    System.out.println("End");
  }
}
