package com.visualdust.deliveryBackYard.infoManagement;

public class UnrestrainedExtension implements ITagManage<Object, Object> {
    @Override
    public Object addTag(Tag<Object, Object> tag) {
        return null;
    }

    @Override
    public void removeTag(Object tagKey) {

    }

    @Override
    public void setValueOfKey(Object key, Object value) {

    }

    @Override
    public boolean checkIfThereIs(Object key) {
        return false;
    }

    @Override
    public Object getValueOf(Object key) {
        return null;
    }
}
