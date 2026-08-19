
//Contolador de hilo con excepcion
import java.io.File;
import java.util.concurrent.TimeUnit;

class FileSeach implements Runnable {
    // Atributos para el buscador de archivos
    private String initPath;
    private String fileName;

    // Constructor
    public FileSeach(String initPath, String fileName) {
        this.initPath = initPath;
        this.fileName = fileName;
    }

    // Metodo run()
    @Override
    public void run() {
        File file = new File(initPath);
        // Verifico que file sea un directorio
        if (file.isDirectory()) {
            try {
                directoryProcess(file);
            } catch (InterruptedException e) {
                System.out.printf("%s: La busqueda se ha interrumpido", Thread.currentThread().getName());
            }
        }
    }

    // [
    // 1. Firma con "throws InterruptedException"
    private void directoryProcess(File file) throws InterruptedException {
        File[] list = file.listFiles();
        if (list != null) {
            for (File content : list) {
                if (content.isDirectory()) {
                    directoryProcess(content);
                } else {
                    fileProcess(content);
                }
            }
        }
        // 2. Si se interrumpió el hilo, lanzamos la excepción
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

    // Funcion buscar archivo
    private void fileProcess(File file) throws InterruptedException {
        if (file.getName().equals(fileName)) {
            System.out.printf("%s : %s\n", Thread.currentThread().getName(), file.getAbsolutePath());
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }
    // ]
}

class Main {
    public static void main(String[] args) {
        // Inicializo la clase FileSeach y un hilo.
        FileSeach buscador = new FileSeach("Ejemplo 1", "Ejemplo 2");
        Thread thread = new Thread(buscador);
        thread.start();

        // Se esperara 10 segundos para interrumpir el hilo
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // En este caso la interrupcion es lanzada por la interrupcion del hilo
        thread.interrupt();
    }
}