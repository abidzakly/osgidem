package org.d3ifcool.medisgosh.component

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import org.d3ifcool.medisgosh.ui.theme.Archivo

class AppText {
    companion object {
        @Composable
        fun Large32(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                fontStyle = fontStyle,
                text = text,
                fontSize = 32.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Large24(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                fontStyle = fontStyle,
                text = text,
                fontSize = 24.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Semi22(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                fontStyle = fontStyle,
                text = text,
                fontSize = 22.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Semi20(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                fontStyle = fontStyle,
                text = text,
                fontSize = 20.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Medium18(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                fontStyle = fontStyle,
                text = text,
                fontSize = 18.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Regular16(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            lineHeight: TextUnit = TextUnit.Unspecified,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                softWrap = true,
                lineHeight = lineHeight,
                fontStyle = fontStyle,
                text = text,
                fontSize = 16.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Regular16Annotated(
            modifier: Modifier = Modifier,
            text: AnnotatedString,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            lineHeight: TextUnit = TextUnit.Unspecified,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                softWrap = true,
                lineHeight = lineHeight,
                fontStyle = fontStyle,
                text = text,
                fontSize = 16.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Small15Annotated(
            modifier: Modifier = Modifier,
            text: AnnotatedString,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            lineHeight: TextUnit = TextUnit.Unspecified,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                softWrap = true,
                lineHeight = lineHeight,
                fontStyle = fontStyle,
                text = text,
                fontSize = 15.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Small15(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                fontStyle = fontStyle,
                text = text,
                fontSize = 15.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Small14(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                fontStyle = fontStyle,
                text = text,
                fontSize = 14.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Small12(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                fontStyle = fontStyle,
                text = text,
                fontSize = 12.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Small10(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            fontFamily: FontFamily? = null,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                fontStyle = fontStyle,
                text = text,
                fontSize = 10.sp,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }

        @Composable
        fun Custom(
            modifier: Modifier = Modifier,
            text: String,
            fontWeight: FontWeight = FontWeight.Normal,
            color: Color = Color.Black,
            textAlign: TextAlign = TextAlign.Start,
            maxLines: Int = Int.MAX_VALUE,
            overflow: TextOverflow = TextOverflow.Clip,
            fontStyle: FontStyle? = null,
            fontFamily: FontFamily? = null,
            fontSize: TextUnit = 15.sp,
            style: TextStyle = LocalTextStyle.current,
        ) {
            Text(
                style = style,
                fontStyle = fontStyle,
                text = text,
                fontSize = fontSize,
                fontFamily = fontFamily ?: Archivo,
                fontWeight = fontWeight,
                modifier = modifier,
                color = color,
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = overflow
            )
        }
    }
}