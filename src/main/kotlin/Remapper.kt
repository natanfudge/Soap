import kastree.ast.Node
import kastree.ast.Visitor
import kastree.ast.Writer
import kastree.psi.Converter
import kastree.psi.Parser
import org.cadixdev.lorenz.MappingSet
import java.io.File

internal val Node.Import.fullPath get() = names.joinToString("/")

object Remapper {


    fun remap(codePath: File, mappings: MappingSet) = remap(codePath.readText(), mappings)

    //TODO: Can possibly optimize this by getting imports on-the-fly because they are at the top of the file.
    // Need to see the performance impact of this.
    private fun getImports(file: Node.File): Map<String, String> = buildMap {
        Visitor.visit(file) { node, _ ->
            if (node is Node.Import) {
                put(node.names.first(), node.fullPath)
            }
        }
    }

    fun remap(code: String, mappings: MappingSet): String {
        val extras = Converter.WithExtras()
        val file = Parser(extras).parseFile(code, throwOnError = false)
        val newFile = FileContext(getImports(file), mappings).remap(file)

        return Writer.write(newFile)
    }


}