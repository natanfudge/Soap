fun<T> buildList(builder : MutableList<T>.() -> Unit) = mutableListOf<T>().apply(builder)
fun<K,V> buildMap(builder : MutableMap<K,V>.() -> Unit) = mutableMapOf<K,V>().apply(builder)