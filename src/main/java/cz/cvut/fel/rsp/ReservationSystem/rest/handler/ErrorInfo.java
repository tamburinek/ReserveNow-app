package cz.cvut.fel.rsp.ReservationSystem.rest.handler;

public class ErrorInfo {
    private String message;

    private String requestUri;

    public ErrorInfo() {
    }

    public ErrorInfo(String message, String requestUri) {
        this.message = message;
        this.requestUri = requestUri;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" + requestUri + ", message = " + message + "}";
    }
}
