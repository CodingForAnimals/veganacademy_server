package org.codingforanimals

import org.codingforanimals.auth.JwtService
import org.codingforanimals.model.repository.UserRepository
import org.codingforanimals.model.repository.impl.UserRepositoryImpl
import org.koin.core.annotation.KoinReflectAPI
import org.koin.dsl.module
import org.koin.dsl.single

@KoinReflectAPI
val appModule = module {
    single<AppConfig>()
    single<JwtService>()
    single<UserRepository> { UserRepositoryImpl() }

}