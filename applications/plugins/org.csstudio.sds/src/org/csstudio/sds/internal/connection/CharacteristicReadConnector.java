package org.csstudio.sds.internal.connection;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.csstudio.platform.logging.CentralLogger;
import org.csstudio.platform.model.pvs.ValueType;
import org.epics.css.dal.DataExchangeException;
import org.epics.css.dal.simple.AnyData;
import org.epics.css.dal.simple.AnyDataChannel;
import org.epics.css.dal.simple.ChannelListener;

public class CharacteristicReadConnector implements PropertyChangeListener {
	private ChannelInputProcessor channelInputProcessor;
	private String characteristic;
	private ValueType valueType;

	public CharacteristicReadConnector(
			ChannelInputProcessor channelInputProcessor, ValueType valueType,
			String characteristic) {
		assert channelInputProcessor != null;
		assert valueType != null;
		assert characteristic != null;
		this.channelInputProcessor = channelInputProcessor;
		this.valueType = valueType;
		this.characteristic = characteristic;
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (characteristic.equalsIgnoreCase(event.getPropertyName())) {
			channelInputProcessor.valueChanged(event.getNewValue());
		}
	}
}
