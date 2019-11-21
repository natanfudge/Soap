import kastree.ast.Node
import kastree.psi.Converter

class MigratableWithExtras : Converter.WithExtras() {
    fun migrate(old: Node, new: Node) {
        nodesToPsiIdentities[new] = nodesToPsiIdentities[old]
        nodesToPsiIdentities.remove(old)
    }
}