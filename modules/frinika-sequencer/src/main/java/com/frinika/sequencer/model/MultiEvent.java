/*
 * Created on Jan 10, 2006
 *
 * Copyright (c) 2005 Peter Johan Salomonsen (http://www.petersalomonsen.com)
 * 
 * http://www.frinika.com
 * 
 * This file is part of Frinika.
 * 
 * Frinika is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * Frinika is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Frinika; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.frinika.sequencer.model;

import com.frinika.model.EditHistoryRecordable;
import com.frinika.sequencer.FrinikaTrackWrapper;
import com.frinika.sequencer.gui.Item;
import java.io.Serializable;

/**
 * MultiEvent is a representation of one or more MidiEvents that form a note pair,
 * a control change/pitch bend envelope or a single MidiEvent.
 * 
 * Note that the clone() method must be implemented for all non-abstract extensions
 * of the MultiEvent class. This is for the edithistory to work properly.
 * 
 * @author Peter Johan Salomonsen
 */
public abstract class MultiEvent implements Comparable,Serializable,EditHistoryRecordable,Item,Selectable,Cloneable {    
    private static final long serialVersionUID = 1L;

    transient static long multiEventCounter = 1; // For generating unique multiEventIDs 
    
    MidiPart part;
    
    long multiEventID = 0;
    protected long startTick;
    private Integer trackerColumn; // The prefered column number to use in the tracker view
    
    transient boolean selected = false;
    transient MultiEventEndTickComparable multiEventEndTickComparable = null;
    
    // zombies have not been commited to a FTW (used by the pianoroll for displaying notes).
    transient boolean zombie=true;
    
    /*
     * Protected constructor used by SubsetMultiEvent
     * This doesn't set a multiEventID resulting that it always will be ordered as
     * the first in a group of MultiEvents with the same tick. This should never
     * be added to a track.
     */ 
    protected MultiEvent(long startTick)
    {
        this.startTick = startTick;
    }
    
    /**
     * 
     * @param track
     * @param startTick
    * @deprecated
     */
    public MultiEvent(FrinikaTrackWrapper track,long startTick)
    {
   // 	System. out.println(" Creating MultiEvent ");
//    	assert(false);
       // this.multiEventGroup = track.defaultGroup;
        this.startTick = startTick;
        this.multiEventID = multiEventCounter++; 
    }


    public MultiEvent(MidiPart part,long startTick)
    {
        this.part = part;
        this.startTick = startTick;
        this.multiEventID = multiEventCounter++; 
    }

    /**
     * @return Returns the startTick.
     */
    public long getStartTick() {
        return startTick;
    }
    
    /**
     * sub classes should override this.
     * @return Returns the endTick 
     */
    abstract public long getEndTick();
    
    /**
     * 
     * @param startTick The startTick to set.
     */
    public void setStartTick(long startTick) {
        this.startTick = startTick;
        /**
         * NOTE: Previosly add and remove from track was removed from here
         * Do this in the MultiEventChangeRecorder style instead
         */
    }    
 
    /**
     * 
     * @return
    * @deprecated
     */
    
    public final FrinikaTrackWrapper getTrack()
    {
    		return ((MidiLane)(part.lane)).getTrack();
    }

    
    
	public MidiPart getMidiPart() {
		return part;
	}

    /**
     * Remove the MidiEvents generated by this MultiEvent from the track
     * and fire CommitEvent to CommitListener
     *
     */
    void commitRemove() {
    	commitRemoveImpl();
    	getMidiPart().fireCommitRemove(this); // Jens
    }
    
    /**
     * Add the MidiEvents generated by this MultiEvent to the track
     * and fire CommitEvent to CommitListener
     *
     */
    public void commitAdd() {
    	commitAddImpl();
    	getMidiPart().fireCommitAdd(this); // Jens
    }
    
    /**
     * Remove the MidiEvents generated by this MultiEvent from the track
     *
     */
    abstract void commitRemoveImpl(); // Jens, name-change was necessary for allowing listener-notification (which, in turn, is required for implementing ghosts)
    
    /**
     * Add the MidiEvents generated by this MultiEvent to the track
     *
     */
    abstract void commitAddImpl(); // Jens, name-change was necessary for allowing listener-notification (which, in turn, is required for implementing ghosts)
    
    /**
     * This method will be run by the processor handler 
     *@deprecated Do not use this - it was previously used by processors - but they're broken - the correct procedure is: remove from part - change - add to part again
     */
   public  void commitChanges()
    {
        commitRemove();
        commitAdd();
    }
    
    @Override
    public int compareTo(Object obj)
    {
        int ret = new Long(startTick).compareTo(((MultiEvent)obj).getStartTick());
        // In case the tick is the same use multiEventID for ordering
        if(ret==0 && obj != this)
        {
            return new Long(multiEventID).compareTo(((MultiEvent)obj).multiEventID);
        }
        else
            return ret;
    }
    

    @Override
    public void setSelected(boolean yes) {
    	if (selected == yes) return;
    	selected = yes;
    }

    @Override
    public boolean isSelected() {
    	return selected;
    }

   
	/**
	*  Create a parentless copy to allow serialiation without explosions
    */
    public MultiEvent detachedCopy() {
		MultiEvent ev=null;
		try {
			ev = (MultiEvent)clone();
			ev.part=null;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ev;
    }
    
    @Override
	public Selectable deepCopy(Selectable parent) {
		MultiEvent ev=null;
		try {
			ev = (MultiEvent)clone();
			if (parent != null ) {
				ev.part=(MidiPart)parent;
			} else {
				ev.part=part;
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ev;
	}
	
    public MidiPart getPart() {
    	return part;
    }
    
    /*
     * Added these 2 so NoteEvent and ControllerEvent can share methods for the ControllerView
     */
    public int getValue() {return 0;}

    public void setValue(int val) {}  
    
    /*
     * Override these if you want the GUI to map low level values onto the GUI display values
     * (a bit of a hack)
     * 
     */
    public int getValueUI() {return getValue();}

    public void setValueUI(int val) {setValue(val);}  
	
    @Override
    public void deepMove(long tick) {
		startTick+=tick;	
	}
    
    @Override
    public void removeFromModel() {
		part.remove(this);
	}
    
    @Override
    public void addToModel() {
		part.add(this);
	}
    
    @Override
    public long leftTickForMove() {
		return startTick;
	}
    /**
     * PLease override if need be
     */
    @Override
    public long rightTickForMove() {
		return startTick;
	}

    /**
     * The prefered column to use in a tracker view. In case of no prefered column NULL is returned
     * @return
     */
    public Integer getTrackerColumn() {
        return trackerColumn;
    }
    public void setTrackerColumn(int column) {
        this.trackerColumn = column;
        
    }
    
    /**
     * Get a comparable wrapper for sorting on end tick
     * @return
     */
    public MultiEventEndTickComparable getMultiEventEndTickComparable() {
        if(multiEventEndTickComparable == null)
            multiEventEndTickComparable = new MultiEventEndTickComparable(this);
        return multiEventEndTickComparable;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
    	MultiEvent evt = (MultiEvent)super.clone();
        evt.multiEventID = multiEventCounter++; 
    	evt.setSelected(false);
    	return evt;
    }
    
    public boolean isZombie() {
    	return zombie;   	
    }
    
}
