package bll.exceptions;

public class NegativeQuantityException extends Exception{
    public NegativeQuantityException(String msg){
        super(msg);
    }
}
