package net.evlikat.hexatrix.entities

/**
 * Created by RSProkhorov on 18.03.2015.
 */
internal open class CompositeIterator<T>(collection: Iterator<List<T>?>) : MutableIterator<T> {

    private val collectionIt: Iterator<List<T>?> = collection
    private var subCollectionIt: Iterator<T>? = null

    open fun afterLine() {
        // override me
    }

    override fun hasNext(): Boolean {
        return if (subCollectionIt == null || !subCollectionIt!!.hasNext()) {
            collectionIt.hasNext()
        } else true
    }

    override fun next(): T {
        if (subCollectionIt == null || !subCollectionIt!!.hasNext()) {
            if (subCollectionIt != null) {
                afterLine()
            }
            val nextLine = collectionIt.next()
            subCollectionIt = nextLine!!.iterator()
        }
        return subCollectionIt!!.next()
    }

    override fun remove() {}
}