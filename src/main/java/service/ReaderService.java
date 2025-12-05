package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Reader;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReaderService {
    private final Map<String, Reader> readers = new HashMap<>();
    private final Gson gson;
    public ReaderService() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }
    public void registerReader(Reader reader) {
        if (readers.containsKey(reader.getId())) {
            throw new IllegalArgumentException("Reader with this ID already exists");
        }
        readers.put(reader.getId(), reader);
    }
    public String getAllReadersJson() {
        return gson.toJson(readers.values());
    }
    public Map<String, Reader> getReaders() {
        return readers;
    }
}