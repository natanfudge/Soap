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
class Tests{
    private val mappings: MappingSet = TestMappingsReader().read()

    private fun testRemap(testName : String) {
        assertEquals(File("src/test/resources/$testName/Expected.kt").readText().trim(),
            Remapper.remap(File("src/test/resources/$testName/Original.kt"),mappings).trim())

    }
    //TODO: remember to test preserving space and comments!

    @Test
    fun testImport(){
        testRemap("import")
    }

    @Test
    fun testTypeAlias(){
        testRemap("typealias")
    }

    @Test
    fun testImportedType(){
        testRemap("importedType")
    }
}

