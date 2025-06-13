package gal.ast;

public class Opposite extends Argument {

	Argument operand;
	
	public Opposite(Argument operand){
		this.operand = operand;
	}
	
	@Override
	public String toString() {
		return String.format("-%s", operand);
	}

	@Override
	Object accept(IVisitor visitor) {
		visitor.enter(this);
		Object o_operand = operand.accept(visitor);
		visitor.exit(this);
		return visitor.build(this, o_operand);
	}
	
}
