import org.cadixdev.lorenz.MappingSet
import org.cadixdev.lorenz.io.MappingsReader
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals


class TestMappingsReader : MappingsReader() {
    override fun read(mappings: MappingSet): MappingSet {
        val mapping = mappings.getOrCreateClassMapping("net/obf/TestObf").setDeobfuscatedName("deobf/TestDeobf")

        return mappings
    }

    override fun close() {
    }

}

//class A<T>{
//    inner class B<T>
//}
////TODO: test this situation
//val x : A<Int>.B<String>? = null
class Tests {
    private val mappings: MappingSet = TestMappingsReader().read()

    private fun testRemap(testName: String) {
        val dir = "src/test/resources/$testName/"
        val result = Remapper.remap(File("$dir/Original.kt"), mappings).trim().replace("\n\n","\n")
        File("$dir/tempResult.kt").writeText(result)
        assertEquals(
            File("$dir/Expected.kt").readText().trim(),
            result
        )

    }
    //TODO: remember to test preserving space and comments!

    @Test
    fun testImport() {
        testRemap("import")
    }

    @Test
    fun testTypeAlias() {
        testRemap("typealias")
    }

    @Test
    fun testImportedType() {
        testRemap("importedType")
    }
}

