// createScreen.kts

import java.io.File


val screenName = "WishlistDetail"//args.getOrNull(0) ?: error("Ekran adı girilmedi")
val packageName = screenName.lowercase()
val baseDir = File("composeApp/src/commonMain/kotlin/com/yusufteker/worthy/screen/$packageName/presentation")

val files = listOf(
    "$screenName" + "Action.kt" to """
        package com.yusufteker.worthy.screen.$packageName.presentation

        sealed interface ${screenName}Action {
            object Init : ${screenName}Action
        }
    """.trimIndent(),

    "$screenName" + "State.kt" to """
        package com.yusufteker.worthy.screen.$packageName.presentation

        data class ${screenName}State(
            val isLoading: Boolean = false,
            val errorMessage: String? = null
        )
    """.trimIndent(),

    "$screenName" + "ViewModel.kt" to """
        package com.yusufteker.worthy.screen.$packageName.presentation

        import com.yusufteker.worthy.core.presentation.BaseViewModel
        import kotlinx.coroutines.flow.MutableStateFlow
        import kotlinx.coroutines.flow.StateFlow

        class ${screenName}ViewModel : BaseViewModel() {
            private val _state = MutableStateFlow(${screenName}State())
            val state: StateFlow<${screenName}State> = _state

            fun onAction(action: ${screenName}Action) {
                when (action) {
                    is ${screenName}Action.Init -> {
                        // TODO
                    }
                }
            }
        }
    """.trimIndent(),

    "$screenName" + "Screen.kt" to """
        package com.yusufteker.worthy.screen.$packageName.presentation

        import androidx.compose.foundation.layout.*
        import androidx.compose.runtime.*
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.unit.dp
        import androidx.lifecycle.compose.collectAsStateWithLifecycle
        import org.koin.compose.viewmodel.koinViewModel

        @Composable
        fun ${screenName}ScreenRoot(
            viewModel: ${screenName}ViewModel = koinViewModel(),
            contentPadding: PaddingValues = PaddingValues()
        ) {
            val state by viewModel.state.collectAsStateWithLifecycle()
            ${screenName}Screen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
        }

        @Composable
        fun ${screenName}Screen(
            state: ${screenName}State,
            onAction: (action: ${screenName}Action) -> Unit,
            contentPadding: PaddingValues = PaddingValues()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // TODO
            }
        }
    """.trimIndent()
)

baseDir.mkdirs()

files.forEach { (filename, content) ->
    val file = File(baseDir, filename)
    file.writeText(content)
    println("Oluşturuldu: ${file.path}")
}
