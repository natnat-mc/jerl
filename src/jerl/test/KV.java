package jerl.test;

import jerl.*;
import java.util.*;

public final class KV<K, V> extends EObj<KV.KVState<K, V>, KV.KVMsg<K, V>, KV.KVRet<K, V>, KV.KVTrans<K, V>> {
	public KV() {
		super(new KVState<K, V>(), (state, msg) -> {
			if(msg instanceof KVPut<K, V> p) {
				return new Pair<>(new KVState<>(state, p.k, p.v), new KVNone<>());
			} else if(msg instanceof KVGet<K, V> g) {
				return new Pair<>(state, new KVSome<>(state.map.get(g.k)));
			} else if(msg instanceof KVClear<K, V> c) {
				return new Pair<>(new KVState<>(), new KVNone<>());
			}
			return null;
		});
	}

	public void put(K k, V v) {
		_send(new KVPut<>(k, v));
	}

	public void clear() {
		_send(new KVClear<>());
	}

	public V get(K k) {
		KVRet<K, V> r = _send(new KVGet<>(k));
		if(r instanceof KVSome<K, V> s) {
			return s.v;
		} else {
			throw new IllegalStateException();
		}
	}

	public static class KVState<K, V> {
		public final Map<K, V> map = new HashMap<>();

		public KVState(KVState<K, V> prev, K k, V v) {
			map.putAll(prev.map);
			map.put(k, v);
		}
		public KVState() {}
	}

	public abstract static sealed class KVMsg<K, V> permits KVPut, KVClear, KVGet {}
	public static final class KVPut<K, V> extends KVMsg<K, V> {
		public final K k;
		public final V v;
		public KVPut(K k, V v) {
			this.k = k;
			this.v = v;
		}
	}
	public static final class KVClear<K, V> extends KVMsg<K, V> {}
	public static final class KVGet<K, V> extends KVMsg<K, V> {
		public final K k;
		public KVGet(K k) {
			this.k = k;
		}
	}

	public abstract static sealed class KVRet<K, V> permits KVSome, KVNone {}
	public static final class KVSome<K, V> extends KVRet<K, V> {
		public final V v;
		public KVSome(V v) {
			this.v = v;
		}
	}
	public static final class KVNone<K, V> extends KVRet<K, V> {}

	public static interface KVTrans<K, V> extends EObj.ETrans<KVState<K, V>, KVMsg<K, V>, KVRet<K, V>> {}
}
