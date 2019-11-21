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
        val actual = Remapper.remap(
            File("$dir/Original.kt").readText().replace("\r", ""), mappings
        ).trim()
        val expected = File("$dir/Expected.kt").readText().trim().replace("\r", "")
        File("$dir/tempResult.kt").writeText(actual)
        assertEquals(expected, actual)

    }

    @Test
    fun `As import`() {
        testRemap("import")
    }

    @Test
    fun `As a typealias`() {
        testRemap("typealias")
    }

    @Test
    fun `As an imported type`() = testRemap("importedType")


    @Test
    fun `Constructed and imported type`() = testRemap("constructedImport")

    @Test
    fun `Constructed type`() = testRemap("constructed")

    @Test
    fun `Typed field`() = testRemap("field")

    @Test
    fun `As generic parameter of type`() = testRemap("generics")

    @Test
    fun `As generic parameter of function`() = testRemap("genericsCall")

    @Test
    fun `Imported as`() = testRemap("importAs")

    @Test
    fun `Inherited as an open class`() = testRemap("inheritClass")

    @Test
    fun `Implemented as an interface`() = testRemap("inheritInterface")

    @Test
    fun `Inner obfuscated class`() = testRemap("inner")

    @Test
    fun `Complex inner classes and generics`() = testRemap("innerGenerics")

    @Test
    fun `As parameter of lambda`() = testRemap("lambdaparam")

    @Test
    fun `Multiple conversions in one file`() = testRemap("multiple")

    @Test
    fun `As parameter of function`() = testRemap("parameter")

    @Test
    fun `When a static method is called on the type`() = testRemap("static")
}

