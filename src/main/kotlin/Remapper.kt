import kastree.ast.MutableVisitor
import kastree.ast.Node
import kastree.ast.Writer
import kastree.psi.Converter
import kastree.psi.Parser
import org.cadixdev.lorenz.MappingSet
import java.io.File



object Remapper {
    private fun MappingSet.remapClass(obf : String) : String? = computeClassMapping(obf).orElse(null)?.fullDeobfuscatedName


    fun remap(codePath: File, mappings: MappingSet) = remap(codePath.readText(), mappings)
    fun remap(code: String, mappings: MappingSet): String {
        val extras = Converter.WithExtras()
        val file = Parser(extras).parseFile(code)
        val newFile = MutableVisitor.preVisit(file) { node, _ ->
            when (node) {
                is Node.Import -> node.copy(
                        names = mappings.remapClass(node.names.joinToString("/"))
                           ?.split("/") ?: node.names
                    )

//            is Node.TypeRef.Dynamic -> {
//                node.copy()
//            }
                is Node.TypeRef.Simple ->{
//                    val remappedType = mappings.remapClass(node.pieces.joinToString("/"){it.name})?.split("/")
//                    if(remappedType == null)  node
//                    else{
//                        node.copy(pieces = node.pieces.zip(remappedType).map { (piece,remappedType) })
//                    }

//                    node.copy(pieces = ?.split("/").map {  }
//                        ?: node.pieces)

                    node
                }
                else -> node
            }

        }

        return Writer.write(newFile)
    }


}