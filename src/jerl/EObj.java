package jerl;

public abstract class EObj<S, M, R, T extends EObj.ETrans<S, M, R>> {
	private S state;
	private final T trans;

	protected EObj(S state, T trans) {
		this.state = state;
		this.trans = trans;
	}

	protected R _send(M msg) {
		Pair<S, R> p = trans.fn(state, msg);
		state = p.a;
		return p.b;
	}

	@FunctionalInterface
	public static interface ETrans<S, M, R> {
		Pair<S, R> fn(S state, M msg);
	}
}
