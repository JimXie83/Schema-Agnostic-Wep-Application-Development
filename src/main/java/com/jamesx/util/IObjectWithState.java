package com.jamesx.util;


public interface IObjectWithState
{
    //ObjectState objectState =ObjectState.Unchanged;
    public ObjectState getObjectState();
    public void setObjectState(ObjectState objectState);
}
