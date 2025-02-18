package jdbc;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.name = name;
    }

    public static CustomDataSource getInstance() {
        if(instance != null)
            return instance;

        Properties p = readAppProperties();
        String driver = p.getProperty("postgres.driver");
        String url = p.getProperty("postgres.url");
        String password = p.getProperty("postgres.password");
        String name = p.getProperty("postgres.name");

        return new CustomDataSource(driver,url,password,name);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new CustomConnector().getConnection(this.url, this.name, this.password);
    }

    @Override
    public Connection getConnection(String username, String password) {
        return new CustomConnector().getConnection(this.url, username, password);
    }

    @Override
    public PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) {

    }

    @Override
    public void setLoginTimeout(int seconds) {

    }

    @Override
    public int getLoginTimeout()  {
        return 0;
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    private static Properties readAppProperties() {
        /*
        Properties appProp = new Properties();
        File propFile = new File("src/main/resources/app.properties");
        try(java.io.FileReader fr = new java.io.FileReader(propFile);
            BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] prop = line.split("=");
                appProp.setProperty(prop[0], prop[1]);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        return appProp;
        */
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        InputStream stream = loader.getResourceAsStream("app.properties");
        try {
            props.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return props;
    }
}
