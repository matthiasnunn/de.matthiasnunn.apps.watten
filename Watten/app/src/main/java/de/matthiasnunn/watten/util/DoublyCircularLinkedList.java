package de.matthiasnunn.watten.util;


public interface DoublyCircularLinkedList<T>
{
    public T getPrevious();
    public void setPrevious( T previous );

    public T getNext();
    public void setNext( T next );
}