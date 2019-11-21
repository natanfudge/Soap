//import kastree.ast.ExtrasMap;
//import kastree.ast.Node;
//import kastree.ast.Writer;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//
//public class WriterJavaBridge extends Writer {
//    public WriterJavaBridge(@NotNull Appendable app, @Nullable ExtrasMap extrasMap, boolean includeExtraBlankLines) {
//        super(app, extrasMap, includeExtraBlankLines);
//    }
//
//    @Override
//    final protected void writeExtrasBefore(@NotNull Node $self) {
//        writeExtrasBeforeBridge($self);
//    }
//
//    protected void writeExtrasBeforeBridge(@NotNull Node $self) {
//        super.writeExtrasBefore($self);
//    }
//
//    @Override
//    final protected void writeExtras(@NotNull Node $self, @NotNull List<? extends Node.Extra> extras) {
//        writeExtrasBridge($self, extras);
//    }
//
//
//    protected void writeExtrasBridge(@NotNull Node $self, @NotNull List<? extends Node.Extra> extras) {
//        super.writeExtras($self, extras);
//    }
//}
