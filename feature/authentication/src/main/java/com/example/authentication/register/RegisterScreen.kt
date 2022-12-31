package com.example.authentication.register

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Base64OutputStream
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.authentication.R
import com.example.authentication.navigationArgs.loginScreenRoute
import com.example.authentication.navigationArgs.navigateToAuthScreen
import com.example.common.components.CltButton
import com.example.common.components.CltInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.util.*


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by registerViewModel.registerState.collectAsState()
    val scrollState = rememberScrollState()


    LaunchedEffect(state.isCreated) {
        if (!state.isCreated) return@LaunchedEffect
        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        scaffoldState.snackbarHostState.showSnackbar("Successfully created account!", "Dismiss")
            .also {
                navController.navigateToAuthScreen()
            }
    }

    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.errorChannel.collect {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    scaffoldState.snackbarHostState.showSnackbar(it, "Dismiss")
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {

            Image(
                modifier = Modifier.padding(40.dp),
                contentScale = ContentScale.Fit,
                painter = painterResource(id = R.drawable.foodit_high_resolution_logo_color_on_transparent_background),
                contentDescription = "FoodIt's Logo"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                    .background(MaterialTheme.colors.secondaryVariant),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        text = "Are You A New User?",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "Make a new account!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primaryVariant
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Please fill up all fields",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                    CltInput(
                        value = state.first_name,
                        label = "First Name",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.firstNameError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            registerViewModel.onEvent(
                                RegisterEvent.OnFirstNameChange(first_name = it)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    CltInput(
                        value = state.last_name,
                        label = "Last Name",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.lastNameError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            registerViewModel.onEvent(
                                RegisterEvent.OnLastNameChange(last_name = it)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    CltInput(
                        value = state.username,
                        label = "User Name",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.usernameError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            registerViewModel.onEvent(
                                RegisterEvent.OnUsernameChange(username = it)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Password must contain atleast one uppercase, one lowercase, one numeral, and a special character",
                        Modifier.padding(4.dp),
                        fontSize = 12.sp,
                    )
                    CltInput(
                        value = state.user_pass,
                        label = "User Password",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.userPassError,
                        isPassword = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            registerViewModel.onEvent(
                                RegisterEvent.OnUserPassChange(user_pass = it)
                            )
                        }
                    )
                    CltInput(
                        value = state.confirmUserPass,
                        label = "Confirm User Password",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.confirmUserPassError,
                        isPassword = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            registerViewModel.onEvent(
                                RegisterEvent.OnConfirmUserPassChange(confirmUserPass = it)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    CltInput(
                        value = state.gender,
                        label = "User Gender",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.genderError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            registerViewModel.onEvent(
                                RegisterEvent.OnGenderChange(gender = it)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    CltInput(
                        value = state.mobile_number.toString(),
                        label = "User Mobile Number",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.mobileNumberError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            if (it != "") {
                                registerViewModel.onEvent(
                                    RegisterEvent.OnMobileNumberChange(mobile_number = it.toLong())
                                )
                            } else {
                                registerViewModel.onEvent(
                                    RegisterEvent.OnMobileNumberChange(mobile_number = 65)
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    CltInput(
                        value = state.email,
                        label = "User Email Address",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.emailError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            registerViewModel.onEvent(
                                RegisterEvent.OnEmailChange(email = it)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    CltInput(
                        value = state.address,
                        label = "User Address",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.addressError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            registerViewModel.onEvent(
                                RegisterEvent.OnAddressChange(address = it)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    pickImage()
//                    CltInput(
//                        value = state.profile_pic,
//                        label = "User Profile Picture",
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        error = state.profilePicError,
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Text,
//                            imeAction = ImeAction.Next
//                        ),
//                        keyboardActions = KeyboardActions(
//                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
//                        ),
//                        onValueChange = {
//                            registerViewModel.onEvent(
//                                RegisterEvent.OnProfilePicChange(profile_pic = it)
//                            )
//                        }
//                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = { navController.navigate(loginScreenRoute) }) {
                        Text(
                            text = "Already have an account?",
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.primaryVariant
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    CltButton(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading,
                        onClick = {
                            focusManager.clearFocus()
                            registerViewModel.onEvent(RegisterEvent.OnSubmit)
                        }
                    ) {
                        AnimatedContent(targetState = state.isLoading) { isLoading ->
                            if (isLoading)
                                return@AnimatedContent CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 3.dp
                                )
                            Text(text = "Register", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun pickImage(
    registerViewModel: RegisterViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val myImage: Bitmap =
        BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember {
        mutableStateOf<Bitmap>(myImage)
    }
    val loadImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (Build.VERSION.SDK_INT < 29) {
            result.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        }
        else {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            result.value = ImageDecoder.decodeBitmap(source)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            result.value.asImageBitmap(), contentDescription = "image",
            modifier = Modifier
                .size(300.dp)
                .padding(10.dp)
        )
        CltButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                loadImage.launch("image/*")
            }
        ) {
            Text(text = "Load Image", fontSize = 30.sp, color = Color.White)
        }
    }

    registerViewModel.base64ProfilePic.value = encodeImage(result.value)!!
}

private fun encodeImage(bm: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}