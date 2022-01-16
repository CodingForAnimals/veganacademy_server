package org.codingforanimals.veganacademy.server.di

import org.codingforanimals.veganacademy.server.database.DatabaseFactory
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryImpl
import org.codingforanimals.veganacademy.server.features.model.data.source.LoggedInUserDataSource
import org.codingforanimals.veganacademy.server.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.server.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.LoggedInUserDataSourceImpl
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.RecipeSourceImpl
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.UserSourceImpl
import org.codingforanimals.veganacademy.server.features.model.repository.LoggedInUserRepository
import org.codingforanimals.veganacademy.server.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.server.features.model.repository.impl.LoggedInUserRepositoryImpl
import org.codingforanimals.veganacademy.server.features.model.repository.impl.RecipeRepositoryImpl
import org.codingforanimals.veganacademy.server.features.model.repository.impl.UserRepositoryImpl
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService
import org.codingforanimals.veganacademy.server.features.model.service.RememberMeService
import org.codingforanimals.veganacademy.server.features.model.service.UserService
import org.codingforanimals.veganacademy.server.features.model.service.impl.RecipeServiceImpl
import org.codingforanimals.veganacademy.server.features.model.service.impl.RememberMeServiceImpl
import org.codingforanimals.veganacademy.server.features.model.service.impl.UserServiceImpl
import org.koin.dsl.module

private val databaseModule = module {
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }
}

private val userModule = module {
    single<UserSource> { UserSourceImpl() }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<UserService> { UserServiceImpl(get()) }
}

private val loggedInUserModule = module {
    single<LoggedInUserDataSource> { LoggedInUserDataSourceImpl() }
    single<LoggedInUserRepository> { LoggedInUserRepositoryImpl(get()) }
    single<RememberMeService> { RememberMeServiceImpl(get()) }
}

private val recipeModule = module {
    single<RecipeSource> { RecipeSourceImpl() }
    single<RecipeRepository> { RecipeRepositoryImpl(get()) }
    single<RecipeService> { RecipeServiceImpl(get()) }
}

val dataModules = mutableListOf(
    databaseModule,
    userModule,
    loggedInUserModule,
    recipeModule
)