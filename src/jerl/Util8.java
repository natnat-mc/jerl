package jerl;

import java.util.function.*;

public final class Util8 {
	private Util8() {} // no instances

	@SuppressWarnings("unchecked")
	public static <S, R> R match(S obj, When<S, ?, R>... cases) {
		Class<?> c = obj.getClass();
		for(Util8.When<S, ?, R> w: cases) {
			if(w.s.isAssignableFrom(c)) return w.apply(obj);
		}
		throw new NoMatchException();
	}

	@SuppressWarnings("unchecked")
	public static <S, R> R match(S obj, Class<R> r, When<S, ?, R>... cases) {
		return match(obj, cases);
	}

	public static final class When<S, T, R> {
		public final Class<T> s;
		public final Function<T, R> f;
		public When(Class<T> s, Function<T, R> f) {
			this.s = s;
			this.f = f;
		}
		public R apply(S v) {
			return this.f.apply(this.s.cast(v));
		}
	}
	public static final <S, T, R> When<S, T, R> when(Class<T> s, Function<T, R> f) {
		return new When<S, T, R>(s, f);
	}

	@SuppressWarnings("serial")
	public static class NoMatchException extends RuntimeException {}

	@SuppressWarnings("unchecked")
	public static <S, D> D magicCast(S s) {
		return (D) s;
	}
}
