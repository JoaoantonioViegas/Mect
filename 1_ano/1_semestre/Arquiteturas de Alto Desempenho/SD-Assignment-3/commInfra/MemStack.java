package commInfra;
//this is a class that implements 
public class MemStack<T> extends MemObject<T> {
    private int empty_index;
    private int size;

    public MemStack(int size) throws MemException{
        super(size);
        empty_index = 0;
        this.size = size;
    }

    @Override
    public boolean isEmpty() {
        return empty_index == 0;
    }

    @Override
    public T pop() {
        if (empty_index == 0) {
            //error
            System.out.println("Error: Buffer is empty");
            return null;
        } else {
            T data = buffer[empty_index-1];
            empty_index--;
            return data;
        }
    }

    @Override
    public void push(T value) {
        if (empty_index % size == size) {
            //error
            System.out.println("Error: Buffer is full");
        } else {
            buffer[empty_index % size] = value;
            // System.out.println("Writing: " + value + " at index: " + empty_index);
            empty_index++;
        }
    }
}
