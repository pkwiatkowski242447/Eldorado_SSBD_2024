//package pl.lodz.p.it.ssbd2024.ssbd03.dbconfig;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.stereotype.Component;
//
//@Component
//@PropertySource(value = {"classpath:application.properties"})
//@Getter @Setter
//@NoArgsConstructor
//public class DatabaseProperties {
//    @Value("${hibernate.dialect}")
//    private String dialect;
//    @Value("${hibernate.show_sql}")
//    private String showSql;
//    @Value("${hibernate.format_sql}")
//    private String formatSql;
//    @Value("${hibernate.hbm2ddl.auto}")
//    private String hbm2ddlAuto;
//    @Value("${jdbc.driverClassName}")
//    private String driverClassName;
//    @Value("${jdbc.url}")
//    private String url;
//    @Value("${jdbc.admin.username}")
//    private String username;
//    @Value("${jdbc.admin.password}")
//    private String password;
//}
