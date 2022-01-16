package org.codingforanimals.veganacademy.server.di

import com.google.gson.Gson
import org.codingforanimals.veganacademy.server.config.plugins.AppConfig
import org.codingforanimals.veganacademy.server.utils.UserUtils
import org.koin.dsl.module


private val appModule = module {
    single { AppConfig() }
}

private val utilsModule = module {
    single { Gson() }
    single { UserUtils() }
}

val sharedModules = mutableListOf(appModule, utilsModule)
