import kastree.ast.MutableVisitor
import kastree.ast.Node

abstract class MigratingMutableVisitor(private val extras: MigratableWithExtras) : MutableVisitor() {
    override fun <T> T.origOrChanged(orig: T, ref: ChangedRef): T = if (ref.changed) {
        if (this is Node && orig is Node) extras.migrate(orig, this)
        this
    } else orig

    companion object {
        fun <T : Node> preVisit(extras: MigratableWithExtras, v: T, fn: (v: Node?, parent: Node) -> Node?): T
                = object : MigratingMutableVisitor(extras) {
            override fun <T : Node?> preVisit(v: T, parent: Node): T = fn(v, parent) as T
        }.visit(v, v)
    }
}