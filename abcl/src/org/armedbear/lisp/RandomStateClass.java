package org.armedbear.lisp;

import static org.armedbear.lisp.Lisp.*;

public class RandomStateClass 
	extends StructureClass {

	public static final LispObject STATE_SLOT = new Symbol("STATE"); // Intern me someplace?
	
	public RandomStateClass() {
		super(Symbol.RANDOM_STATE);
		
		int n_slots = 1;
		
		// Allocate Slots
		Cons slots;
		//SimpleVector slots = new SimpleVector(1);
		LispObject[] instanceSlotNames = new LispObject[1];
		
		// Define State Slot
		
		//SlotDefinition stateSlotDefinition = new SlotDefinition(STATE_SLOT,NIL);
		
		SimpleVector stateSlotDefinition = new SimpleVector(2);
		
		stateSlotDefinition.aset(1, STATE_SLOT);
		//slots.aset(0, stateSlotDefinition);
		
		slots = new Cons(stateSlotDefinition,NIL);
		//setCPL(new Cons(this,NIL));
		
		
		
        //setSlotDefinitions(slots);
        setSlotDefinitions(slots);
        
        instanceSlotNames[0] = STATE_SLOT;

        setClassLayout(new Layout(this, instanceSlotNames, NIL));
        setDefaultInitargs(computeDefaultInitargs());
        setFinalized(true);
	}
	
	

}
