package utilities;
import java.util.EventObject;


public class UIEvent extends EventObject {
	public enum Type{
		//Was kann durch das Event übermittlet werden?
		StartGame, LoadGame, EndGame, StartSet, EndSet
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Type type;
	private String[] args;
	
	public UIEvent(Object source, Type t, String[] args){
		super(source);
		type = t;
		this.args = args;		
	}

	@Override
	public Object getSource() {
		// TODO Auto-generated method stub
		return super.getSource();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	} 
	
	public Type getType() {
		return type;
	}

	public String[] getArgs() {
		return args;
	}
	
	

}
