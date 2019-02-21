
package anneal;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.color.*;

public class AnnealModel implements Runnable
    {
    private boolean paintImmediate, useMetropolis, swap, reverse, shuffle, splice;

    private int x,y,height,width,numPoints;

    private int maxSuccesses;
    private int maxIterations;
    private double t, tFactor;

    private ChangeListener listener;

    private Random rng = new Random();

    private Point[] data;
    private Point[] trial;
    private double totalCost, theMin;

    protected AnnealModel( int numPoints )
        {
        this.numPoints = numPoints;
        trial = new Point[ numPoints ];
        data = new Point[ numPoints ];
        maxSuccesses = 5;
        maxIterations = 50;
        tFactor = .9;
        t = .5;
        useMetropolis = true;
        shuffle = false;
        splice = false;
        reverse = true;
        swap = true;
        }

    /**
        Create anneal from vector of points.
    */
    public AnnealModel( Vector vector )
        {
        }

    /**
        Create anneal from array of points
    */
    public AnnealModel( Point[] array )
        {
        }

    /**
        Create anneal using random points
    */
    public AnnealModel( int numPoints, int x, int y, int width, int height )
        {
        this( numPoints );

        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;

        doRandom();
        }

    /**
        Create anneal using random points
    */
    public AnnealModel( int numPoints, Rectangle r )
        {
        this( numPoints, r.x, r.y, r.width, r.height );
        }

    public void doRandom()
        {
        for( int i = 0; i < data.length; i++ )
            {
            data[i] = new Point( x+rng.nextInt(width), y+rng.nextInt(height) );
            }

        doCost();
        fireChanged();
        }

    /**
        Calculate total cost
    */
    public double doCost()
        {
        double cost = 0;
        int iMax = data.length-1;
        for( int i = 0; i < iMax; i++ )
            {
            cost += data[i].distance( data[i+1] );
            }
        totalCost = cost + data[0].distance( data[iMax] );
        return totalCost;
        }

    public void setT( double t )
        {
        this.t = t;
        }
    public double getT()
        {
        return t;
        }

    public double getTFactor()
        {
        return tFactor;
        }

    public double setTFactor( double t )
        {
        double old = tFactor;
        tFactor = t;
        return old;
        }

    public int getMaxSuccesses()
        {
        return maxSuccesses;
        }

    public int setMaxSuccesses( int t )
        {
        int old = maxSuccesses;
        maxSuccesses = t;
        return old;
        }

    public double getMaxIterations()
        {
        return maxIterations;
        }

    public int setMaxIterations( int t )
        {
        int old = maxIterations;
        maxIterations = t;
        return old;
        }

    public boolean setSwap(boolean nf)
        {
        boolean old = swap;
        swap = nf;
        return old;
        }

    public boolean setShuffle(boolean nf)
        {
        boolean old = shuffle;
        shuffle = nf;
        return old;
        }

    public boolean setReverse(boolean nf)
        {
        boolean old = reverse;
        reverse = nf;
        return old;
        }

    public boolean setSplice(boolean nf)
        {
        boolean old = splice;
        splice = nf;
        return old;
        }

    public boolean isSwap()
        {
        return swap;
        }

    public boolean isShuffle()
        {
        return shuffle;
        }

    public boolean isReverse()
        {
        return reverse;
        }

    public boolean isSplice()
        {
        return splice;
        }

    public boolean isUseMetropolis()
        {
        return useMetropolis;
        }

    public boolean setUseMetropolis( boolean val )
        {
        boolean old = useMetropolis;
        useMetropolis = val;
        return old;
        }

    public boolean evaluate( double oldCost, double newCost )
        {
        //double d = oldCost / newCost;
        //return d > 1;
        double dE = newCost - oldCost;	// cost difference
        if( dE < 0 )
            {
            return true;
            }


        // for metropolis to work, xde must be between 0 and 1...
        if( isUseMetropolis() )
            {
            double cost = totalCost - oldCost + newCost ; // new cost
            double r = rng.nextDouble();
            double e = cost / ((double)height*(double)width);
            double metro = Math.exp( -e/t );
            if( r < metro )
                {
                //System.out.println( ""+r+"<"+metro+"("+e+")" );
                return true;
                }
            }

        return false;
        }

    private int adjustIndex( int i )
        {
        int l = data.length;
        if( i < 0 ) return i += l;
        else return i % l;
        }

    private double cost6( int p1, int p2, int p3, int p4, int p5, int p6 )
        {
        return data[p1].distance( data[p2] ) + data[p3].distance( data[p4] ) + data[p5].distance( data[p6] );
        }

    private double cost4( int p1, int p2, int p3, int p4 )
        {
        return data[p1].distance( data[p2] ) + data[p3].distance( data[p4] );
        }

    private double cost3( int p1, int p2, int p3 )
        {
        return data[p1].distance( data[p2] ) + data[p3].distance( data[p2] );
        }

    /**
        Swap two points.
    */
    public boolean doSwap()
        {
        double costOld, costNew;
        int p1,p2,p3,p4,p5,p6;
        int l = data.length;

        // grab two points
        p1 = rng.nextInt( l );
        p2 = rng.nextInt( l );
        if( p1 == p2  ) return false;

        //
        p3 = adjustIndex( p1-1 );
        p4 = adjustIndex( p1+1 );
        p5 = adjustIndex( p2-1 );
        p6 = adjustIndex( p2+1 );
        if( p2 == p4 || p1 == p6 ) return false;

        // cost them
        costOld = cost3( p3, p1, p4 ) + cost3( p5, p2, p6 );
        costNew = cost3( p3, p2, p4 ) + cost3( p5, p1, p6 );

        //
        boolean rv = evaluate( costOld, costNew );
        if( rv ) // swap if good
            {
            Point tmp = data[p1];
            data[p1] = data[p2];
            data[p2] = tmp;
            totalCost = totalCost - costOld + costNew ;
            fireChanged();
            //verifyCost( "swap" );
            //System.out.println( "("+p3+","+p1+","+p4+") ("+p5+","+p2+","+p6+")" );
            }
        return rv;
        }

    /**
        Remove and insert segment.
    */
    public boolean doSplice()
        {
        int p1, p2, p3, p4, p5, p6;
        double costOld, costNew;
        int l = data.length;
        // grab a segment starting at random idx and cnt
        int start = rng.nextInt( l );
        int count = rng.nextInt( l );
        if( count < 2 || count >= (l-2) ) return false;
        int insert = rng.nextInt( l - count - 1 );
        if( insert == 0 ) return false;
        // usual suspects
        p1 = adjustIndex( start-1 );
        p2 = adjustIndex( start );
        p3 = adjustIndex( start+count-1 );
        p4 = adjustIndex( start+count );
        p5 = adjustIndex( start+count+insert );
        p6 = adjustIndex( start+count+insert+1 );
        //
        costOld = cost6( p1, p2, p3, p4, p5, p6 );
        costNew = cost6( p1, p4, p2, p5, p3, p6 );
        //
        boolean rv = evaluate( costOld, costNew );
        if( rv ) // splice if good
            {
            int i;
            // copy out segment
            for( i = 0; i < count; i++ )
                {
                trial[i] = data[ adjustIndex( start+i ) ];
                }
            // copy down end to insert
            for( i = 0; i <= insert; i++ )
                {
                data[ adjustIndex( start+i ) ] = data[ adjustIndex( start+count+i ) ];
                }
            // copy into
            for( i = 0; i < count; i++ )
                {
                data[ adjustIndex( start+insert+i+1 ) ] = trial[i];
                }
            totalCost = totalCost - costOld + costNew;
            // verifyCost( "splice" );
            fireChanged();
            }
        return rv;
        }

    /**
        Reverse a segment.
    */
    public boolean doReverse()
        {
        int p1, p2, p3, p4;
        double costOld, costNew;
        int l = data.length;
        // grab a segment starting at random idx and cnt
        int start = rng.nextInt( l );
        int count = rng.nextInt( l );
        if( count < 2 || count >= (l-2) ) return false;
        // I know that reversal does not change cost of segment
        p1 = adjustIndex( start-1 );
        p2 = adjustIndex( start );
        p3 = adjustIndex( start+count-1 );
        p4 = adjustIndex( start+count );
        //
        costOld = cost4( p1, p2, p3, p4 );
        costNew = cost4( p1, p3, p2, p4 );
        //
        boolean rv = evaluate( costOld, costNew );
        if( rv ) // reverse if good
            {
            int half = count >> 1;
            for( int i = 0; i < half; i++ )
                {
                int i1 = adjustIndex( start+i );
                int i2 = adjustIndex( start+count-1-i );
                Point tmp = data[i1];
                data[i1] = data[i2];
                data[i2] = tmp;
                }
            totalCost = totalCost - costOld + costNew;
            //verifyCost( "reverse" );
            fireChanged();
            }
        return rv;
        }

    /**
        Shuffle a segment.

        Shuffle of a segment radically changes cost.
        Shuffle will only operate on even count.
    */
    public boolean doShuffle()
        {
        int p1, p2, p3, p4;
        int l = data.length;
        int start = rng.nextInt( l );
        int count = rng.nextInt( l ) & 0xFFFFFFFE;
        if( count < 2 || count >= (l-2) ) return false;

        // calculate old cost of segment
        double oldCost = 0;
        for( int i = 0; i < (count-1); i++ )
            {
            p1 = adjustIndex( start+i );
            p2 = adjustIndex( start+i+1 );
            oldCost += data[p1].distance( data[p2] );
            }
        // adjust for end points
        //p1 = adjustIndex( start-1 );
        //p2 = adjustIndex( start );
        //p3 = adjustIndex( start+count-1 );
        //p4 = adjustIndex( start+count );
        //oldCost += cost4( p1, p2, p3, p4 );

        // make a map
        int half = count >> 1;
        for( int i = 0; i < half; i++ )
            {
            trial[i<<1] = data[ adjustIndex( start+i ) ];
            trial[(i<<1)+1] = data[ adjustIndex( start+half+i ) ];
            }

        // calculate cost of map
        double newCost = 0;
        for( int i = 0; i < (count-1); i++ )
            {
            newCost += trial[i].distance( trial[i+1] );
            }
        // adjust for end points
        //p1 = adjustIndex( start-1 );
        //p2 = adjustIndex( start );
        //p3 = adjustIndex( start+count-1 );
        //p4 = adjustIndex( start+count );
        //newCost += cost4( p1, p2, p3, p4 );
        //
        boolean rv = evaluate( oldCost, newCost );
        if( rv ) // shuffle if good
            {
            for( int i = 0; i < count; i++ )
                {
                data[ adjustIndex( start+i ) ] = trial[i];
                }
            totalCost = totalCost - oldCost + newCost;
            //verifyCost("shuffle");
            fireChanged();
            }
        return rv;
        }

    public int size() { return data.length; }
    public Point getPoint( int idx ) { return data[idx]; }
    public void addChangeListener( ChangeListener listener )
        {
        this.listener = listener;
        }
    public void fireChanged()
        {
        if( listener != null )
            listener.stateChanged( new ChangeEvent( this ) );
        }

    public void verifyCost( String s )
        {
        double d = totalCost;
        doCost();
        if( Math.abs(totalCost - d) > 0.000001 )
            {
            System.err.println( s + " failed " + d + "!=" + totalCost );
            }
        }

    //public void doAnneal()
    public void run()
        {
        int swaps = 0;
        int reverses = 0;
        int shuffles = 0;
        int splices = 0;

        int gswaps = 0;
        int greverses = 0;
        int gshuffles = 0;
        int gsplices = 0;

        setT( .01 );
        for( int i = 0; i < 100; i++ )
            {
            int successes = 0;
            for( int j = 0; j < ( getMaxIterations() * size()); j++ )
                {
                switch( rng.nextInt(4) )
                    {
                    case 0: if( reverse ) { if( doReverse() ) {successes++; greverses++;} reverses++; break; }
                    case 1: if( swap ) { if( doSwap() ) {successes++; gswaps++;} swaps++; break; }
                    case 2: if( splice ) { if( doSplice() ) {successes++; gsplices++;} splices++; break; }
                    case 3: if( shuffle ) { if( doShuffle() ) {successes++; gshuffles++;} shuffles++; break; }
                    default:
                        continue;
                    }
                if( successes > ( getMaxSuccesses() * size()) )
                    break;
                }
            if( successes == 0 )
                break;
            setT( getT() * getTFactor() );
            }
            System.out.println( "Total Cost " + totalCost );
//            System.out.println("Reverses " + greverses + " of " + reverses );
//            System.out.println("Swaps " + gswaps + " of " + swaps );
//            System.out.println("Splices " + gsplices + " of " + splices );
//            System.out.println("Shuffles " + gshuffles + " of " + shuffles );
        }

    public int setSize( int numPoints )
        {
        int old = this.numPoints;

        this.numPoints = numPoints;

        trial = new Point[ numPoints ];
        data = new Point[ numPoints ];

        return old;
        }

    public int getSize()
        {
        return numPoints;
        }
    }