package name.lcf.xrmi;

import java.io.Serializable;
import java.lang.reflect.Method;

public class XrmiMethod implements Serializable {

    private Class<?> declaringClass;

    private String name;

    private Class<?>[] parameterTypes;

    XrmiMethod(Method method) {
        this.declaringClass = method.getDeclaringClass();
        this.name = method.getName();
        this.parameterTypes = method.getParameterTypes();
    }

    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public String getName() {
        return name;
    }
}
