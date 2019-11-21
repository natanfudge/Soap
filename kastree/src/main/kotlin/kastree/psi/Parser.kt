package kastree.psi

import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiManager
import com.intellij.testFramework.LightVirtualFile
import kastree.ast.Node
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType

open class Parser(val converter: Converter = Converter) {
    protected val proj by lazy {
        KotlinCoreEnvironment.createForProduction(
            Disposer.newDisposable(),
            CompilerConfiguration(),
            EnvironmentConfigFiles.JVM_CONFIG_FILES
        ).project
    }

    fun parseFile(code: String, throwOnError: Boolean = true): Node.File {
        val psi = parsePsiFile(code)
        return converter.convertFile(
            psi.also { file ->
                if (throwOnError) file.collectDescendantsOfType<PsiErrorElement>().let {
                    if (it.isNotEmpty()) throw ParseError(file, it)
                }
            })
    }

    private fun parsePsiFile(code: String): KtFile =
        PsiManager.getInstance(proj).findFile(LightVirtualFile("temp.kt", KotlinFileType.INSTANCE, code)) as KtFile

    data class ParseError(
        val file: KtFile,
        val errors: List<PsiErrorElement>
    ) : IllegalArgumentException("Failed with ${errors.size} errors, first: ${errors.first().errorDescription}")

    companion object : Parser() {
        init {
            // To hide annoying warning on Windows
            System.setProperty("idea.use.native.fs.for.win", "false")
        }
    }
}