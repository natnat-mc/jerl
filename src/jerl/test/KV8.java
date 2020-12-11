package jerl.test;

import jerl.*;
import java.util.*;
import java.util.function.*;
import static jerl.Util8.*;

public final class KV8<K, V> extends EObj<KV8.KVState<K, V>, KV8.KVMsg<K, V>, KV8.KVRet<K, V>, KV8.KVTrans<K, V>> {
	@SuppressWarnings("unchecked")
	public KV8() {
		super(new KVState<>(), (state, msg) ->
			match(msg,
				when(KVPut.class,   p -> new Pair<>(new KVState<>(state, magicCast(p.k), magicCast(p.v)), new KVNone<>()                              )),
				when(KVGet.class,   g -> new Pair<>(state,                                                new KVSome<>(state.map.get(magicCast(g.k))) )),
				when(KVClear.class, c -> new Pair<>(new KVState<>(),                                      new KVNone<>()                              ))
			)
		);
	}

	public void put(K k, V v) {
		_send(new KVPut<>(k, v));
	}

	public void clear() {
		_send(new KVClear<>());
	}

	@SuppressWarnings("unchecked")
	public V get(K k) {
		return (V) match(_send(new KVGet<>(k)),
			when(KVSome.class, s -> s.v)
		);
	}

	public static class KVState<K, V> {
		public final Map<K, V> map = new HashMap<>();

		public KVState(KVState<K, V> prev, K k, V v) { map.putAll(prev.map); map.put(k, v); }
		public KVState() {}
	}

	protected abstract static class KVMsg<K, V> {}
	public static final class KVPut<K, V> extends KVMsg<K, V> {
		public final K k;
		public final V v;
		public KVPut(K k, V v) { this.k = k; this.v = v; }
	}
	public static final class KVClear<K, V> extends KVMsg<K, V> {}
	public static final class KVGet<K, V> extends KVMsg<K, V> {
		public final K k;
		public KVGet(K k) { this.k = k; }
	}

	protected abstract static class KVRet<K, V> {}
	public static final class KVSome<K, V> extends KVRet<K, V> {
		public final V v;
		public KVSome(V v) { this.v = v; }
	}
	public static final class KVNone<K, V> extends KVRet<K, V> {}

	public static interface KVTrans<K, V> extends EObj.ETrans<KVState<K, V>, KVMsg<K, V>, KVRet<K, V>> {}
}
