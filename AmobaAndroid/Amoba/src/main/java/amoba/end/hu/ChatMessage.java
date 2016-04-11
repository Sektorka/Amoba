package amoba.end.hu;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage {
    protected String message, name;
    protected Date date;
    protected SimpleDateFormat dateFormat;

    public final String DEFAULT_DATE_FORMAT_PATTERN = "HH:mm:ss";

    public ChatMessage(String message, String name) {
        this.message = message;
        this.name = name;
        this.date = new Date();
        this.dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public void setDateFormat(String dateFormatPattern){
        dateFormat = new SimpleDateFormat(dateFormatPattern);
    }

    public String getFormattedDate(){
        return dateFormat.format(date);
    }

    @Override
    public String toString() {
        return getFormattedDate() + " | " + getName() + ": " + getMessage();
    }
}
