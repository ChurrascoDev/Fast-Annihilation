package com.github.imthenico.fastannihilation.storage.impl;

import com.github.imthenico.fastannihilation.config.AnniConfig;
import com.github.imthenico.fastannihilation.mapping.DefaultMapperInstance;
import com.github.imthenico.fastannihilation.storage.AnniStorage;
import com.github.imthenico.fastannihilation.storage.StorageHandlerProvider;
import com.github.imthenico.fastannihilation.storage.StorageSource;
import com.github.imthenico.fastannihilation.storage.StorageSourceType;
import com.github.imthenico.simplecommons.data.db.UserCredential;
import com.github.imthenico.simplecommons.data.db.sql.connector.HikariConnector;
import com.github.imthenico.simplecommons.data.db.sql.model.Constraint;
import com.github.imthenico.simplecommons.data.db.sql.model.SQLTableModel;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.repository.AbstractRepository;
import com.github.imthenico.simplecommons.data.repository.SQLRepository;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLStorageHandlerProvider implements StorageHandlerProvider {

    private static final SQLTableModel MODEL_DATA_TABLE;

    static {
        MODEL_DATA_TABLE = SQLTableModel
                .builder("annihilation-model-data")
                .column("name", "VARCHAR", 100, false, null, Constraint.PRIMARY)
                .column("json", "JSON", -1)
                .build();
    }

    @Override
    public AnniStorage createHandler(
            JavaPlugin plugin, AnniConfig anniConfig
    ) throws Exception {
        UserCredential credential = anniConfig.getUserStorageCredential();

        Validate.notNull(credential, "credential is null");

        Connection connection = new HikariConnector()
                .jdbcUrl(HikariConnector.MYSQL_JDBC_URL)
                .credential(credential)
                .getHandle();

        StorageSource storageSource = new StorageSource(StorageSourceType.MYSQL, connection);
        AbstractRepository<TreeNode> mapModelDataRepository = createRepository(
                connection, TreeNode.class, MODEL_DATA_TABLE
        );

        return new SimpleAnniStorage(
                mapModelDataRepository,
                storageSource
        );
    }

    private <T> AbstractRepository<T> createRepository(
            Connection connection,
            Class<T> modelClass,
            SQLTableModel tableModel
    ) throws SQLException {
        return new SQLRepository<>(
                STORAGE_EXECUTOR,
                modelClass,
                DefaultMapperInstance.getMapper(),
                connection,
                tableModel
        );
    }
}