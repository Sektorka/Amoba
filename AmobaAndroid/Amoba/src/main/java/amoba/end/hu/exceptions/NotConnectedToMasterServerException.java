package amoba.end.hu.exceptions;

public class NotConnectedToMasterServerException extends IllegalStateException{
    public NotConnectedToMasterServerException() {
    }

    public NotConnectedToMasterServerException(String detailMessage) {
        super(detailMessage);
    }

    public NotConnectedToMasterServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotConnectedToMasterServerException(Throwable cause) {
        super(cause);
    }
}
