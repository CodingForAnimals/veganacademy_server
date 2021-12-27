package org.codingforanimals.veganacademy.utils

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

inline fun <reified T> genericType(): Type = object: TypeToken<T>(){}.type