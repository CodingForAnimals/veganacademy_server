package testutils

import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import org.codingforanimals.veganacademy.config.plugins.AppConfig
import org.codingforanimals.veganacademy.config.plugins.DatabaseConfig
import org.codingforanimals.veganacademy.config.plugins.ServerConfig
import org.codingforanimals.veganacademy.database.DatabaseFactory
import org.codingforanimals.veganacademy.database.DatabaseFactoryForServerTest
import org.codingforanimals.veganacademy.database.DatabaseFactoryImpl
import org.codingforanimals.veganacademy.features.model.data.source.UserDataSource
import org.codingforanimals.veganacademy.features.model.data.source.UserDataSourceImpl
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.features.model.repository.impl.UserRepositoryImpl
import org.codingforanimals.veganacademy.features.routes.user.JwtService
import org.codingforanimals.veganacademy.run
import org.koin.core.module.Module
import org.koin.dsl.module

fun MapApplicationConfig.createConfigForTesting() {
    // Server config
    put("ktor.server.isProd", "false")

    // Database Config
    put("ktor.database.jdbcDriver", "org.h2.Driver")
    put("ktor.database.jdbcDatabaseUrl", "jdbc:h2:mem:;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE")
    put("ktor.database.dbUser", "root")
    put("ktor.database.dbPassword", "password")
    put("ktor.database.maxPoolSize", "1")

    // JWT config
    put("ktor.jwt.issuer", "issuer")
    put("ktor.jwt.secret", "secret")
}

fun getAppConfigForUnitTest(): AppConfig {

    return AppConfig().apply {
        databaseConfig = DatabaseConfig(
            jdbcDriver = "org.h2.Driver",
            jdbcDatabaseUrl = "jdbc:h2:mem:;DATABASE_TO_UPPER=false;MODE=MYSQL",
            dbUser = "root",
            dbPassword = "password",
            maxPoolSize = 1
        )
        serverConfig = ServerConfig(isProd = false)
    }
}

@KtorExperimentalLocationsAPI
fun withTestServer(koinModules: List<Module> = listOf(appTestModule), block: TestApplicationEngine.() -> Unit) {
    withTestApplication(
        {
            (environment.config as MapApplicationConfig).apply {
                createConfigForTesting()
            }
            run(testing = true, koinModules = koinModules)
        }, block
    )
}

fun setContentType(request: TestApplicationRequest) =
    request.addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())

val appTestModule = module {
    single {  getAppConfigForUnitTest() }
    single { JwtService(get()) }
    single<DatabaseFactory> { DatabaseFactoryForServerTest(get()) }
    single<UserDataSource> { UserDataSourceImpl() }
    single<UserRepository> { UserRepositoryImpl(get()) }

}