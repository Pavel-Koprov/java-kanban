package http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {

        if (localDateTime == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String dateString = jsonReader.nextString();

        if (dateString.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
