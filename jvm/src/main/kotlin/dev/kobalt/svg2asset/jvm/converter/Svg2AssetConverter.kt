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

package dev.kobalt.svg2asset.jvm.converter

import com.android.ide.common.vectordrawable.Svg2Vector
import dev.kobalt.svg2asset.jvm.platform.Svg2AssetPlatform
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.fop.svg.PDFTranscoder
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class Svg2AssetConverter {

    fun convert(type: Svg2AssetPlatform, input: InputStream, output: OutputStream) = when (type) {
        Svg2AssetPlatform.Android -> disposableFile {
            it.writeBytes(input.readBytes()); Svg2Vector.parseSvgToXml(it, output)
        }

        Svg2AssetPlatform.Ios -> PDFTranscoder().transcode(TranscoderInput(input), TranscoderOutput(output))
    }

    private fun disposableFile(block: (File) -> Unit) {
        createTempFile().also(block).also { it.delete() }
    }

}