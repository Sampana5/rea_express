package com.rea.express.wrapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminStatsWrapper {
    private long users;
    private long activeUsers;
    private long admins;
    private long products;
    private long categories;
    private long quotes;
    private long pendingQuotes;
    private long quotedQuotes;
}
