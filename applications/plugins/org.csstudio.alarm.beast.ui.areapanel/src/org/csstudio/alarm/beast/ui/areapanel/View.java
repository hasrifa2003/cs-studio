/*******************************************************************************
 * Copyright (c) 2011 Oak Ridge National Laboratory.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.alarm.beast.ui.areapanel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.csstudio.alarm.beast.client.AlarmTreeItem;
import org.csstudio.alarm.beast.ui.GUIUpdateThrottle;
import org.csstudio.alarm.beast.ui.SeverityColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

/** Eclipse ViewPart for the AreaPanel
 *  @author Kay Kasemir
 */
public class View extends ViewPart implements AreaAlarmModelListener
{
	/** Model */
	private AreaAlarmModel model;
	
	private SeverityColorProvider color_provider;

	/** GUI box that holds the {@link AlarmPanelItem}s */
	private Composite panel_box;
	
	/** List of panels in display */
	final private List<AlarmPanelItem> panels = new ArrayList<AlarmPanelItem>();

	/** Throttle for panel_box updates */
	private GUIUpdateThrottle throttle;
	
	/** {@inheritDoc} */
	@SuppressWarnings("nls")
    @Override
	public void createPartControl(final Composite parent)
	{
		// Connect to alarms
		try
		{
			model = new AreaAlarmModel(this);
		}
		catch (Exception ex)
		{
			Activator.getLogger().log(Level.WARNING, "Error initializing area alarm model", ex);

			Label l = new Label(parent, 0);
			l.setText("Error: " + ex.getMessage());
			return;
		}
		
		// Dispose model when done
		parent.addDisposeListener(new DisposeListener()
		{
			@Override
			public void widgetDisposed(DisposeEvent e)
			{
				model.close();
			}
		});

		// Create GUI
		createAlarmPanelGUI(parent);

		// Schedule updates
		throttle = new GUIUpdateThrottle(100, 1000)
		{
			@Override
	        protected void fire()
	        {
				panel_box.getDisplay().asyncExec(new Runnable()
				{
					@Override
		            public void run()
		            {
						if (panel_box.isDisposed())
							return;
			            // Remove existing alarm panels
						for (AlarmPanelItem panel : panels)
							panel.updateAlarmState();
		            }
				});
	        }
		};
		throttle.start();
	}

	/** Create panel layout
	 *  @param parent
	 */
	private void createAlarmPanelGUI(final Composite parent)
    {
		color_provider = new SeverityColorProvider(parent);
		parent.setLayout(new FillLayout());
		panel_box = new Composite(parent, 0);
		fillPanelBox();
    }

	/** Fill <code>panel_box</code> with items for model */
	private void fillPanelBox()
    {
	    final int columns = Preferences.getColumns();
		final AlarmTreeItem[] items = model.getItems();
		panel_box.setLayout(new GridLayout(columns, false));

		for (AlarmTreeItem item : items)
		{
			final AlarmPanelItem panel = new AlarmPanelItem(panel_box, color_provider, item);
			panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			panels.add(panel);
		}
    }

	/** {@inheritDoc} */
	@Override
	public void setFocus()
	{
		// NOP
	}

	/** Re-populate the display with alarm panels
	 *  because the model has changed
	 *  {@inheritDoc}
	 */
	@Override
    public void areaModelChanged()
    {
		if (panel_box.isDisposed())
			return;
		panel_box.getDisplay().asyncExec(new Runnable()
		{
			@Override
            public void run()
            {
				if (panel_box.isDisposed())
					return;
	            // Remove existing alarm panels
				for (AlarmPanelItem panel : panels)
					panel.dispose();
				panels.clear();
				// Create new alarm panels
				fillPanelBox();
				// Force re-layout
				panel_box.layout(true, true);
            }
		});
    }

	@Override
    public void alarmsChanged()
    {
		throttle.trigger();
    }
}