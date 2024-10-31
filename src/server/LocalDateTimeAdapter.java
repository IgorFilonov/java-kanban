package server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(formatter));  // Преобразуем LocalDateTime в строку
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        String dateTimeStr = in.nextString();
        return LocalDateTime.parse(dateTimeStr, formatter);  // Преобразуем строку обратно в LocalDateTime
    }
}