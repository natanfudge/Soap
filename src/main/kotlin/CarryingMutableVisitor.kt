import kastree.ast.MutableVisitor
import kastree.ast.Node

/**
 * Carries over tags from old nodes, this makes comment stay.
 */
open class CarryingMutableVisitor : MutableVisitor() {
    override fun <T> T.origOrChanged(orig: T, ref: ChangedRef): T {
        return if (ref.changed) this.apply { if (this is Node && orig is Node) this.tag = orig.tag } else orig
    }

    companion object {
        fun <T : Node> preVisit(v: T, fn: (v: Node?, parent: Node) -> Node?): T = object : CarryingMutableVisitor() {
            override fun <T : Node?> preVisit(v: T, parent: Node): T = fn(v, parent) as T
        }.visit(v, v)
    }

}