package name.lcf.xrmi;

import java.io.Serializable;

public class XrmiInvocationResult implements Serializable {

    private ReturnType returnType;

    private Object result;

    public XrmiInvocationResult(ReturnType returnType, Object result) {
        this.returnType = returnType;
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public boolean isExceptional() {
        return returnType == ReturnType.Exceptional;
    }
}
