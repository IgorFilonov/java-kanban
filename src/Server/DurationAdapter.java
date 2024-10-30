package Server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toMillis());  // Сериализация Duration в миллисекунды
        }
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        long durationMillis = in.nextLong();  // Чтение миллисекунд
        return Duration.ofMillis(durationMillis);  // Десериализация обратно в Duration
    }
}