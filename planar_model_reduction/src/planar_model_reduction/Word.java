package planar_model_reduction;

// this is where the planar model is stored as a word
// composed of an array of letters

public class Word {
	Letter[] model; // array of Letter which represents the word
	boolean surface; // boolean value, is surface or not a surface. determined from model

	// Constructor methods listed below //

	// create null word
	public Word() {
		this.model = null;
		this.surface = false;
	}

	// create new word from letterArray
	public Word(Letter[] letterArray) {
		this.model = letterArray;
		this.surface = this.is_surface();
	}

	// create new word from existing word
	public Word(Word originalWord) {
		this.model = originalWord.get_model();
		this.surface = originalWord.get_surface();
	}

	// end of constructor methods //

	// set methods below //

	// set word with new word
	public void setWord(Word newWord) {
		this.model = newWord.get_model();
		this.surface = newWord.get_surface();
	}

	// set word with Letter array
	public void setModel(Letter[] newLetterArray) {
		this.model = newLetterArray;
		this.surface = this.is_surface();
	}

	// end of set methods //

	// get methods below //

	// returns copy of model with new reference
	public Letter[] get_model() {
		Letter[] copy1 = new Letter[this.model.length];
		for (int i = 0; i < this.model.length; i++) {
			Letter newLet = this.model[i].getCopy();
			copy1[i] = newLet;
		}
		return copy1;
	}

	// returns a copy of this word
	public Word get_copy() {
		return new Word(this);
	}

	// returns surface value
	public boolean get_surface() {
		return this.surface;
	}

	// end of get methods //

	// blow are methods to determine if model is a surface or not //

	// check if word length is even
	public boolean isEven() {
		if ((this.model.length % 2) != 0)
			return false;
		else
			return true;
	}

	// get a string which contains all characters in word
	public String get_string_model() {
		String return_val = "";
		for (int i = 0; i < this.model.length; i++) {
			return_val = return_val + String.valueOf(this.model[i].getChar());
		}
		return return_val;
	}

	// check if letters exist in pairs
	public boolean allPairs() {
		// if model length is not even it is not surface
		if (!this.isEven())
			return false;

		// create the string to store characters of word
		String model_word = this.get_string_model();
		int length1 = model_word.length();

		// create loop remove characters from the string
		// if the length does not decrease by 2 return false
		while (model_word.length() > 0) {
			model_word = model_word.replaceAll(String.valueOf(model_word.charAt(0)), "");
			int new_length = model_word.length();
			// if the length does not decrease by 2 return false
			if (length1 - new_length != 2)
				return false;
			length1 = new_length;
		}
		return true;

	}

	// returns if word is surface
	public boolean is_surface() {
		return this.allPairs();
	}

	// end of is surface block //

	
	// blow are the 5 basic operations we may make on a word //

	// flip rule
	// sets this model as this word read backward with orientation of Letters
	// reversed
	public void flip() {

		Word dummyWord = this.get_copy();
		Letter[] newWord = this.get_model();

		// start at end of original word
		int flipdex = this.model.length - 1;

		for (int i = flipdex; i > -1; i--) {
			newWord[flipdex - i] = (dummyWord.model[i].get_Inverse());
		}

		this.model = newWord;
	}

	// cycle rule
	// start reading the model from specified index
	public void cycle(int newStart) {
		Letter[] newWord = new Letter[this.model.length];
		// this loop works by letting (i - newStart) be the position of the new model
		// and i or (i - model.length) is the position being read from the original
		// model
		for (int i = newStart; i < (this.model.length + newStart); i++) {
			if (i < this.model.length)
				newWord[i - newStart] = this.model[i];
			else// when i is larger than or equal to the model length go to i - model.length
				newWord[i - newStart] = this.model[i - this.model.length];
		}
		// replace model
		this.model = newWord;
	}

	// sphere rule
	// will try to remove sphere starting at index
	public void sphereRule(int index) {
		// make sure the letters are adjacent
		if (this.findMatch(index) == (index + 1)) { // make sure letters have opposite orientations
			if (this.model[index].inv != this.model[index + 1].inv) {
				// create new array with length-2
				Letter[] newWord = new Letter[this.model.length - 2];
				// add all characters before sphere
				for (int i = 0; i < index; i++) {
					newWord[i] = this.model[i];
				}
				// add all chacters after sphere
				for (int i = (index + 2); i < this.model.length; i++) {
					newWord[i - 2] = this.model[i];
				}
				// convert current model to string
				String highlight = this.toString();
				// get position of sphere
				int pos = highlight.indexOf(this.model[index].getChar());
				// insert parentheses to highlight rule application
				highlight = addChar(highlight, '(', pos);
				highlight = addChar(highlight, ')', pos + 5);// position between ( and ) is always 5
				System.out.println("= " + highlight);
				this.model = newWord;// update model
				System.out.println("[Sphere rule]~ " + this.toString());
			} else
				System.out.println("Letters have the same orientaion. Not a sphere.");

		} else
			System.out.println("Letters not adjacent.");

	}

	// cylinder rule
	// will first check if the the specified index(cyclStrt) corrisponds to a
	// matching Letter with opposite orientation
	public void cylinderRule(int cyclStrt, int strtRd) {
		// get position of cyclinder end
		int cyclEnd = this.findMatch(cyclStrt);
		// if opposite orientation
		if (!(this.model[cyclStrt].inv == this.model[cyclEnd].inv)) {
			int invCounter = 0;// track number of inverted number(for string manipulation)
			// determine how many letters are within in the cylinder
			int blockLength = 0;
			if (cyclStrt != 0)
				blockLength = Math.abs(cyclEnd - cyclStrt - 1);
			else
				blockLength = cyclEnd - 1;
			if (blockLength < 2)// if there is only a singleton then its no use. break
				;// pass;
			else {
				// get sub-word
				// start reading from strtRd index
				Letter[] block = new Letter[blockLength];
				for (int i = strtRd; i < strtRd + blockLength; i++) {
					if (i < cyclEnd) {
						block[i - strtRd] = this.model[i];
						if (this.model[i].inv == true)
							invCounter++;
					} else {
						block[i - strtRd] = this.model[i - blockLength];
						if (this.model[i - blockLength].inv == true)
							invCounter++;
					}

				}
				// this prints procedure and updates the model
				String highlight = this.toString();

				int first = 0;
				if (this.model[cyclStrt].inv == false)
					first = highlight.indexOf(this.model[cyclStrt].getChar()) + 1;
				else
					first = highlight.indexOf(this.model[cyclStrt].getChar()) + 3;
				int second = (first + 1 + blockLength + (2 * invCounter));

				highlight = addChar(highlight, '(', first);
				highlight = addChar(highlight, ')', second);

				System.out.println("= " + highlight);

				Word dummyWord = this.get_copy();
				for (int i = 0; i < blockLength; i++) {

					dummyWord.model[i + (cyclStrt + 1)] = block[i];
				}
				this.setWord(dummyWord);
				highlight = this.toString();
				highlight = addChar(highlight, '(', first);
				highlight = addChar(highlight, ')', second);
				System.out.println("[Cylinder rule]~ " + highlight);
			}

		}

		else
			System.out.println("Invalid since letters have the same orientaion.");

	}

	
	//mobius rule
	public void mobiusRule(int start) {
			int mobEnd = this.findMatch(start);
				int invCounter=0;// inverse letter counter for string manipulation
				if((this.model[start].inv==this.model[mobEnd].inv)) //compare orientation
				{
					if(!(mobEnd==(start+1))) //compare position
					{
						//get block length of letters inside mobius band
						int blockLength=0;
						if(start!=0)
							blockLength=Math.abs(mobEnd-start-1);
						else
							blockLength=mobEnd-1;
						
						//get inverse of block
						Letter [] block = new Letter[blockLength];
						for(int i=(mobEnd-1);i>start;i--) {
							if(this.model[i].inv==true)
								invCounter++;
							// put copy of Letter with orientation reversed into block
							block[mobEnd-1-i]=this.model[i].get_Inverse();
						}
						
						//make dummy by copying original
						Word dummyWord= this.get_copy();
						//change dummy to new model
						dummyWord.model[start+1] = this.model[mobEnd].getCopy();
						for(int i=0;i<(blockLength);i++) {
							dummyWord.model[start+2+i].setLet(block[i]);
						}
						
						String highlight = this.toString();
						
						int first=0;
						if(this.model[start].inv ==false)
							first=highlight.indexOf(this.model[start].getChar())+1;
						else
							first=highlight.indexOf(this.model[start].getChar())+3;
						int second= (first+1+blockLength + (2*invCounter));
						
						highlight=addChar(highlight,'(',first);
						highlight=addChar(highlight,')',second);
						
						System.out.println("= " + highlight);//original
						this.setWord(dummyWord);// update this word
						highlight=this.toString();
						
						if(this.model[start].inv==false)
							first=highlight.indexOf(this.model[start].getChar())+2;
						else
							first=highlight.indexOf(this.model[start].getChar())+6;
						
						highlight=addChar(highlight,'(',first);
						second=(first+1+blockLength + (2*(blockLength-invCounter)));
						highlight=addChar(highlight,')',second);
						System.out.println("[Mobius rule]~ " + highlight );
					}
					
					
			}
			
			
			else
				System.out.println("Invalid since letters have the same orientaion.");

	}
	
	// end of basic rules //
	
	
	
	// various helper functions listed below //

	// find the index of matching Letter
	public int findMatch(int index) {
		// key value is the character at said index
		char key = this.model[index].getChar();
		for (int i = 0; i < this.model.length; i++) {
			if (this.model[i].getChar() == key) {
				if (i != index)
					return i;// return first instance of key value which is not the index
			}

		}
		return -1;
	}

	// return string of word
	public String toString() {
		String output = null;
		for (int i = 0; i < this.model.length; i++) {
			if (i == 0)
				output = model[i].toString();
			else
				output = output + model[i].toString();
		}
		return output;
	}

	// returns length of model
	public int getLength() {
		return this.model.length;
	}
	// add char to string
	// made to simplify syntax
	public String addChar(String str, char ch, int position) {
		return str.substring(0, position) + ch + str.substring(position);
	}
}
