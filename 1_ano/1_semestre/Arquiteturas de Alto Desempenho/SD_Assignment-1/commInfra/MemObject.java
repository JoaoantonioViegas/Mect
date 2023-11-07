package commInfra;

//MemObject abstract class
//This class is the base class 
//It has a constructor to initialize the buffer
//It has 2 abstract methods to read and write to the buffer
public abstract class MemObject<T> {

    // The buffer is a generic type array
    public T[] buffer;

    @SuppressWarnings("unchecked")
    public MemObject(int size) throws MemException {
        buffer = (T[]) new Object[size];
    }

    public abstract void push(T value) throws MemException;

    public abstract T pop() throws MemException;

    public abstract boolean isEmpty() throws MemException;
}
