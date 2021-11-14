package org.codingforanimals.veganacademy.di

import org.codingforanimals.veganacademy.config.AppConfig
import org.codingforanimals.veganacademy.auth.JwtService
import org.codingforanimals.veganacademy.model.repository.UserRepository
import org.codingforanimals.veganacademy.model.repository.impl.UserRepositoryImpl
import org.koin.core.annotation.KoinReflectAPI
import org.koin.dsl.module
import org.koin.dsl.single

@KoinReflectAPI
val appModule = module {
    single<AppConfig>()
    single<JwtService>()
    single<UserRepository> { UserRepositoryImpl() }

}