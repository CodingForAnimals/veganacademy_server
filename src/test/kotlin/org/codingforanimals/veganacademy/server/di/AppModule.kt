package org.codingforanimals.veganacademy.server.di

import org.codingforanimals.veganacademy.server.config.plugins.AppConfig
import org.codingforanimals.veganacademy.server.database.DatabaseFactory
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryForServerTest
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryForUnitTest
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appTestModule = module {
    single { AppConfig() }
    single<DatabaseFactory>(named("unit")) { DatabaseFactoryForUnitTest() }
    single<DatabaseFactory>(named("server")) { DatabaseFactoryForServerTest(get()) }
}