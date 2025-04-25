package org.bmserver.app.common.config.db

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.convert.CustomConversions.StoreConversions
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.PostgresDialect
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
class DBConfig {
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        val populator = ResourceDatabasePopulator(ClassPathResource("schema.sql"))
        initializer.setDatabasePopulator(populator)
        return initializer
    }


    @Bean
    fun r2dbcCustomConversions(): R2dbcCustomConversions {
        val converters = listOf(
            VectorToListFloatConverter()
        )

        val storeConversions = StoreConversions.of(PostgresDialect.INSTANCE.simpleTypeHolder, converters)

        return R2dbcCustomConversions(storeConversions, converters)
    }
}
