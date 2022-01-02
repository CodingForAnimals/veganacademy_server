package org.codingforanimals.veganacademy.server.di

import com.google.gson.Gson
import org.codingforanimals.veganacademy.server.config.plugins.AppConfig
import org.codingforanimals.veganacademy.server.database.DatabaseFactory
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryImpl
import org.codingforanimals.veganacademy.server.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.server.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.RecipeSourceImpl
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.UserSourceImpl
import org.codingforanimals.veganacademy.server.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.server.features.model.repository.impl.RecipeRepositoryImpl
import org.codingforanimals.veganacademy.server.features.model.repository.impl.UserRepositoryImpl
import org.codingforanimals.veganacademy.server.utils.RoutingUtils
import org.codingforanimals.veganacademy.server.utils.UserUtils
import org.koin.dsl.module

val appModule = module {
    single { AppConfig() }
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }

    single { Gson() }

    single { RoutingUtils(get()) }

    single { UserUtils() }

    single<UserSource> { UserSourceImpl() }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single<RecipeSource> { RecipeSourceImpl() }
    single<RecipeRepository> { RecipeRepositoryImpl(get()) }

}