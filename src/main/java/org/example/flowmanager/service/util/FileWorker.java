package org.example.flowmanager.service.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FileWorker {
    public static UUID getEventId(String key) {
        int slashIndex = key.indexOf('/');
        String stringUUID = key.substring(slashIndex + 1, slashIndex + 37);
        return UUID.fromString(stringUUID);
    }

    public static String getFileName(String key) {
        return key.substring(key.lastIndexOf('/') + 1);
    }
}
