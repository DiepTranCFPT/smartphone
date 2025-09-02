package managesmartphone.diepxdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DiepxdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiepxdemoApplication.class, args);
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    public CommandLineRunner updateSchema() {
        return args -> {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            // Check if columns exist before attempting to drop them
            try {
                jdbcTemplate.execute("ALTER TABLE purchases DROP COLUMN IF EXISTS item_name");
                jdbcTemplate.execute("ALTER TABLE purchases DROP COLUMN IF EXISTS qty");
                jdbcTemplate.execute("ALTER TABLE purchases DROP COLUMN IF EXISTS unit_price");
                jdbcTemplate.execute("ALTER TABLE purchases DROP COLUMN IF EXISTS warranty_months");
            } catch (Exception e) {
                // Log error but don't fail startup
                System.err.println("Error updating schema: " + e.getMessage());
            }
        };
    }
}
