/*
 * dev.kobalt.svg2asset
 * Copyright (C) 2022 Tom.K
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.kobalt.svg2asset.jvm

import dev.kobalt.svg2asset.jvm.converter.Svg2AssetConverter
import dev.kobalt.svg2asset.jvm.platform.Svg2AssetPlatform
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import java.io.BufferedInputStream
import java.io.File

fun main(args: Array<String>) {
    val parser = ArgParser("csv2i18n")
    val inputPath by parser.option(ArgType.String, "inputPath", null, null)
    val outputPath by parser.option(ArgType.String, "outputPath", null, null)
    val outputType by parser.option(ArgType.String, "outputType", null, null)
    parser.parse(args)
    val type = when (outputType) {
        "android" -> Svg2AssetPlatform.Android
        "ios" -> Svg2AssetPlatform.Ios
        else -> throw Exception("Unsupported output type: $outputType")
    }
    val converter = Svg2AssetConverter()
    ((inputPath?.let { File(it).readText() }) ?: run {
        BufferedInputStream(System.`in`).use {
            if (it.available() > 0) it.readBytes().decodeToString() else null
        }
    })?.let { input ->
        val inputStream = input.byteInputStream()
        (outputPath?.let { File(it) }?.outputStream() ?: System.out)?.let { outputStream ->
            converter.convert(type, inputStream, outputStream)
        }
    }
}