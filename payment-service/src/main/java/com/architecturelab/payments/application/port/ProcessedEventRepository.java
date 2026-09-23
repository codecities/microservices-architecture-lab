package com.architecturelab.payments.application.port;

import java.util.UUID;

public interface ProcessedEventRepository {

    boolean exists(UUID eventId);

    void save(UUID eventId, String type);
}
