package gal.ast;

public class Field extends Argument {

	Dollar dollar;
	Terminal field_name;

	public Field(Dollar dollar, Terminal field_name) {
		this.dollar = dollar;
		this.field_name = field_name;
	}

	@Override
	public String toString() {
		return String.format("%s.%s", dollar, field_name);
	}

	@Override
	Object accept(IVisitor visitor) {
		Object o_dollar = dollar.accept(visitor);
		Object o_field_name = field_name.accept(visitor);
		return visitor.build(this, o_dollar, o_field_name);
	}

}
