class ConsoleClock implements Runnable {
  @Override
  public void run(){
    //En cada iteracion se crea un objeto Date
    for(int i = 0; i < 10; i++){
      System.out.printf("%s\n", new Date());
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
          System.out.printf("The FileClock has been interrupted");
        }
      }
    }
  }

class Main {
  public static void main(String[] args){
    ConsoleClock clock = new ConsoleClock();
    Thread thread = new Thread(clock);
    thread.start();

    //Duermo el hilo Main
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e){
      e.printStackTrace();
    };

    //Finalmente interurmpo el hilo de la clase ConsoleClock
    thread.interrupt();
  }
}
