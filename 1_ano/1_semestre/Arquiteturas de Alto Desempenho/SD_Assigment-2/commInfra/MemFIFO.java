package commInfra;
//This class extends the MemObject class and implements the read and write methods
public class MemFIFO<T> extends MemObject<T> {
    private int empty_index, occupied_index;
    private int size;
    private int numElements;

    public MemFIFO(int size) throws MemException{
        super(size);
        empty_index = 0;
        occupied_index = 0;
        this.size = size;
        this.numElements = 0;
    }

    @Override
    public boolean isEmpty() {
        return empty_index == occupied_index;
    }

    public boolean isFull() {
        return empty_index % size == size;
    }

    public int getNumElem() {
        return this.numElements;
    }

    @Override
    public T pop() {
        if (empty_index == occupied_index) {
            //error
            System.out.println("Error: Buffer is empty");
            return null;
        } else {
            T data = buffer[occupied_index%size];
            occupied_index++;
            numElements--;
            return data;
        }
    }

    @Override
    public void push(T value) throws MemException{
        if (empty_index % size == size) {
            //error
            System.out.println("Error: Buffer is full");
            throw new MemException("Buffer is full");
        } else {
            buffer[empty_index % size] = value;
            // System.out.println("Writing: " + value + " at index: " + empty_index);
            empty_index++;
            numElements++;
        }
    }

}
