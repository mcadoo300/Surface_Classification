package planar_model_reduction;

// create a letter class to use in planar models
public class Letter {
	char let; //this is the character of the letter
	boolean inv; //False=clockwise(non inverted) ; True=counterclockwise(inverted)
	
	
	//constructor of a letter
	//
	public Letter(char let, boolean inv) {
		this.let=let;
		this.inv=inv;
	}
	
	public void setLet(Letter newLet) {
		this.let=newLet.getChar();
		this.inv=newLet.getInv();
	}
	//compare letter
	// takes a letter as input, returns true if compared letter is exactly the same
	// both char and orientation
	public boolean compLet(Letter comp) {
		if(this.getChar()==comp.getChar())
			if(this.getInv()==comp.getInv())
				return true;
		
		return false;
	}
	//get char
	public char getChar() {
		return let;
	}
	
	public boolean compInverse(Letter comp) {
		if(this.getChar()==comp.getChar())
			if(this.getInv()!=comp.getInv())
				return true;
		
		return false;
	}
	
	//get inverse
	public boolean getInv() {
		return inv;
	}
	
	//get too string of letter
	public String toString() {
		
		if (this.inv){
			String output;
			output=String.valueOf(let) + "-1";
			output=superscript(output);
			return output;
		}
		else
		{
			return String.valueOf(let);
		}
	}
	
	//invert orientation
	public void invert(){
		this.inv = !this.inv;
	}
	
	//get copy
	public Letter getCopy() {
		Letter newLet = new Letter(this.getChar(),this.getInv());
		return newLet;
	}
	
	//get copy of inverse
	public Letter get_Inverse() {
		Letter newLet = new Letter(this.let,this.inv);
		newLet.invert();
		return newLet;
	}
	//getting superscript
	public static String superscript(String str) {
	    str = str.replaceAll("-1", "`¹");   
	    return str;
	}
}
