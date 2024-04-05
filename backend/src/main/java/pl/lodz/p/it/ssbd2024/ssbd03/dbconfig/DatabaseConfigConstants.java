package pl.lodz.p.it.ssbd2024.ssbd03.dbconfig;

public class DatabaseConfigConstants {

    // Packages
    public static final String JPA_PACKAGE_TO_SCAN = "pl.lodz.p.it.ssbd2024.ssbd03.entities";

    // Persistence Units
    public static final String ADMIN_PU = "ssbd03adminPU";
    public static final String MOK_PU = "ssbd03mokPU";
    public static final String MOP_PU = "ssbd03mopPU";

    // Data sources
    public static final String DS_ADMIN = "dataSourceAdmin";
    public static final String DS_MOK = "dataSourceMOK";
    public static final String DS_MOP = "dataSourceMOP";

    // Entity managers
    public static final String EMF_ADMIN = "entityManagerFactoryAdmin";
    public static final String EMF_MOK = "entityManagerFactoryMOK";
    public static final String EMF_MOP = "entityManagerFactoryMOP";

    // Transaction managers
    public static final String TXM_ADMIN = "transactionManagerAdmin";
    public static final String TXM_MOK = "transactionManagerMOK";
    public static final String TXM_MOP = "transactionManagerMOP";

}