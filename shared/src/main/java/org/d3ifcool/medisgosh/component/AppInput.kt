package org.d3ifcool.medisgosh.component


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.ui.theme.AppGrayColor
import org.d3ifcool.medisgosh.ui.theme.AppTypography

class AppInput {
    companion object {
        @Composable
        fun Default(
            modifier: Modifier = Modifier,
            isPassword: Boolean = false,
            value: String,
            placeHolder: String = "",
            fontSize: TextUnit = 15.sp,
            fontWeight: FontWeight = FontWeight.Normal,
            fontColor: Color = Color.Black,
            placeHolderColor: Color? = null,
            placeHolderStyle: TextStyle? = null,
            containerColor: Color? = null,
            colors: TextFieldColors? = null,
            capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
            keyboardType: KeyboardType = KeyboardType.Text,
            keyboardActions: KeyboardActions = KeyboardActions.Default,
            imeAction: ImeAction = ImeAction.Default,
            shape: Shape? = null,
            isError: Boolean = false,
            enabled: Boolean = true,
            readOnly: Boolean = false,
            testTag: String,
            prefix: @Composable() (() -> Unit)? = null,
            suffix: @Composable() (() -> Unit)? = null,
            leadingIcon: @Composable() (() -> Unit)? = null,
            trailingIcon: @Composable() (() -> Unit)? = null,
            onValueChange: (String) -> Unit
        ) {
            var passwordVisible by rememberSaveable { mutableStateOf(false) }

            TextField(
                prefix = if (prefix != null) {
                    { prefix() }
                } else null,
                suffix = if (suffix != null) {
                    { suffix() }
                } else null,
                modifier = modifier
                    .fillMaxWidth()
                    .testTag(testTag),
                shape = shape ?: RoundedCornerShape(10.dp),
                enabled = enabled,
                readOnly = readOnly,
                isError = isError,
                textStyle = AppTypography.copy(
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    color = fontColor,
                ),
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    AppText.Regular16(
                        text = placeHolder,
                        color = placeHolderColor ?: Color.Black,
                        style = placeHolderStyle ?: LocalTextStyle.current
                    )
                },
                colors = colors ?: TextFieldDefaults.colors(
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = containerColor ?: Color.White,
                    focusedContainerColor = containerColor ?: Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorContainerColor = containerColor?.copy(alpha = .7f)
                        ?: Color.White.copy(alpha = .7f),
                    errorPlaceholderColor = containerColor?.copy(alpha = .7f)
                        ?: Color.White.copy(alpha = .7f),
                ),
                visualTransformation = if (passwordVisible || !isPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    capitalization = capitalization,
                    keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = keyboardActions,
                leadingIcon = if (leadingIcon != null) {
                    { leadingIcon() }
                } else null,
                trailingIcon = if (isPassword) {
                    {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff

                        // Provide localized description for accessibility services
                        val description = stringResource(
                            if (passwordVisible) R.string.hide_password else R.string.show_password
                        )

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = description,
                                tint = Color.Gray
                            )
                        }
                    }
                } else if (trailingIcon != null) {
                    { trailingIcon() }
                } else null,
            )
        }

        @Composable
        fun WithLabel(
            label: String = "",
            modifier: Modifier = Modifier,
            isPassword: Boolean = false,
            value: String,
            placeHolder: String = "",
            fontSize: TextUnit = 15.sp,
            fontWeight: FontWeight = FontWeight.Normal,
            fontColor: Color = Color.Black,
            placeHolderColor: Color? = null,
            placeHolderStyle: TextStyle? = null,
            containerColor: Color? = null,
            colors: TextFieldColors? = null,
            capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
            keyboardType: KeyboardType = KeyboardType.Text,
            keyboardActions: KeyboardActions = KeyboardActions.Default,
            imeAction: ImeAction = ImeAction.Default,
            shape: Shape? = null,
            isError: Boolean = false,
            enabled: Boolean = true,
            readOnly: Boolean = false,
            testTag: String,
            prefix: @Composable() (() -> Unit)? = null,
            suffix: @Composable() (() -> Unit)? = null,
            leadingIcon: @Composable() (() -> Unit)? = null,
            trailingIcon: @Composable() (() -> Unit)? = null,
            onValueChange: (String) -> Unit
        ) {
            var passwordVisible by rememberSaveable { mutableStateOf(false) }

            Column {
                AppText.Small15(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    text = label,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                TextField(
                    prefix = if (prefix != null) {
                        { prefix() }
                    } else null,
                    suffix = if (suffix != null) {
                        { suffix() }
                    } else null,
                    modifier = modifier
                        .fillMaxWidth()
                        .testTag(testTag),
                    shape = shape ?: RoundedCornerShape(10.dp),
                    enabled = enabled,
                    readOnly = readOnly,
                    isError = isError,
                    textStyle = AppTypography.copy(
                        fontSize = fontSize,
                        fontWeight = fontWeight,
                        color = fontColor,
                    ),
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = {
                        AppText.Regular16(
                            text = placeHolder,
                            color = placeHolderColor ?: Color.Black,
                            style = placeHolderStyle ?: LocalTextStyle.current
                        )
                    },
                    colors = colors ?: TextFieldDefaults.colors(
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedContainerColor = containerColor ?: Color.White,
                        focusedContainerColor = containerColor ?: Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        errorContainerColor = containerColor?.copy(alpha = .7f)
                            ?: Color.White.copy(alpha = .7f),
                        errorPlaceholderColor = containerColor?.copy(alpha = .7f)
                            ?: Color.White.copy(alpha = .7f),
                    ),
                    visualTransformation = if (passwordVisible || !isPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = capitalization,
                        keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
                        imeAction = imeAction
                    ),
                    keyboardActions = keyboardActions,
                    leadingIcon = if (leadingIcon != null) {
                        { leadingIcon() }
                    } else null,
                    trailingIcon = if (isPassword) {
                        {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                            // Provide localized description for accessibility services
                            val description = stringResource(
                                if (passwordVisible) R.string.hide_password else R.string.show_password
                            )

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = description,
                                    tint = Color.Gray
                                )
                            }
                        }
                    } else if (trailingIcon != null) {
                        { trailingIcon() }
                    } else null,
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        @Composable
        fun DatePicker(
            useLabel: Boolean = false,
            label: String = "",
            modifier: Modifier = Modifier,
            value: String,
            placeHolder: String = "",
            fontSize: TextUnit = 15.sp,
            fontWeight: FontWeight = FontWeight.Normal,
            fontColor: Color = Color.Black,
            placeHolderColor: Color? = null,
            placeHolderStyle: TextStyle? = null,
            containerColor: Color? = null,
            colors: TextFieldColors? = null,
            shape: Shape? = null,
            isError: Boolean = false,
            readOnly: Boolean = false,
            testTag: String,
            onValueChange: (String) -> Unit
        ) {
            var showDatePicker by remember { mutableStateOf(false) }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    onDateSelected = { date ->
                        onValueChange(date.toString())
                        showDatePicker = false
                    }
                )
            }
            Column {
                if (useLabel) {
                    AppText.Small15(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        text = label,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            showDatePicker = true
                        }
                        .testTag(testTag)
                ) {
                    TextField(
                        modifier = modifier
                            .fillMaxWidth(),
                        shape = shape ?: RoundedCornerShape(10.dp),
                        enabled = false,
                        readOnly = readOnly,
                        isError = isError,
                        textStyle = AppTypography.copy(
                            fontSize = fontSize,
                            fontWeight = fontWeight,
                            color = fontColor,
                        ),
                        value = value,
                        onValueChange = { },
                        placeholder = {
                            AppText.Regular16(
                                text = placeHolder,
                                color = placeHolderColor ?: Color.Black,
                                style = placeHolderStyle ?: LocalTextStyle.current
                            )
                        },
                        colors = colors ?: TextFieldDefaults.colors(
                            unfocusedTextColor = Color.Black,
                            disabledTextColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedContainerColor = containerColor ?: Color.White,
                            focusedContainerColor = containerColor ?: Color.White,
                            disabledContainerColor = containerColor ?: Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorContainerColor = containerColor?.copy(alpha = .7f)
                                ?: Color.White.copy(alpha = .7f),
                            errorPlaceholderColor = containerColor?.copy(alpha = .7f)
                                ?: Color.White.copy(alpha = .7f),
                        ),
                        trailingIcon =
                        {
                            IconButton(onClick = {
                                showDatePicker = true
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.calendar),
                                    contentDescription = "Pick Date Button",
                                    tint = Color.Unspecified,
                                )
                            }
                        },
                    )

                }
                Spacer(Modifier.height(16.dp))
            }
        }

        @Composable
        fun Dropdown(
            label: String? = null,
            testTag: String,
            dropdownItems: List<String>,
            modifier: Modifier = Modifier,
            initialValue: String,
            placeHolder: String,
            onItemSelected: (String) -> Unit
        ) {
            var expanded by remember { mutableStateOf(false) }
            var selectedItem by remember { mutableStateOf(initialValue.ifEmpty { placeHolder }) }

            LaunchedEffect(initialValue) {
                selectedItem = initialValue
            }

            Box(modifier = modifier) {
                // The trigger for the dropdown
                Column {
                    if (label != null) {
                        AppText.Small15(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            text = label,
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(testTag)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { expanded = true }
                            .background(Color.White)
                            .padding(18.dp)
                        ,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppText.Small15(
                            text = selectedItem,
                            color = if (initialValue.isEmpty()) Color.Gray.copy(alpha = .8f) else Color.Black
                        )
                        Icon(
                            painter = painterResource(if (expanded) R.drawable.baseline_keyboard_arrow_up_24 else R.drawable.baseline_keyboard_arrow_down_24),
                            contentDescription = "Dropdown Button",
                            tint = AppGrayColor
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // Dropdown menu
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    dropdownItems.forEach { item ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            text = { AppText.Small15(text = item) },
                            onClick = {
                                selectedItem = item
                                onItemSelected(item)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        @Composable
        fun IsDigitOnly(
            label: String = "",
            modifier: Modifier = Modifier,
            value: String,
            placeHolder: String = "",
            fontSize: TextUnit = 15.sp,
            fontWeight: FontWeight = FontWeight.Normal,
            fontColor: Color = Color.Black,
            placeHolderColor: Color? = null,
            placeHolderStyle: TextStyle? = null,
            containerColor: Color? = null,
            colors: TextFieldColors? = null,
            keyboardActions: KeyboardActions = KeyboardActions.Default,
            imeAction: ImeAction = ImeAction.Next,
            shape: Shape? = null,
            isError: Boolean = false,
            enabled: Boolean = true,
            readOnly: Boolean = false,
            testTag: String,
            prefix: @Composable() (() -> Unit)? = null,
            suffix: @Composable() (() -> Unit)? = null,
            leadingIcon: @Composable() (() -> Unit)? = null,
            trailingIcon: @Composable() (() -> Unit)? = null,
            onValueChange: (String) -> Unit
        ) {
            Column {
                AppText.Small15(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    text = label,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                TextField(
                    prefix = if (prefix != null) {
                        { prefix() }
                    } else null,
                    suffix = if (suffix != null) {
                        { suffix() }
                    } else null,
                    modifier = modifier
                        .fillMaxWidth()
                        .testTag(testTag),
                    shape = shape ?: RoundedCornerShape(10.dp),
                    enabled = enabled,
                    readOnly = readOnly,
                    isError = isError,
                    textStyle = AppTypography.copy(
                        fontSize = fontSize,
                        fontWeight = fontWeight,
                        color = fontColor,
                    ),
                    value = value,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            onValueChange(it)
                        }
                    },
                    placeholder = {
                        AppText.Regular16(
                            text = placeHolder,
                            color = placeHolderColor ?: Color.Black,
                            style = placeHolderStyle ?: LocalTextStyle.current
                        )
                    },
                    colors = colors ?: TextFieldDefaults.colors(
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedContainerColor = containerColor ?: Color.White,
                        focusedContainerColor = containerColor ?: Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        errorContainerColor = containerColor?.copy(alpha = .7f)
                            ?: Color.White.copy(alpha = .7f),
                        errorPlaceholderColor = containerColor?.copy(alpha = .7f)
                            ?: Color.White.copy(alpha = .7f),
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction),
                    keyboardActions = keyboardActions,
                    leadingIcon = if (leadingIcon != null) {
                        { leadingIcon() }
                    } else null,
                    trailingIcon = if (trailingIcon != null) {
                        { trailingIcon() }
                    } else null,
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}