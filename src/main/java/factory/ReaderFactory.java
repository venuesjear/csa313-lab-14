package factory;

import dto.ReaderRequest;
import model.Reader;

public class ReaderFactory extends BaseFactory<Reader> {
    @Override
    public Reader create(String id, String name, String additionalInfo) {
        return new Reader(id, name);
    }

    public Reader createFromRequest(ReaderRequest request) {
        return create(request.id, request.name, null);
    }
}