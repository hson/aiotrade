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
package org.aiotrade.charting.chart.handledchart;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import org.aiotrade.charting.widget.WidgetModel;
import org.aiotrade.charting.chart.AbstractChart;
import org.aiotrade.charting.chart.handledchart.PeriodsChart.Model;
import org.aiotrade.charting.laf.LookFeel;
import org.aiotrade.charting.widget.Label;
import org.aiotrade.charting.widget.PathWidget;

/**
 *
 * @author Caoyuan Deng
 */
public class PeriodsChart extends AbstractChart<Model> {
    public final static class Model implements WidgetModel {
        public long t1;
        public long t2;
        
        public void set(long t1, long t2) {
            this.t1 = t1;
            this.t2 = t2;
        }
    }
    
    protected Model createModel() {
        return new Model();
    }
    
    protected void plotChart() {
        final Model model = model();
        
        Color color = LookFeel.getCurrent().drawingColor;
        setForeground(color);
        
        final int numPn = 40;
        
        float[] bs = new float[numPn * 2 + 1];
        bs[0] = bt(model.t1);
        bs[1] = bt(model.t2);
        float interval = bs[1] - bs[0];
        
        Label label1 = addChild(new Label());
        label1.setFont(LookFeel.getCurrent().axisFont);
        label1.setForeground(color);
        label1.model().set(xb((int)bs[1]) + 2, yv(datumPlane.getMinValue()), Integer.toString(Math.round(bs[1] - bs[0])));
        label1.plot();
        
        /** calculate Periods series */
        bs[1] = bs[0] + interval;
        bs[2] = bs[0] - interval;
        for (int n = 1; n < numPn; n++) {
            /** positive side */
            bs[n * 2 + 1] = bs[0] + n * interval;
            /** negative side */
            bs[n * 2 + 2] = bs[0] - n * interval;
        }
        
        final PathWidget pathWidget = addChild(new PathWidget());
        pathWidget.setForeground(color);
        final GeneralPath path = pathWidget.getPath();
        for (int bar = 1; bar <= nBars; bar += nBarsCompressed) {
            for (int i = 0; i < nBarsCompressed; i++) {
                if (bar + i == Math.round(bs[0])) {
                    plotVerticalLine(bar + i, path);
                }
                
                /** search if i is in Periods series */
                for (int j = 1; j < numPn * 2; j += 2) {
                    if (bar + i == Math.round(bs[j]) || bar + i == Math.round(bs[j + 1])) {
                        plotVerticalLine(bar + i, path);
                        break;
                    }
                }
            }
        }
        
    }
    
}


