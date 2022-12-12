@file:OptIn(KtorExperimentalLocationsAPI::class)

package testutils

import com.google.gson.Gson
import io.ktor.server.application.ApplicationCall
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import org.codingforanimals.veganacademy.server.config.plugins.AppConfig
import org.codingforanimals.veganacademy.server.config.plugins.UserSession
import org.codingforanimals.veganacademy.server.database.DatabaseFactory
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryForServerTest
import org.codingforanimals.veganacademy.server.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.server.features.model.data.source.RememberMeDataSource
import org.codingforanimals.veganacademy.server.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.RecipeSourceImpl
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.RememberMeDataSourceImpl
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.UserSourceImpl
import org.codingforanimals.veganacademy.server.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.server.features.model.repository.RememberMeRepository
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.server.features.model.repository.impl.RecipeRepositoryImpl
import org.codingforanimals.veganacademy.server.features.model.repository.impl.RememberMeRepositoryImpl
import org.codingforanimals.veganacademy.server.features.model.repository.impl.UserRepositoryImpl
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService
import org.codingforanimals.veganacademy.server.features.model.service.RememberMeService
import org.codingforanimals.veganacademy.server.features.model.service.UserService
import org.codingforanimals.veganacademy.server.features.model.service.impl.RecipeServiceImpl
import org.codingforanimals.veganacademy.server.features.model.service.impl.RememberMeServiceImpl
import org.codingforanimals.veganacademy.server.features.model.service.impl.UserServiceImpl
import org.codingforanimals.veganacademy.server.run
import org.codingforanimals.veganacademy.server.utils.UserUtils
import org.koin.core.module.Module
import org.koin.dsl.module

fun MapApplicationConfig.createConfigForTesting() {
    // Server config
    put("ktor.server.isProd", "false")
    put("ktor.server.isTesting", "true")

    // Database Config
    put("ktor.database.jdbcDriver", "org.h2.Driver")
//    put("ktor.database.jdbcDatabaseUrl", "jdbc:h2:mem:;DATABASE_TO_UPPER=false;MODE=MYSQL")
    put("ktor.database.jdbcDatabaseUrl", "jdbc:h2:mem:test")
    put("ktor.database.dbUser", "root")
    put("ktor.database.dbPassword", "password")
    put("ktor.database.maxPoolSize", "1")
}

fun withTestServer(koinModules: List<Module> = listOf(appTestModule), block: TestApplicationEngine.() -> Unit) {
    withTestApplication(
        {
            (environment.config as MapApplicationConfig).apply { createConfigForTesting() }
            run(koinModules = koinModules)
        }, block

    )
}

val appTestModule = module {
    single { Gson() }
    single { AppConfig() }
    single<DatabaseFactory> { DatabaseFactoryForServerTest(get()) }

    single { UserUtils() }

    single<RememberMeDataSource> { RememberMeDataSourceImpl() }
    single<RememberMeRepository> { RememberMeRepositoryImpl(get()) }
    single<RememberMeService> { RememberMeServiceImpl(get()) }

    single<UserSource> { UserSourceImpl() }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<UserService> { UserServiceImpl(get(), get()) }

    single<RecipeSource> { RecipeSourceImpl() }
    single<RecipeRepository> { RecipeRepositoryImpl(get()) }
    single<RecipeService> { RecipeServiceImpl(get()) }
}

fun ApplicationCall.setUserSession(userId: Int) {
    sessions.set(UserSession(userId))
}