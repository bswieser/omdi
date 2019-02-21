
package anneal;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class AnnealView extends JComponent implements ChangeListener
    {
    private AnnealModel am;
    private boolean paintImmediate;

    public Dimension getPreferredSize()
        {
        return new Dimension( 300, 300 );
        }

    public AnnealView( anneal.AnnealModel am )
        {
        this.am = am;
        am.addChangeListener( this );
        paintImmediate = true;
        }

    public void paint( Graphics gc )
        {
        Point p1, p2;
        //Rectangle bounds = getBounds();
        // gc.clearRect( 0, 0, getWidth(), getHeight() );
        gc.setColor( Color.white );
        gc.fillRect( 0,0,getWidth(),getHeight() );
        gc.setColor( Color.black );
        for( int i = 0; i < am.size()-1; i++ )
            {
             p1 = am.getPoint( i );
             p2 = am.getPoint( i+1 );
            gc.drawLine( p1.x, p1.y, p2.x, p2.y );
            }
         p1 = am.getPoint( 0 );
         p2 = am.getPoint( am.size()-1 );
        gc.drawLine( p1.x, p1.y, p2.x, p2.y );
        }

    public void stateChanged( ChangeEvent evt )
        {
        if( !isPaintImmediate() )
            repaint();
        else
            paintImmediately( 0, 0, getWidth(), getHeight() );
        }


    public boolean isPaintImmediate()
        {
        return paintImmediate;
        }

    public boolean setPaintImmediate( boolean val )
        {
        boolean old = paintImmediate;
        paintImmediate = val;
        return old;
        }
    }
