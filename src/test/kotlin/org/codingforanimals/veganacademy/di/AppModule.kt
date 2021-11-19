package org.codingforanimals.veganacademy.di

import org.codingforanimals.veganacademy.config.plugins.AppConfig
import org.codingforanimals.veganacademy.database.DatabaseFactory
import org.codingforanimals.veganacademy.database.DatabaseFactoryForServerTest
import org.codingforanimals.veganacademy.database.DatabaseFactoryForUnitTest
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appTestModule = module {
    single { AppConfig() }
    single<DatabaseFactory>(named("unit")) { DatabaseFactoryForUnitTest() }
    single<DatabaseFactory>(named("server")) { DatabaseFactoryForServerTest(get()) }
}