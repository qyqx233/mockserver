package me.ifling;

import io.ktor.server.engine.ApplicationEngine;
import me.liuwj.ktorm.database.Database;

public interface ServiceHandler1 {
    void prepareDB(Database database);

    String handle(Context ctx, String message);

    void parseRcv(Context ctx, String message);

    void registerRouter(ApplicationEngine server);
}
