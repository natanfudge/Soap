//import kastree.ast.ExtrasMap
//import kastree.ast.Node
//
//class WorkingWriter(
//    app: Appendable = StringBuilder(),
//    extrasMap: ExtrasMap? = null,
//    includeExtraBlankLines: Boolean = extrasMap == null
//) : WriterJavaBridge(app, extrasMap, includeExtraBlankLines) {
//    override fun writeExtrasBeforeBridge(node: Node) {
//        if (extrasMap == null) return
//        super.writeExtrasBeforeBridge(node)
////        writeExtrasBridge(node, extrasMap.extrasWithin(node).takeWhile {
////            it is Node.Extra.Comment
////        })
//    }
//}