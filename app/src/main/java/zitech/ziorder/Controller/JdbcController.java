package zitech.ziorder.Controller;

import java.sql.Connection;

import zitech.ziorder.Models.JdbcModel;

public class JdbcController {
    JdbcModel jdbcModel = new JdbcModel();

    public Connection ConnnectionData() {
        return jdbcModel.getConnectionOf();
    }
}
