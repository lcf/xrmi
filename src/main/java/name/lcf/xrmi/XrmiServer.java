package name.lcf.xrmi;

import com.google.inject.Injector;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class XrmiServer {
    /**
     * Launches XRMI server accepting connections on the port specified
     * Uses a cached thread pool for processing requests
     */
    public static void start(Injector injector, final int port) {
        start(injector, port, Executors.newCachedThreadPool());
    }

    /**
     * Launches XRMI server accepting connections on the port specified
     */
    public static void start(Injector injector, final int port, ExecutorService executor) {
        try (ServerSocket socket = new ServerSocket(port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket clientSocket = socket.accept();
                executor.execute(() -> processRequest(injector, clientSocket));
            }
        } catch (Exception e) {
            // todo: proper exception handling
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void processRequest(Injector injector, Socket clientSocket) {
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            XrmiMethodInvocation invocation = (XrmiMethodInvocation) in.readObject();
            Class<?> clazz = invocation.getMethod().getDeclaringClass();
            Method method = clazz.getMethod(
                    invocation.getMethod().getName(),
                    invocation.getMethod().getParameterTypes()
            );
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            try {
                Object result = method.invoke(injector.getInstance(clazz), invocation.getArguments());
                out.writeObject(new XrmiInvocationResult(ReturnType.Normal, result));
            } catch (InvocationTargetException exception) {
                out.writeObject(new XrmiInvocationResult(ReturnType.Exceptional, exception.getTargetException()));
            }
        } catch (Exception e) {
            // todo: proper exception handling
            System.out.println(e.getMessage());
        }
    }
}
