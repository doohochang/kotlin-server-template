package io.github.doohochang.ktserver.repository

import org.springframework.data.r2dbc.convert.R2dbcConverter
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.RowsFetchSpec

inline fun <reified T> DatabaseClient.GenericExecuteSpec.convert(converter: R2dbcConverter): RowsFetchSpec<T> =
    map { row, rowMetadata -> converter.read(T::class.java, row, rowMetadata) }
