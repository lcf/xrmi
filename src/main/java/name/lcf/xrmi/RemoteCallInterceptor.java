package name.lcf.xrmi;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RemoteCallInterceptor implements MethodInterceptor {

    private InetSocketAddress socketAddress;

    /**
     * @param socketAddress Intercepted method calls will be directed to this remote instance
     */
    public RemoteCallInterceptor(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Socket clientSocket = new Socket(socketAddress.getHostString(), socketAddress.getPort());

        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        outputStream.writeObject(new XrmiMethodInvocation(methodInvocation));

        XrmiInvocationResult xrmiInvocationResult = (XrmiInvocationResult) new ObjectInputStream(clientSocket.getInputStream()).readObject();
        clientSocket.close();
        if (xrmiInvocationResult.isExceptional()) {
            throw (Throwable) xrmiInvocationResult.getResult();
        } else {
            return xrmiInvocationResult.getResult();
        }
    }
}
