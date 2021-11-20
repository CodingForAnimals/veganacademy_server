package org.codingforanimals.veganacademy.di

import org.codingforanimals.veganacademy.config.plugins.AppConfig
import org.codingforanimals.veganacademy.database.DatabaseFactory
import org.codingforanimals.veganacademy.database.DatabaseFactoryImpl
import org.codingforanimals.veganacademy.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.features.model.data.source.impl.RecipeSourceImpl
import org.codingforanimals.veganacademy.features.model.data.source.impl.UserSourceImpl
import org.codingforanimals.veganacademy.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.features.model.repository.impl.RecipeRepositoryImpl
import org.codingforanimals.veganacademy.features.model.repository.impl.UserRepositoryImpl
import org.codingforanimals.veganacademy.features.routes.user.JwtService
import org.koin.dsl.module

val appModule = module {
    single { AppConfig() }
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }
    single { JwtService(get()) }

    single<UserSource> { UserSourceImpl() }
    single<UserRepository> { UserRepositoryImpl(get()) }

    single<RecipeSource> { RecipeSourceImpl() }
    single<RecipeRepository> { RecipeRepositoryImpl(get()) }
}