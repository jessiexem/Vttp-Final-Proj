package vttp.csf.Final.Project.exception;

public class HomeworkNerdException extends RuntimeException{
    
    public HomeworkNerdException(String exMessage, Exception ex) {
        super (exMessage, ex);
    }

    public HomeworkNerdException(String exMessage) {
        super (exMessage);
    }
}
