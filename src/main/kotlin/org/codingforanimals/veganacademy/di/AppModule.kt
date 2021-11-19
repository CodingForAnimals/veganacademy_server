package org.codingforanimals.veganacademy.di

import org.codingforanimals.veganacademy.config.plugins.AppConfig
import org.codingforanimals.veganacademy.database.DatabaseFactory
import org.codingforanimals.veganacademy.database.DatabaseFactoryImpl
import org.codingforanimals.veganacademy.features.model.data.source.UserDataSource
import org.codingforanimals.veganacademy.features.model.data.source.UserDataSourceImpl
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.features.model.repository.impl.UserRepositoryImpl
import org.codingforanimals.veganacademy.features.routes.user.JwtService
import org.koin.core.annotation.KoinReflectAPI
import org.koin.dsl.module
import org.koin.dsl.single

val appModule = module {
    single { AppConfig() }
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }
    single { JwtService(get()) }
    single<UserDataSource> { UserDataSourceImpl() }
    single<UserRepository> { UserRepositoryImpl(get()) }
}