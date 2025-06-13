package gal.ast;

public class Dollar extends Argument {
	int i;

	public Dollar(int i) {
		this.i = i;
	}

	@Override
	public String toString() {
		return String.format("$%d", i);
	}

	@Override
	Object accept(IVisitor visitor) {
		return visitor.build(this);
	}
}
