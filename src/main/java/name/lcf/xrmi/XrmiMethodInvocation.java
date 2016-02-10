package name.lcf.xrmi;

import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;

public class XrmiMethodInvocation implements Serializable {

    private XrmiMethod method;

    private Object[] arguments;

    public XrmiMethodInvocation(MethodInvocation invocation) {
        this.method = new XrmiMethod(invocation.getMethod());
        this.arguments = invocation.getArguments();
    }

    public Object[] getArguments() {
        return arguments;
    }

    public XrmiMethod getMethod() {
        return method;
    }
}
