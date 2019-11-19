import kastree.ast.MutableVisitor
import kastree.ast.Node
import org.cadixdev.lorenz.MappingSet

internal data class Import(val shortClassName: String, val fullyQualifiedPath: String)


internal data class FileContext(private val imports: Map<String, String>, private val mappings: MappingSet) {
    private fun remapClass(obf: String): String? =
        mappings.computeClassMapping(obf).orElse(null)?.fullDeobfuscatedName

    private val Node.TypeRef.Simple.fullyQualifiedName: String
        get() = if (isImported()) imports[pieces.first().name] ?: piecesCombined
        else piecesCombined

    private fun Node.TypeRef.Simple.isImported() = pieces.size == 1

    private val Node.TypeRef.Simple.piecesCombined get() = pieces.joinToString("/") { it.name }

    fun remap(code: Node.File): Node.File = MutableVisitor.preVisit(code) { node, _ ->
        when (node) {
            is Node.Import -> node.copy(
                names = remapClass(node.fullPath)
                    ?.split("/") ?: node.names
            )

            is Node.TypeRef.Simple -> {
                val fullPath = node.fullyQualifiedName
                val remapped = remapClass(fullPath) ?: return@preVisit node

                val remappedPieces = remapped.split("/")
                        // When the type is imported we only replace with the imported class name
                    .let { if (node.isImported()) listOf(it.last()) else it }

                node.copy(pieces = node.pieces.zip(remappedPieces).map { (oldPiece, newPieceName) ->
                    oldPiece.copy(name = newPieceName)
                })

            }
            else -> node
        }


    }
}