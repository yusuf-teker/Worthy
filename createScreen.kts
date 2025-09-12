// createScreen.kts

import java.io.File

val screenName = "InstallmentList" // args.getOrNull(0) ?: error("Ekran adı girilmedi")
val packageName = screenName.lowercase()

// 1️⃣ Base klasörler
val presentationDir = File("composeApp/src/commonMain/kotlin/com/yusufteker/worthy/screen/$packageName/presentation")
val domainDir = File("composeApp/src/commonMain/kotlin/com/yusufteker/worthy/screen/$packageName/domain/repository")
val dataDir = File("composeApp/src/commonMain/kotlin/com/yusufteker/worthy/screen/$packageName/data/repository")

presentationDir.mkdirs()
domainDir.mkdirs()
dataDir.mkdirs()

// 2️⃣ Presentation dosyaları
val presentationFiles = listOf(
    "$screenName" + "Action.kt" to """
        package com.yusufteker.worthy.screen.$packageName.presentation

        sealed interface ${screenName}Action {
            object Init : ${screenName}Action
            object NavigateBack : ${screenName}Action
        }
    """.trimIndent(),

    "$screenName" + "State.kt" to """
        package com.yusufteker.worthy.screen.$packageName.presentation
        
        import com.yusufteker.worthy.core.presentation.base.BaseState

        data class ${screenName}State(
            override val isLoading: Boolean = false,
            val errorMessage: String? = null
        ): BaseState{
            override fun copyWithLoading(isLoading: Boolean): BaseState {
                return this.copy(isLoading = isLoading)
            }
        }
    """.trimIndent(),

    "$screenName" + "ViewModel.kt" to """
        package com.yusufteker.worthy.screen.$packageName.presentation

        import com.yusufteker.worthy.core.presentation.base.BaseViewModel
        import com.yusufteker.worthy.screen.$packageName.domain.repository.${screenName}Repository

    class ${screenName}ViewModel(
        private val repository: ${screenName}Repository
    ) : BaseViewModel<${screenName}State>(${screenName}State()) {
            fun onAction(action: ${screenName}Action) {
                when (action) {
                    is ${screenName}Action.Init -> {
                        // TODO: Veri yükleme vs.
                    }
                    is ${screenName}Action.NavigateBack -> {
                        navigateBack()
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
        import com.yusufteker.worthy.core.presentation.UiText
        import com.yusufteker.worthy.core.presentation.base.AppScaffold
        import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
        import com.yusufteker.worthy.core.presentation.components.AppTopBar
        import com.yusufteker.worthy.app.navigation.NavigationHandler
        import com.yusufteker.worthy.app.navigation.NavigationModel
        import org.koin.compose.viewmodel.koinViewModel
        import worthy.composeapp.generated.resources.Res
        import worthy.composeapp.generated.resources.add_new_card

        @Composable
        fun ${screenName}ScreenRoot(
            viewModel: ${screenName}ViewModel = koinViewModel(),
            contentPadding: PaddingValues = PaddingValues(),
            onNavigateTo: (NavigationModel) -> Unit,
        ) {
            val state by viewModel.state.collectAsStateWithLifecycle()

            NavigationHandler(viewModel) { model ->
                onNavigateTo(model)
            }

            BaseContentWrapper(state = state) { modifier ->
                ${screenName}Screen(
                    state = state,
                    onAction = viewModel::onAction,
                    contentPadding = contentPadding,
                    modifier = modifier
                )
            }
        }

        @Composable
        fun ${screenName}Screen(
            state: ${screenName}State,
            onAction: (action: ${screenName}Action) -> Unit,
            contentPadding: PaddingValues = PaddingValues(),
            modifier: Modifier = Modifier
        ) {
            AppScaffold(
                modifier = modifier.fillMaxSize().padding(contentPadding),
                topBar = {
                    AppTopBar(
                        title = UiText.StringResourceId(Res.string.add_new_card).asString(),
                        onNavIconClick = { onAction(${screenName}Action.NavigateBack) }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // TODO: Ekran içeriği
                }
            }
        }
    """.trimIndent()
)

presentationFiles.forEach { (filename, content) ->
    val file = File(presentationDir, filename)
    file.writeText(content)
    println("Oluşturuldu: ${file.path}")
}

// 3️⃣ Domain repository
val domainFile = File(domainDir, "${screenName}Repository.kt")
domainFile.writeText(
    """
    package com.yusufteker.worthy.screen.$packageName.domain.repository

    import kotlinx.coroutines.flow.Flow

    interface ${screenName}Repository {
        fun getSomething(): Flow<List<String>> // örnek method
        suspend fun addSomething(item: String)
    }
    """.trimIndent()
)
println("Oluşturuldu: ${domainFile.path}")

// 4️⃣ Data repository implementation
val dataFile = File(dataDir, "${screenName}RepositoryImpl.kt")
dataFile.writeText(
    """
    package com.yusufteker.worthy.screen.$packageName.data.repository

    import com.yusufteker.worthy.screen.$packageName.domain.repository.${screenName}Repository
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.flow.flow
   

    class ${screenName}RepositoryImpl: ${screenName}Repository {
        override fun getSomething(): Flow<List<String>> {
            return flow { emit(emptyList()) } // TODO: Gerçek veri kaynağı
        }

        override suspend fun addSomething(item: String) {
            TODO("Not yet implemented")
        }
    }
    """.trimIndent()
)
println("Oluşturuldu: ${dataFile.path}")


val diFile = File("composeApp/src/commonMain/kotlin/com/yusufteker/worthy/di/Modules.kt") // bu dosyanın adını senin projene göre ayarladım
if (diFile.exists()) {
    val originalContent = diFile.readText()
    val updatedContent = originalContent
        .replaceFirst("package com.yusufteker.worthy.di", """
        package com.yusufteker.worthy.di
        
        import com.yusufteker.worthy.screen.$packageName.domain.repository.${screenName}Repository
        import com.yusufteker.worthy.screen.$packageName.data.repository.${screenName}RepositoryImpl
        import com.yusufteker.worthy.screen.$packageName.presentation.${screenName}ViewModel
    """.trimIndent())
        .replace(
            "val sharedModule = module {",
            """
        val sharedModule = module {

            // 🆕 $screenName eklemeleri
            single<${screenName}Repository> { ${screenName}RepositoryImpl() }
            viewModel { ${screenName}ViewModel(get()) }
        """.trimIndent()
        )

    diFile.writeText(updatedContent)
    println("DI module güncellendi: ${diFile.path}")
} else {
    println("Uyarı: DI module bulunamadı!")
}