package com.amade.dev.parkingapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.amade.dev.parkingapp.model.Utente
import com.amade.dev.parkingapp.utils.Validation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    password: String,
    onValueChange: (String) -> Unit,
    validation: Validation,
) {
    var isVisiblePassword by remember { mutableStateOf(false) }

    val onChangeVisibility = remember {
        {
            isVisiblePassword = !isVisiblePassword
        }
    }

    val icon by remember {
        derivedStateOf {
            if (!isVisiblePassword) {
                Icons.Outlined.Visibility
            } else {
                Icons.Outlined.VisibilityOff
            }
        }
    }

    val passwordVisualTransformation by remember {
        derivedStateOf {
            if (isVisiblePassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }
        }
    }

    TextField(
        value = password,
        onValueChange = onValueChange,
        placeholder = { Text(text = "Password") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Password,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = onChangeVisibility) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        },
        supportingText = {
            if (validation is Validation.Failure) {
                Text(text = validation.message)
            }
        },
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = passwordVisualTransformation,
        isError = validation is Validation.Failure,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),

        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Unspecified
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(email: String, onValueChange: (String) -> Unit, validation: Validation) {


    val onClearText = remember {
        {
            onValueChange("")
        }
    }

    val emailIsNotBlank by remember(email) {
        derivedStateOf {
            email.isNotBlank()
        }
    }


    TextField(
        value = email,
        onValueChange = onValueChange,
        placeholder = { Text(text = "Email") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.AlternateEmail,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (emailIsNotBlank) {
                IconButton(onClick = onClearText) {
                    Icon(imageVector = Icons.Outlined.Clear, contentDescription = null)
                }
            }
        },
        supportingText = {
            if (validation is Validation.Failure) {
                Text(text = validation.message)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth(),
        isError = validation is Validation.Failure,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Unspecified
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameField(name: String, onValueChange: (String) -> Unit, validation: Validation) {

    val onClearText = remember {
        {
            onValueChange("")
        }
    }

    val emailIsNotBlank by remember(name) {
        derivedStateOf {
            name.isNotBlank()
        }
    }


    TextField(
        value = name,
        onValueChange = onValueChange,
        placeholder = { Text(text = "Name") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (emailIsNotBlank) {
                IconButton(onClick = onClearText) {
                    Icon(imageVector = Icons.Outlined.Clear, contentDescription = null)
                }
            }
        },
        supportingText = {
            if (validation is Validation.Failure) {
                Text(text = validation.message)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth(),
        isError = validation is Validation.Failure,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Unspecified
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PaymentPlanChooser(onSelected: (Utente.PaymentPlan) -> Unit) {
    val plans = remember { Utente.PaymentPlan.values() }
    var selected by remember { mutableStateOf<Utente.PaymentPlan>(Utente.PaymentPlan.Daily) }
    var expanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = expanded) {
        keyboardController?.hide()
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .menuAnchor(),
            readOnly = true,
            value = selected.name,
            onValueChange = {},
            label = { Text("Payment Plan") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            plans.forEach { plan ->
                DropdownMenuItem(
                    text = { Text(plan.name) },
                    onClick = {
                        selected = plan.also(onSelected)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun UtenteTypeChooser(onSelected: (Utente.UtenteType) -> Unit) {
    val types = remember { Utente.UtenteType.values() }
    var selected by remember { mutableStateOf(Utente.UtenteType.ESTUDANTE) }
    var expanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = Unit) {
        keyboardController?.hide()
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            value = selected.name,
            onValueChange = {},
            label = { Text("Utente Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        selected = type.also(onSelected)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}