package com.jackli.common.util;

import java.util.UUID;

/**
 * @description:
 * @author: luoxingcheng
 * @created: 2023/12/08 18:41
 */
public class UniqueIDGenerator {

    public static long generateUniqueID() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
    }

}
