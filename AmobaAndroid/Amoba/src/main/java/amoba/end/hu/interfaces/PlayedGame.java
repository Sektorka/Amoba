package amoba.end.hu.interfaces;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlayedGame {
    public final Header header;
    public final List<Step> body;

    public PlayedGame(Header header, List<Step> body){
        this.header = header;
        this.body = body;
    }

    public static class Header{
        public final Date timestamp;
        public final int width, height;

        public Header(Date timestamp, int width, int height){
            this.timestamp = timestamp;
            this.width = width;
            this.height = height;
        }

        public String formattedTimestamp(String format){
            return new SimpleDateFormat(format).format(timestamp);
        }
    }

    public static class Step{
        public final char signal;
        public final int x;
        public final int y;

        public Step(char signal, int x, int y){
            this.signal = signal;
            this.x = x;
            this.y = y;
        }
    }
}
