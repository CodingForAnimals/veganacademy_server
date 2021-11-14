package org.codingforanimals.veganacademy.di

import org.codingforanimals.veganacademy.config.plugins.AppConfig
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.features.model.repository.impl.UserRepositoryImpl
import org.codingforanimals.veganacademy.features.routes.user.JwtService
import org.koin.core.annotation.KoinReflectAPI
import org.koin.dsl.module
import org.koin.dsl.single

@KoinReflectAPI
val appModule = module {
    single<AppConfig>()
    single<JwtService>()
    single<UserRepository> { UserRepositoryImpl() }
}