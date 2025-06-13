package gal.ast.export;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import engine.brain.FSM;
import gal.ast.AST;
import gal.ast.Actions;
import gal.ast.Automaton;
import gal.ast.Behaviour;
import gal.ast.BinaryOp;
import gal.ast.Category;
import gal.ast.Condition;
import gal.ast.Direction;
import gal.ast.Dollar;
import gal.ast.Field;
import gal.ast.FunCall;
import gal.ast.IVisitor;
import gal.ast.Key;
import gal.ast.Mode;
import gal.ast.Opposite;
import gal.ast.State;
import gal.ast.Terminal;
import gal.ast.Transition;
import gal.ast.UnaryOp;
import gal.ast.Underscore;
import gal.ast.Value;

/**
 * This class implements the IVisitor interface to convert an AST into FSM
 * objects.
 */
public class Ast2FSM implements IVisitor {
	private Map<String, FSM> automataMap = new HashMap<>();
	private List<FSM> fsmList = new LinkedList<>();

	// ** Terminal and simple elements builders **
	@Override
	public Object build(Terminal name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object build(Category cat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object build(Direction dir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object build(Key key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object build(Value v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object build(Underscore u) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object build(Dollar d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object build(Field f, Object dollar, Object field_name) {
		// TODO Auto-generated method stub
		return null;
	}

	// OPPOSITE

	@Override
	public void enter(Opposite o) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit(Opposite o) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object build(Opposite o, Object operand) {
		// TODO Auto-generated method stub
		return null;
	}

	// FUNCALL

	@Override
	public void enter(FunCall funcall) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(FunCall funcall) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit(FunCall funcall) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object build(FunCall funcall, List<Object> arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	// BINARY OP
	@Override
	public void enter(BinaryOp binop) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(BinaryOp binop) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit(BinaryOp binop) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object build(BinaryOp binop, Object left, Object right) {
		// TODO Auto-generated method stub
		return null;
	}

	// UNARY OP

	@Override
	public void enter(UnaryOp unop) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit(UnaryOp unop) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object build(UnaryOp unop, Object expression) {
		// TODO Auto-generated method stub
		return null;
	}

	// STATE

	@Override
	public Object build(State state) {
		// TODO Auto-generated method stub
		return null;
	}

	// MODE

	@Override
	public void enter(Mode mode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Mode mode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit(Mode mode) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object build(Mode mode, Object source_state, Object behaviour) {
		// TODO Auto-generated method stub
		return null;
	}

	// BEHAVIOUR

	@Override
	public Object build(Behaviour behaviour, List<Object> transitions) {
		// TODO Auto-generated method stub
		return null;
	}

	// CONDITION

	@Override
	public void enter(Condition condition) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit(Condition condition) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object build(Condition condition, Object expression) {
		// TODO Auto-generated method stub
		return null;
	}

	// ACTION

	@Override
	public void enter(Actions action) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Actions action) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit(Actions action) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object build(Actions action, String operator, List<Object> funcalls) {
		// TODO Auto-generated method stub
		return null;
	}

	// TRANSITION
	@Override
	public void enter(Transition transition) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Transition transition) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit(Transition transition) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object build(Transition transition, Object condition, Object action, Object target_state) {
		// TODO Auto-generated method stub
		return null;
	}

	// AUTOMATON
	@Override
	public void enter(Automaton automaton) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Automaton automaton) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit(Automaton automaton) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object build(Automaton automaton, Object initial_state_name, List<Object> modes) {
		// TODO Auto-generated method stub
		return null;
	}

	// AST
	@Override
	public void enter(AST ast) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit(AST ast) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object build(AST ast, List<Object> automata) {
		// Clear the FSM list and populate it with automata
		fsmList.clear();
		for (Object automaton : automata)
			if (automata instanceof FSM)
				fsmList.add((FSM) automaton);

		return fsmList;
	}
}
