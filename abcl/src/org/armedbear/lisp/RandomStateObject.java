/*
 * RandomState.java
 *
 * Copyright (C) 2003-2005 Peter Graves
 * $Id$
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */

package org.armedbear.lisp;

import static org.armedbear.lisp.Lisp.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Random;

public final class RandomStateObject 
	extends StructureObject
{

	protected Object lock = new Object();
    private Random random;

    protected long seed[];
    
    public RandomStateObject()
    {
    	super(Symbol.RANDOM_STATE);
    	init(T);
    }

    public RandomStateObject(LispObject rs)
    {
    	super(Symbol.RANDOM_STATE);
    	init(rs);
    }

    private void init(LispObject rs) {
    	
    	if (rs == NIL) {
    		rs = (RandomStateObject)Symbol._RANDOM_STATE_.symbolValue();
    	}
    	
    	if (rs instanceof RandomStateObject) {
    		RandomStateObject rso= (RandomStateObject)rs;
    		synchronized(rso.lock) {
    			seed = new long[rso.seed.length];
    			for(int i=0;i<seed.length;i++) {
    				seed[i] = rso.seed[i];
    			}
    		}
    	} else if (rs == T) {
    		Random tmp_rand = new Random();
    		int len = (int)(100 + ((Math.abs(tmp_rand.nextLong())) % 900));
    		
    		seed = new long[len];
    		for(int i=0;i<len;i++) {
    			seed[i] = ((i % 2) > 0) ? Long.MAX_VALUE : 0;
    			seed[i] = tmp_rand.nextLong() ^ seed[i];
    		}
    		
    	} else {
    		RandomStateObject rso= (RandomStateObject)Symbol._RANDOM_STATE_.symbolValue();
    		synchronized(rso.lock) {
    			seed = new long[rso.seed.length];
    			for(int i=0;i<seed.length;i++) {
    				seed[i] = rso.seed[i];
    			}
    		}
    	}
    	
    	random = new Random(parseSeed());

    }
    
    private long parseSeed() {
    	long s = 0;
    	int r;
    	synchronized(lock) {
    		for(int i=0;i<seed.length;i++) {
    			r = i % 64;
    			s  = s ^ ((seed[i] >> r) | (seed[i] << (64 - r)));  
    		}
    	}
    	return s;
    }
    
    private void nextSeed() {
    	synchronized(lock) {
    		for(int i=0;i<seed.length;i++) {
    			seed[i] = random.nextLong();
    		}
    		random.setSeed(parseSeed());
    	}
    }
    
    
    @Override
    public LispObject typeOf()
    {
        return Symbol.RANDOM_STATE;
    }

    @Override
    public LispObject classOf()
    {
        return BuiltInClass.RANDOM_STATE;
    }

    @Override
    public LispObject typep(LispObject type)
    {
        if (type == Symbol.RANDOM_STATE)
            return T;
        if (type == BuiltInClass.RANDOM_STATE)
            return T;
        return super.typep(type);
    }

    @Override
    public LispObject printObject()
    {
        return new SimpleString(unreadableString("RANDOM-STATE"));
    }

    public LispObject random(LispObject arg)
    {
        if (arg instanceof Fixnum) {
            int limit = ((Fixnum)arg).value;
            if (limit > 0) {
                int n = random.nextInt((int)limit);
                nextSeed();
                return Fixnum.getInstance(n);
            }
        } else if (arg instanceof Bignum) {
            BigInteger limit = ((Bignum)arg).value;
            if (limit.signum() > 0) {
                int bitLength = limit.bitLength();
                BigInteger rand = new BigInteger(bitLength + 1, random);
                nextSeed();
                BigInteger remainder = rand.remainder(limit);
                
                return number(remainder);
            }
        } else if (arg instanceof SingleFloat) {
            float limit = ((SingleFloat)arg).value;
            if (limit > 0) {
                float rand = random.nextFloat();
                nextSeed();
                return new SingleFloat(rand * limit);
            }
        } else if (arg instanceof DoubleFloat) {
            double limit = ((DoubleFloat)arg).value;
            if (limit > 0) {
                double rand = random.nextDouble();
                nextSeed();
                return new DoubleFloat(rand * limit);
            }
        }
        return type_error(arg, list(Symbol.OR,
                                          list(Symbol.INTEGER, Fixnum.ONE),
                                          list(Symbol.FLOAT, list(Fixnum.ZERO))));
    }

    // ### random limit &optional random-state => random-number
    private static final Primitive RANDOM =
        new Primitive(Symbol.RANDOM, "limit &optional random-state")
    {
        @Override
        public LispObject execute(LispObject arg)
        {
            RandomStateObject randomState =
                (RandomStateObject) Symbol._RANDOM_STATE_.symbolValue();
            return randomState.random(arg);
        }
        @Override
        public LispObject execute(LispObject first, LispObject second)

        {
            if (second instanceof RandomStateObject) {
                RandomStateObject randomState = (RandomStateObject) second;
                return randomState.random(first);
            }
            return type_error(first, Symbol.RANDOM_STATE);
        }
    };

    // ### make-random-state &optional state
    private static final Primitive MAKE_RANDOM_STATE =
        new Primitive(Symbol.MAKE_RANDOM_STATE, "&optional state")
    {
        @Override
        public LispObject execute()
        {
            return new RandomStateObject((RandomStateObject)Symbol._RANDOM_STATE_.symbolValue());
        }
        @Override
        public LispObject execute(LispObject arg)

        {
            if (arg == NIL)
                return new RandomStateObject((RandomStateObject)Symbol._RANDOM_STATE_.symbolValue());
            if (arg == T)
                return new RandomStateObject();
            if (arg instanceof RandomStateObject)
                return new RandomStateObject((RandomStateObject)arg);
            return type_error(arg, Symbol.RANDOM_STATE);
        }
    };

    // ### random-state-p
    private static final Primitive RANDOM_STATE_P =
        new Primitive(Symbol.RANDOM_STATE_P, "object")
    {
        @Override
        public LispObject execute(LispObject arg)
        {
            return arg instanceof RandomStateObject ? T : NIL;
        }
    };
}
