package planar_model_reduction;

// basic objects all surfaces are built from
enum Base {
	SPHERE, PLANE, TORUS
}
// reduction object is "superficial" parent to word
// this is where the real algorithm for reducing the word is placed
// this was done to try and not overload any one class with too many methods


// a reminder to how planar models(words) are generated:
//  		letters put into an array which creates a model
//			a model is then used to create a word
//			the reduction object then takes the word and reduces it to its normal form

public class Reduction_Object {
	Word surface; // the surface we attend to operate on

	public Reduction_Object(Word word1) {
		surface = word1;
	}

	// the below functions allow for a search of basic manipulations //

	// these will serve as the first round checks for the reduction algorithm
	/* Includes: sphere, torus, projective plane, cylinder and mobius band */

	// find first instance of sphere
	// if no sphere is found return -1
	// includes fringe case check where the sphere is at the start and end of the
	// model
	public int sphereSearch() {
		if(this.surface.model.length == 0)
			return -1;
		for (int i = 0; i < this.surface.getLength() - 1; i++) {
			if (this.surface.model[i].getChar() == this.surface.model[i + 1].getChar())
				if (this.surface.model[i].inv != this.surface.model[i + 1].inv)
					return i;

		}
		// check for fringe case
		if (this.surface.model[0].getChar() == this.surface.model[this.surface.getLength() - 1].getChar())
			if (this.surface.model[0].getInv() != this.surface.model[this.surface.getLength() - 1].inv) {
				// cycle word to start at end
				this.surface.cycle(this.surface.getLength() - 1);
				return 0;// return 0 since sphere has been moved to the front
			}
		return -1;
	}

	// find first torus
	// does NOT find fringe cases where a torus is within a cylinder manipulation
	// does handle edge cases when a torus is "warpped" around the start and end
	public int torusSearch() {
		if(this.surface.model.length == 0)
			return -1;
		if (this.surface.getLength() > 3) {

			// torus will store the new model
			Letter[] torus_check = new Letter[4];
			Word new_surface = new Word();

			// check every possible index for torus
			for (int i = 0; i < this.surface.getLength() - 3; i++) {
				for (int k = 0; k < 4; k++) {
					torus_check[k] = this.surface.model[i + k];
				}
				new_surface.setModel(torus_check); // turn new block into word
				Reduction_Object torus_block = new Reduction_Object(new_surface);// turn word into red_obj
				if (torus_block.isTorus())// check if torus
					return i;

			}
			// check all possible edge cases
			if (this.surface.getLength() > 4) {
				for (int i = (this.surface.getLength() - 3); i < this.surface.getLength(); i++) {
					for (int j = i; j < (i + 4); j++) {
						torus_check[j - i] = this.surface.model[j % this.surface.getLength()];
					}
					new_surface.setModel(torus_check);
					Reduction_Object torus_block = new Reduction_Object(new_surface);// turn word into red_obj

					if (torus_block.isTorus()) {
						this.surface.cycle(i);
						return 0;
					}
				}
			}
		}
		return -1;
	}

	// find first instance of projective plane
	public int planeSearch() {
		if(this.surface.model.length == 0)
			return -1;
		for (int i = 0; i < this.surface.getLength(); i++) {
			for (int j = i + 1; j < this.surface.getLength(); j++) {
				if (this.surface.model[i].compLet(this.surface.model[j]))
					if ((Math.abs((i - j)) == 1))
						return i;
			}
		}
		return -1;

	}

	// search for cycle
	// returns min index for cylinder
	// only returns cylinders whose indside blocks are larger than 1
	public int cyclinderSearch() {
		if(this.surface.model.length == 0)
			return -1;
		for (int i = 0; i < this.surface.getLength(); i++) {
			for (int j = i + 1; j < this.surface.getLength(); j++) {
				if (this.surface.model[i].compInverse(this.surface.model[j]))
					if (Math.abs((i - j)) > 2)
						return i;
			}
		}
		return -1;
	}

	// search for mobius
	// returns min index for mobius
	public int mobiusSearch() {
		if(this.surface.model.length == 0)
			return -1;
		for (int i = 0; i < this.surface.getLength(); i++) {
			for (int j = i + 1; j < this.surface.getLength(); j++) {
				if (this.surface.model[i].compLet(this.surface.model[j]))
					if (Math.abs((i - j)) > 1)
						return i;
			}
		}
		return -1;
	}

	// end of basic search functions //

	// below will be all functions related to the alternating pairs algorithm //

	// step 1
	// find set of alternating pairs
	public int[] find_alternating_pairs() {
		// array to hold positions of first appearences of alternating pairs
		int[] positions = new int[2];
		for (int i = 0; i < this.surface.getLength(); i++) {
			// check to amke sure i's match is not adjacent and opposite orrientation
			if ((this.surface.findMatch(i) != i + 1)
					&& (this.surface.model[this.surface.findMatch(i)].inv != this.surface.model[i].inv)) {
				positions[0] = i;
				// set j to valuees between i and i's matching index
				for (int j = i + 1; j < this.surface.findMatch(i); j++) {
					// if j's match is larger than i's match its a possible pair
					if (this.surface.findMatch(j) > this.surface.findMatch(i)) {
						// check letters for alternate orientation
						if (this.surface.model[j].inv != this.surface.model[this.surface.findMatch(j)].inv) {
							positions[1] = j;
							// step 1 complete
							return positions;
						}
					}
				}
			}

		}
		int[] notFound = new int[] { -1, -1 };
		return notFound;
	}

	// step 2
	// once a set of alternating pairs has been found get the first pair next to
	// each other by use of the cylinder rule
	public int[] make_alt_pair_adjacent() {
		int[] positions = this.find_alternating_pairs();
		// if no alternating pairs just return the same [-1, -1]
		if (positions[0] == -1)
			return positions;
		// if already adjacent return positions
		if (positions[1] == (positions[0] + 1))
			return positions;
		// preform cylinder rule
		// the cylinder start position is position[0]
		// and since we know position[1] < cylinder end (since they are alternating
		// pairs)
		// we then use position[1] as the start read position
		// this results in the alternating pair being adjacent
		this.surface.cylinderRule(positions[0], positions[1]);
		positions[1] = (positions[0] + 1);
		// step two complete
		return positions;
	}

	// step 3
	// we now wish to get the 3rd compenent of the torus next to the compenents made
	// adjacent in step 2
	public int[] move_third_component() {
		// dont forget to initialize the first positions
		// includes step1 and step 2
		int[] positions = this.make_alt_pair_adjacent();

		// since they are alternating pairs we know that the matching letter to
		// positions[0] is between
		// the matching characters in positions [2]
		// so we now wish to repeat the process from step 2 but on positions[1]
		// if no alternating pairs just return the same [-1, -1]
		if (positions[0] == -1)
			return positions;
		// if already adjacent return positions
		if (this.surface.findMatch(positions[1]) == (positions[1] + 1))
			return positions;
		// preform cylinder rule
		// the cylinder start position is position[1]
		// and since we know position[0]'s matching character < positions[1]'s match
		// we then use position[0]'s match as the start read position
		// this results in a 3 component block of the torus
		this.surface.cylinderRule(positions[1], this.surface.findMatch(positions[0]));
		// positions matrix remains unchanged

		// step three complete
		return positions;
	}

	// step 4 is combining the last component
	// last step simply augments the word. no need to return value
	public void combine_last_component() {
		// dont forget to initialize step1, step2 and step 3
		int[] positions = this.move_third_component();

		// start to read from positions[1]
		// this results on the final compononent being inside
		// the cylder generated by the letter ar positions[0]
		this.surface.cycle(positions[1]);
		// always start reading at 1
		this.surface.cylinderRule(1, this.surface.findMatch(0));

		this.surface.cycle(this.surface.getLength() - 1);

	}

	// outer function call for neatness
	public void run_alt_pairs_algorithm() {
		this.combine_last_component();
	}

	// end of alternating pairs algorithm //

	// below are functions to remove basic blocks from the model //
	// note that sphere automatically removes a basic block
	// **note: all objects must be at front of word**

	// remove torus
	public void remove_torus() {
		int first = 0;
		int second = 9;
		String highlight = this.toString();
		highlight = this.surface.addChar(highlight, '(', first);
		highlight = this.surface.addChar(highlight, ')', second);

		System.out.println("we may express this as a sum of a Torus and some word: " + highlight);// original

		Word dummyWord = this.surface.get_copy();
		Letter[] newWord = new Letter[this.surface.getLength() - 4];
		for (int i = 4; i < this.surface.getLength(); i++) {
			newWord[i - 4] = dummyWord.model[i];
		}
		this.surface.setModel(newWord);
		if(this.toString() != null)
			System.out.println("~ T # " + this.toString());
		else
			System.out.println("~ T");
	}

	// remove plane
	public void remove_plane() {
		int first = 0;
		int second = 0;
		if (this.surface.model[0].inv == true)
			second = 7;
		else
			second = 3;
		String highlight = this.toString();
		highlight = this.surface.addChar(highlight, '(', first);
		highlight = this.surface.addChar(highlight, ')', second);

		System.out.println("we may express this as a sum of a plane and some word " + highlight);// original

		Word dummyWord = this.surface.get_copy();
		Letter[] newWord = new Letter[this.surface.getLength() - 2];
		for (int i = 2; i < this.surface.getLength(); i++) {
			newWord[i - 2] = dummyWord.model[i];
		}
		this.surface.setModel(newWord);
		if(this.toString() != null)
			System.out.println("~ P # " + this.toString());
		else
			System.out.println("~ P");
	}

	// end of removal functions //

	// combine all elements of reduction into an algorithm
	// return array of base components
	public Base[] reduce_word() {
		Base[] basic_components = new Base[this.surface.getLength() / 2];
		int count = 0;
		// check for basic components
		while(this.surface.getLength() != 0) {
			// remove spheres
			while (this.sphereSearch() != -1) {
				this.surface.sphereRule(this.sphereSearch());
				basic_components[count] = Base.SPHERE;
				count++;
			}

			// remove projective planes
			while (this.planeSearch() != -1) {
				this.surface.cycle(this.planeSearch());
				this.remove_plane();
				basic_components[count] = Base.PLANE;
				count++;
			}
			
			//look for mobius bands
			while (this.mobiusSearch() != -1) {
				this.surface.mobiusRule(this.mobiusSearch());
				this.surface.cycle(this.planeSearch());
				this.remove_plane();
				basic_components[count] = Base.PLANE;
				count++;
			}

			// remove tori
			while (this.torusSearch() != -1) {
				this.surface.cycle(this.torusSearch());
				this.remove_torus();
				basic_components[count] = Base.TORUS;
				count++;
			}

			if (this.surface.getLength() > 4) {
				if (this.find_alternating_pairs()[0] != -1)
					this.run_alt_pairs_algorithm();
			}
		}


		return basic_components;

	}

	
	
	
	// will reduce word and display its normal form
	public void get_normal_form() {
		int plane_counter = 0;
		int torus_counter = 0;
		int sphere_counter = 0;
		Base[] reduction = this.reduce_word();
		String normal_form = "";
		for (int i=0;i< reduction.length;i++) {
			if(reduction[i] != null) {
				switch (reduction[i]) {
				case SPHERE:
					sphere_counter ++;
					normal_form = normal_form + "S # ";
					break;
				case PLANE:
					plane_counter++;
					normal_form = normal_form + "P # ";
					break;
				case TORUS:
					torus_counter ++;
					normal_form = normal_form + "T # ";
					
				}
			}
		}
		// trim off last unused ( # )
		normal_form = normal_form.substring(0, normal_form.length()-3);
		
		if(plane_counter >0 && torus_counter > 0) {
			System.out.println("Since (T # P) ~ (P # P # P) ");
			System.out.println("Original Form: " + normal_form);
			normal_form = "";
			for(int k = 0; k < plane_counter + (2*torus_counter); k++) {
				normal_form = normal_form + "P # ";
			}
			normal_form = normal_form.substring(0, normal_form.length()-3);
			System.out.println(normal_form);
			
		}
		else if(plane_counter >0 && sphere_counter > 0) {
			System.out.println("Original Form: " + normal_form);
			normal_form = "";
			for(int k = 0; k < plane_counter; k++) {
				normal_form = normal_form + "P # ";
			}
			normal_form = normal_form.substring(0, normal_form.length()-3);
			System.out.println(normal_form);
		}
		else if(sphere_counter > 0){
			System.out.println("Original Form: " + normal_form);
			normal_form = "";
			for(int k = 0; k < torus_counter; k++) {
				normal_form = normal_form + "T # ";
			}
			if(sphere_counter == reduction.length)
				System.out.println("~ " +"S");
			else {
				normal_form = normal_form.substring(0, normal_form.length()-3);
				System.out.println("~ " +normal_form);
			}

		}
		else
			System.out.println(normal_form);
	}
	
	
	
	
	// helper functions listed below //

	// get string of word
	public String toString() {
		return this.surface.toString();
	}

	// returns if a 4 letter word is a torus
	public boolean isTorus() {
		if (this.surface.getLength() > 3) {
			// check every possible index for torus
			for (int i = 0; i < this.surface.getLength() - 3; i++) {
				if (this.surface.model[i].getChar() == this.surface.model[i + 2].getChar())
					if (this.surface.model[i].inv != this.surface.model[i + 2].inv)
						if (this.surface.model[i + 1].getChar() == this.surface.model[i + 3].getChar())
							if (this.surface.model[i + 1].inv != this.surface.model[i + 3].inv)
								return true;

			}
		}
		return false;

	}

}
