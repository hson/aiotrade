/*
 * Copyright (c) 2006-2007, AIOTrade Computing Co. and Contributors
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *    
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *    
 *  o Neither the name of AIOTrade Computing Co. nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.aiotrade.lib.indicator.function;

import org.aiotrade.lib.math.timeseries.computable.Opt;
import org.aiotrade.lib.math.timeseries.Ser;
import org.aiotrade.lib.math.timeseries.Var;

/**
 *
 * @author Caoyuan Deng
 */
class CCIFunction extends AbstractFunction {
    
    var alpha, period :Opt = _
    
    val _tp        = new DefaultVar[Float]
    val _deviation = new DefaultVar[Float]
    
    val _cci = new DefaultVar[Float]
    
    override
    def set(baseSer:Ser, args:Seq[_]) :Unit = {
        super.set(baseSer, Nil)
        
        this.period = args(0).asInstanceOf[Opt]
        this.alpha = args(1).asInstanceOf[Opt]
    }
    
    def idEquals(baseSer:Ser, args:Seq[_]) :Boolean = {
        this._baseSer == baseSer &&
        this.period == args(0) &&
        this.alpha == args(1)
    }
    
    protected def computeSpot(i:Int) :Unit = {
        _tp(i) = (H(i) + 2 * C(i) + L(i)) / 4f
        
        if (i < period.value - 1) {
            
            _deviation(i) = Float.NaN
            
            _cci(i) = Float.NaN
            
        } else {
            
            val tp_ma_i = ma(i, _tp, period)
            
            _deviation(i) = Math.abs(_tp(i) - tp_ma_i)
            val deviation_ma_i = ma(i, _deviation, period)
            
            _cci(i) = (_tp(i) - tp_ma_i) / (alpha.value * deviation_ma_i)
            
        }
    }
    
    def cci(sessionId:Long, idx:int) :Float = {
        computeTo(sessionId, idx)
        
        _cci(idx)
    }
    
}



