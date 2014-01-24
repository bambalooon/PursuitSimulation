package pursuitsimulation.util;

import pursuitsimulation.Crossing;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 24.01.14
 * Time: 22:39
 * To change this template use File | Settings | File Templates.
 */
public class Pair<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;
    public Pair(K k, V v) {
        this.key = k;
        this.value = v;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V v) {
        V old = this.value;
        value = v;
        return old;
    }

}
