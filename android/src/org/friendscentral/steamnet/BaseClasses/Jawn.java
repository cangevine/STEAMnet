package org.friendscentral.steamnet.BaseClasses;

import java.io.Serializable;

import org.friendscentral.steamnet.BaseClasses.Spark;
import org.friendscentral.steamnet.BaseClasses.Idea;

@SuppressWarnings("serial")
public abstract class Jawn implements Serializable {
	public abstract char getType();
	public abstract Spark getSelfSpark();
	public abstract Idea getSelfIdea();
}
