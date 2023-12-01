package backend.Exceptions;

public class ThisTeacherExists extends Exception {
    public ThisTeacherExists(String msg){
        super(msg);
    }
}