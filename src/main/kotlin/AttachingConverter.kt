import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import kastree.ast.Node
import kastree.psi.Converter

private data class NodeExtras(
    var extrasBefore: List<Node.Extra>? = null,
    var extrasWithin: List<Node.Extra>? = null,
    var extrasAfter: List<Node.Extra>? = null
)

/**
 * Attaches comments to nodes instead of into an extras map. This allows keeping comments information when mutating.
 */
class AttachingConverter : Converter.WithExtras() {
    private val Node.extras get() = tag as NodeExtras
    private val Node.extrasOrNull get() = tag as? NodeExtras

    private fun Node.attachBefore(toAttach: List<Node.Extra>) {
        if (this.tag == null) tag = NodeExtras()
        extras.extrasBefore = toAttach
    }

    private fun Node.attachWithin(toAttach: List<Node.Extra>) {
        if (this.tag == null) tag = NodeExtras()
        extras.extrasWithin = toAttach
    }

    private fun Node.attachAfter(toAttach: List<Node.Extra>) {
        if (this.tag == null) tag = NodeExtras()
        extras.extrasAfter = toAttach
    }

    override fun extrasAfter(v: Node): List<Node.Extra> = v.extrasOrNull?.extrasAfter ?: listOf()

    override fun extrasBefore(v: Node): List<Node.Extra> = v.extrasOrNull?.extrasBefore ?: listOf()

    override fun extrasWithin(v: Node): List<Node.Extra> = v.extrasOrNull?.extrasWithin ?: listOf()

    override fun onNode(node: Node, elem: PsiElement) {
        // We ignore whitespace and comments here to prevent recursion
        if (elem is PsiWhiteSpace || elem is PsiComment) return

        // Since we've never done this element before, grab its extras and persist
        val (beforeElems, withinElems, afterElems) = nodeExtraElems(elem)
        convertExtras(beforeElems).map {
            // As a special case, we make sure all non-block comments start a line when "before"
            if (it is Node.Extra.Comment && !it.startsLine && it.text.startsWith("//")) it.copy(startsLine = true)
            else it
        }.also {
            if (it.isNotEmpty()) node.attachBefore(it)
        }
        convertExtras(withinElems).also { if (it.isNotEmpty()) node.attachWithin(it) }
        convertExtras(afterElems).also { if (it.isNotEmpty()) node.attachAfter(it) }
    }
}